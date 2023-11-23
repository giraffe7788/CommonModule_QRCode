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
 * 1. QR생성시 해당 QR코드를 로컬 폴더에 저장하며
 * 2. Ajax를 통해 화면의 이동 없이 현재 보고있는 홈페이지의 특정 부분에 $('이미지태그').attr('src', 'data:image/png;base64,' + data.base64Encoded); 같이 src부분을 바꾸면 기존 사진이 qr사진으로 바뀝니다
 * 또는 동적으로 img태그를 생성 후 append시키면 됩니다
 * 중요한건 이미지의 src에 base64Encoded를 넣으면 qr이 생성됩니다
 */
@Slf4j
@Controller
@RequestMapping("/qr1")
public class QRcodeController1 {

	@GetMapping("/test")
	public String test() {
		return "qr1/createQRcodeLink";
	}
	
	@ResponseBody
	@PostMapping(value="/getQRcode")
	public Map <String, Object> qrToTistory(@RequestParam String url) throws WriterException, IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 입력된 url
		log.info("url : " + url);
		
		// qr코드 생성부분
	    int width = 200;
	    int height = 200;
	    
	    // qr생성부분
	    BitMatrix encode = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(encode, "PNG", out);

	    // 파일 저장 부분
	    File file = new File("qr저장링크");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(out.toByteArray());
	    
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