<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<title>로그인</title>
</head>
<body>
<form action="/qr/secondLoginPage" method="post">
<p>실험용 아이디/비번 : admin/123</p>
<input type="text" name="id" placeholder="아이디">
<input type="text" name="pw" placeholder="비번">
<button type="submit">제출</button>
</form>

<script>
if('${result}' != '') {
	alert('${result}');
}
</script>
</body>
</html>