package kr.or.ddit.qr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 첫번째 QR 로그인 방법 QR코드 링크 생성하기
 */
@Controller
@RequestMapping("/qr1")
public class QRcodeController1 {

	@GetMapping("/test")
	public String test() {
		return "qr1/createQRcodeLink";
	}

	@ResponseBody
	@PostMapping("/getQRcode")
	public ResponseEntity<byte[]> qrToTistory(@RequestParam String url) throws WriterException, IOException {
		int width = 200;
		int height = 200;

		BitMatrix encode = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);

		// 파일 저장을 위한 객체 생성 
		File file = new File("qr저장링크");
		FileOutputStream fileOutputStream = new FileOutputStream(file);

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			MatrixToImageWriter.writeToStream(encode, "PNG", out);

			// 파일 저장부분 
			// fileOutputStream.write(out.toByteArray());

			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(out.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fileOutputStream.close();
		}

		return null;
	}

}