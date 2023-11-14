<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<title>OTP QR</title>
</head>
<body>
<h1>2차 인증을 위한 QR</h1>
<p>1. Google Authenticator 어플을 다운로드 한 뒤 QR코드를 찍는다</p>
<p>2. 2차인증시 OTP번호 6자리를 입력한다</p>
<p>secretKey : ${secretKey}</p>
<p>QR코드 : <img src='${QrUrl}'></p>
<button onclick="location.href='/qr/FirstLoginPage'">로그인하러가기</button>
</body>
</html>