<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<title>2차인증 페이지</title>
</head>
<body>
<p>2차인증(아이디, 비밀번호를 입력했다는 1차 인증이 끝났다는 가정)</p>
<p>해당 QR코드를 google Authentication 어플에서 스캔 후 6자리 otp번호를 입력하세요</p>
<img src="${url}"/>
<form action="/qr2/login" method="post">
	<!-- 
	지금은 controller에서 생성된 secretKey를 가져와 hidden으로 다시 controller에 넘겨주지만 
	나중에 실제로 구현할 때는 db에서 secretKey만 가져오면 된다
	-->
	<input type="hidden" name="secretKey" value="${secretKey}">
	<input type="number" name="userEnteredKey">
	<button type="submit">제출</button>
</form>
</body>
</html>