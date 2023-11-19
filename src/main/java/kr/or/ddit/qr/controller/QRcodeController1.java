package kr.or.ddit.qr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 첫번째 QR 로그인 방법 QR코드 링크 생성하기
 */
@Slf4j
@Controller
@RequestMapping("/qr1")
public class QRcodeController1 {

	@GetMapping("/test")
	public String test() {
		return "qr1/createQRcodeLink";
	}

//	@PostMapping("/getQRcode")
//	public ResponseEntity<byte[]> qrToTistory(@RequestParam String url) throws WriterException, IOException {
//		
//		int width = 200;
//		int height = 200;
//
//		BitMatrix encode = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
//
//		// 파일 저장을 위한 객체 생성 
//		File file = new File("qr저장링크");
//		FileOutputStream fileOutputStream = new FileOutputStream(file);
//
//		try {
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//			MatrixToImageWriter.writeToStream(encode, "PNG", out);
//
////			byte[] imageBytes = out.toByteArray();
////
////		    HttpHeaders headers = new HttpHeaders();
////		    headers.setContentType(MediaType.IMAGE_PNG);
//
////		    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//
//			
//			// 파일 저장부분 
//			// fileOutputStream.write(out.toByteArray());
//			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(out.toByteArray());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			fileOutputStream.close();
//		}
//
//		return null;
//	}
	
	@ResponseBody
	@PostMapping(value="/getQRcode")
	public Map <String, Object> qrToTistory(@RequestParam String url) throws WriterException, IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		log.info("url : " + url);
		
	    int width = 200;
	    int height = 200;
	    
	    // qr생성부분
	    BitMatrix encode = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(encode, "PNG", out);

	    // out을 Base64로 인코딩 후 반환
	    byte[] imageBytes = out.toByteArray();
	    String base64Encoded = Base64.getEncoder().encodeToString(imageBytes);
	    log.info(base64Encoded);
	    map.put("base64Encoded",base64Encoded);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);

	    return map;
	}

}