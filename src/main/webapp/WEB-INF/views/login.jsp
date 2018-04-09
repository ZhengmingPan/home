<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${ctx }/login" method="post">
		<img alt="" src="${ctx }/static/images/avatar6.jpg"> 姓名：<input type="text" name="username" /> 密码：<input type="password" name="password" />
		<button type="submit">登录</button>
	</form>
	<div></div>
</body>
<script type="text/javascript"> 
</script>
</html>