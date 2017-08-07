<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>

</head>
<body>
	<shiro:hasPermission name="courier:add">
		<a href="#">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="courier:edit">
		<a href="#">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="courier:list">
		<a href="#">查询</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="courier:delete">
		<a href="#">删除</a>
	</shiro:hasPermission>
</body>
</html>