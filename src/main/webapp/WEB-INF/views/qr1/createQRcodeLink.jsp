<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<title>QR코드 링크 생성기</title>
</head>
<body>
<form action="/qr1/getQRcode" method="post">
	<input type="text" name="url" id="url" placeholder="접속하고싶은 url">
	<br><br>
	<button type="button" id="submitBtn">제출</button>
	<br><br>
	<button type="button" id="btnNaver">네이버 링크 QR코드 생성</button>
</form>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
$(function(){
	$('#submitBtn').on('click', function(){
		
		data = {
			url : $('#url').val()
		};
		
		$.ajax({
			url : '/qr1/getQRcode',
			type : 'post',
			data : data,
			dataType : 'json',
			success : function(rst){
				console.log(rst);
			},
			error : function(rst){
				console.log('통신 실패');
				console.log(rst);
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