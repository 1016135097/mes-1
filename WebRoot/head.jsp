<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
	<head>
		<base href="%3C%=basePath%%3E" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="description" content="http://www.cnblogs.com/pelloz/" />
		<title>
			MES系统演示
		</title>
        <link href="css/clear.css" rel="stylesheet" type="text/css" />
		<link href="css/pelloz.css" rel="stylesheet" type="text/css" />
		<!-- <script src="js/pelloz.js"></script> -->
	</head>
	<body>
		<div style="width: 100%; height: 100px; background-color: #D9E7F8;">
			<span style="margin-top: 35px; float: right; font-weight: bolder; font-size: 35px;">欢迎使用MES系统&nbsp;&nbsp;</span>
		</div>
		
		<div class="msgbar">
			<div style="float: left; padding: 0px; margin: 0px; width: 500px;">
				&nbsp;账号：${user.username}&nbsp;&nbsp;部门：${user.department}&nbsp;&nbsp;职称：${user.title}&nbsp;&nbsp;
		  </div>
			<div style="text-align: right; width: 300px; float: right;">
				现在时间&nbsp;<span id="localtime"></span>
				<script>tick('localtime');</script>
			</div>
		</div>
	</body>
</html>