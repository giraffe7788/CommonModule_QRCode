<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>QR코드 링크 생성기</title>
</head>
<body>
<p>QR화 시키고 싶은 링크를 입력 후 버튼을 누르면 해당 링크로 QR코드가 생성됩니다</p>
<form action="/qr1/getQRcode" method="post">
	<input type="text" name="url" id="url" placeholder="접속하고싶은 url">
	<br><br>
	<button type="button" id="submitBtn">제출</button>
	<br><br>
	<button type="button" id="btnNaver">네이버 링크 QR코드 생성</button>
	<img id="qrCodeImage" alt="QR Code" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADIAQAAAACFI5MzAAABIUlEQVR42u2YTRKDIAyF44pjeFORm3IMVqTJi1Rq7bbJQgYd5GORyc8LI/GvQQ95yJ/JTjLWwnVpa2n6sYQhRZ627qRTFsdOENJI7OUG23EkHll7VCIx3+DRWATRlkzckpz6zgNPclRJs3lTP47Ehvm13OiOJ5HwditfrjC5LmFITzqVJyVbCkQOPeaasehzlXgT1WOxNzHET6yeo+1MzOqujqz5UtveBC5Uq3dEO0+Z6E5kIzcSe7H4iLY7gSSzelS3ic5o+5NdKhitTLehLjkMGaosyag5WM5M9CfWzbrmoPJQ5H3rRBFzOT0agKDHartI1s0uN2J/wsOX8pmDkTKcSlOn9ScmMONevPDUzbyJVQk0Bu90qR9P8vw/eIg/eQGpCd2saZ+EZAAAAABJRU5ErkJggg==" />
</form>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
$(function(){
	$('#submitBtn').on('click', function(){
		
		data = {
			url : $('#url').val()
		};
		
		$.ajax({
	        url: '/qr1/getQRcode',
	        type: 'post',
	        data: data, // 실제 사용하려는 URL로 변경
	        success: function (data) {
	        	console.log('성공');
	        	console.log(data.base64Encoded);
	        	
	        	$('#qrCodeImage').attr('src', 'data:image/png;base64,' + data.base64Encoded);
	        	
	        },
	        error: function (error) {
	            console.error('실패');
	            console.log(error);
	        }
	    });
		
	});
	
	$('#btnNaver').on('click',function(){
		$('input').val('https://www.naver.com');
		$('form').submit();
	});
});
</script>
</body>
</html>