package kr.or.ddit.qr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import lombok.extern.slf4j.Slf4j;

/*
 * 두번째, 구글 Authentication 어플을 이용해 최초 1회 QR을 찍은 후, otp 인증 방식으로 2차로그인 구현
 * 1. 회원가입시 홈페이지에서, 또는 이메일로 QR코드를 전송 시켜준다.(여기선 홈페이지에서 보여줬는데 이메일로 전송 후 사용자에게 QR을 찍고 삭제하라고 하는 방법이 나아보임)
 * 2. 로그인 시 2차인증으로 otp 요구
 */
@RequestMapping("/qr2")
@Slf4j
@Controller
public class QRcodeController2 {

	// QR 생성 후 페이지에 보여주기
	@GetMapping("/showQR")
	public String showQR(Model model) {
		String secretKey = getSecretKey();
		String format = "http://chart.apis.google.com/chart?cht=qr&amp;chs=300x300&amp;chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s&amp;chld=H|0";
		String QRUrl = String.format(format, "test", "ddit.kr.or", secretKey);
		
		model.addAttribute("url", QRUrl);
		model.addAttribute("secretKey", secretKey);
		
		return "qr2/showQR";
	}
	
	// 2차 인증
	@PostMapping("/login")
	public String login(Model model, @RequestParam int userEnteredKey, @RequestParam String secretKey) {
		
		if(checkCode(secretKey, userEnteredKey)) {
			model.addAttribute("isSuccess", "true");
		} else {
			model.addAttribute("isSuccess", "false");
		}
		
		return "qr2/result";
	}
	
	// 무작위 난수인 secretKey 얻기
	public String getSecretKey() {
		GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
		GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        String secretKey = key.getKey();
        
        return secretKey;
	}
	
	// secretKey와 유저가 입력한 키를 비교 후 올바른 otp면 true, 아니면 false를 리턴
	public boolean checkCode(String secretKey, int userEnteredCode) {
		GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
		return googleAuthenticator.authorize(secretKey, userEnteredCode);
	}
}
