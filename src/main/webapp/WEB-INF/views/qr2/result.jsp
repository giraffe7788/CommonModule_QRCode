<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<title>로그인결과</title>
</head>
<body>
<p>
	<c:if test="${isSuccess == 'true'}">로그인성공</c:if>
	<c:if test="${isSuccess == 'false'}">로그인실패</c:if>
</p>
</body>
</html>