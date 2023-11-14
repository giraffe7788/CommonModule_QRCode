package kr.or.ddit.qr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import org.apache.commons.codec.binary.Base32;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/************
 * @info : QR Code 생성 및 제공 Controller
 * @name : QrController
 * @date : 2023/03/24 5:32 PM
 * @version : 1.0.0
 * @Description :
 ************/
@Controller
@RequestMapping("/qr")
public class QRcodeController {

	/*
	 * 방법1. 구글OTP어플을 이용해 가입시 최초 1회 QR코드를 찍고 OTP로 로그인
	 */
	@GetMapping("/test")
	public ModelAndView test(ModelAndView mav) {

		// 시크릿 키 생성
		// Google Authenticator 앱과 서버 간에 공유되는 비밀 키
		// 이 비밀 키는 사용자의 디바이스에서 Google Authenticator 앱을 설정할 때 사용
		// 이 비밀 키를 공유함으로써, Google Authenticator 앱은 각 로그인 시간마다 새로운 OTP(일회용 비밀번호)를 생성
		String secretKeyStr = "TESTKEY";
		String format = "http://chart.apis.google.com/chart?cht=qr&amp;chs=300x300&amp;chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s&amp;chld=H|0";
		String QrUrl = String.format(format, "test", "ddit.kr.or", secretKeyStr);

		mav.addObject("secretKey", secretKeyStr);
		mav.addObject("QrUrl", QrUrl);

		mav.setViewName("qr/getOtpQR");
		return mav;
	}

	// 1차 인증 페이지로 이동
	@GetMapping("/FirstLoginPage")
	public String FirstLoginPage() {
		return "qr/FirstLoginPage";
	}

	// 2차 인증 페이지 이동
	@PostMapping("/secondLoginPage")
	public ModelAndView SecondLoginPage(ModelAndView mav, @RequestParam String id, @RequestParam String pw) {

		// 테스트용 아이디 비번
		String testId = "admin";
		String testPw = "123";

		// 1차 로그인 실패시 처음화면으로 돌아감
		if (!id.equals(testId) || !pw.equals(testPw)) {
			mav.addObject("result", "fail");
			mav.setViewName("qr/FirstLoginPage");
			return mav;
		}

		mav.setViewName("qr/SecondLoginPage");
		return mav;
	}

	// 2차 인증 로직
	@PostMapping("/thirdLoginPage")
	public String ThirdLoginPage(Model model, @RequestParam String code) {

		int userCode = Integer.parseInt(code);
		// 비밀키, 지금은 위에꺼 그대로 갖다썼지만 나중엔 회원가입시 최초1번 무작위 키 생성 로직으로 생성하고 DB에 넣어두던가, 아니면 유저ID
		// 같은 고유키를 이용해 규칙적인 암호로 생성해서 쓰던가 하면 된다
		String secretKeyStr = "TESTKEY";
		// OTP 시간 체크용
		long time = new Date().getTime() / 30000;

		boolean check_code = false;

		try {
			// 키, 코드, 시간으로 일회용 비밀번호가 맞는지 일치 여부 확인.
			check_code = check_code(secretKeyStr, userCode, time);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		// 인증 성공시
		if (check_code) {
			return "qr/thirdLoginPage";
			// 실패시 다시
		} else {
			model.addAttribute("result", "fail");
			return "qr/SecondLoginPage";
		}
	}

	
	// 코드체크 알고리즘
	// 비밀키, 입력코드, 시간
	private static boolean check_code(String secretKey, long code, long time)
			throws NoSuchAlgorithmException, InvalidKeyException {
		
		// 시크릿키를 32개의 문자로 변환
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secretKey);

		// 유효성 검사의 범위 설정, 생성된 코드가 현재 시간을 중심으로 일정한 시간 범위 내에 있는지 확인
		// 사용자가 시간에 맞게 OTP를 입력했지만 약간의 오차가 있을 수 있으므로 이 변수를 사용하여 더 넓은 범위에서 코드를 확인
		int window = 3;
		for (int i = -window; i <= window; ++i) {
			// 실제로 검사를 하는 부분
			long hash = verifyGoogleOtp(decodedKey, time + i);

			// 성공시(입력한 값과 생성된 OTP의 값이 같으면)
			if (hash == code) {
				return true;
			}
		}
		
		return false;
	}

	
	// 구글 OTP 어플과 같은 알고리즘으로 번호를 생성 후, 입력된 번호와 비교한다
	// 겉으로 보기엔 서버에서 디바이스로 OTP를 전송하는 것 처럼 보이지만
	// 사실은 개인secretKey+시간 으로 무작위 키를 어플 및 서버에서 같은 알고리즘으로 생성한다음, 
	// 서버에서 디바이스에 나타난 번호를 입력받고비교하는것
	private static int verifyGoogleOtp(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}

		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);

		int offset = hash[20 - 1] & 0xF;

		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			// We are dealing with signed bytes:
			// we just keep the first byte.
			truncatedHash |= (hash[offset + i] & 0xFF);
		}

		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;

		return (int) truncatedHash;
	}

	/*
	 * 
	 * 방법2. QR코드로 링크 생성하기
	 * 
	 * localhost/qr/test로 접속
	 * 
	 * @GetMapping("/test") public ModelAndView test(ModelAndView mav) {
	 * mav.setViewName("qr/createQRcodeLink"); return mav; }
	 * 
	 * @PostMapping("/getQRcode") public ResponseEntity<byte[]>
	 * qrToTistory(@RequestParam String url) throws WriterException, IOException {
	 * int width = 200; int height = 200;
	 * 
	 * BitMatrix encode = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE,
	 * width, height);
	 * 
	 * // 파일 저장을 위한 객체 생성 File file = new File("qr저장링크"); FileOutputStream
	 * fileOutputStream = new FileOutputStream(file);
	 * 
	 * try { ByteArrayOutputStream out = new ByteArrayOutputStream();
	 * 
	 * MatrixToImageWriter.writeToStream(encode, "PNG", out);
	 * 
	 * // 파일 저장부분 fileOutputStream.write(out.toByteArray());
	 * 
	 * return
	 * ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(out.toByteArray());
	 * 
	 * } catch (Exception e){ e.printStackTrace(); } finally {
	 * fileOutputStream.close(); }
	 * 
	 * return null; }
	 */
}