<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	//打开首页清除session，必须重新登录
	session.invalidate();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="description" content="http://www.cnblogs.com/pelloz/" />
<title>MES系统演示</title>
<link href="css/index.css" rel="stylesheet" type="text/css" />
<style type="text/css">
body {
	background-color: #D4E0F2;
}
</style>
<script type="text/javascript" src="js/pelloz.js"></script>
</head>
<body>
	<div class="leftbanner">

		<div class="email" onclick="copyText('pellozhang@163.com','insert')">
			pellozhang@163.com<br /> <a href="file/Java工程师-张鹏.pdf" title="PDF简历"
				target="_blank">PDF简历</a><br /> <span id="insert"> </span>
		</div>
	</div>
	<div class="right">
		<div id="rightup">
			<br /> <br /> <span class="bold">欢迎试用本MES系统<br /> Created
				By 张鹏
			</span><br /> <br /> 管理员账号:admin<br /> 管理员密码:admin<br /> 其他角色账号密码见输入框下面<br />
		</div>
		<div id="right">

			<form action="user.do" method="post">
				<input type="hidden" name="method" value="login" />
				<table width="200" border="0">
					<tr>
						<td height="25" nowrap="nowrap">账号:</td>
						<td height="25"><input name="username" type="text"
							value="admin" size="25" maxlength="23" /></td>
					</tr>
					<tr>
						<td height="25" nowrap="nowrap">密码:</td>
						<td height="25"><input id="password" name="password"
							type="password" size="25" maxlength="23" /></td>
					</tr>
					<tr>
						<td nowrap="nowrap"></td>
						<td style="color:red;">
							<%
								String msg = request.getParameter("loginmsg");
								if (msg != null) {
									switch (msg) {
										case "-1" :
											out.print("密码不正确");
											break;
										case "-2" :
											out.print("账号不存在");
											break;
										default :
											break;
									}
								}
							%>
						</td>
					</tr>
					<tr>
						<td height="30" valign="bottom" nowrap="nowrap">&nbsp;</td>
						<td height="30" valign="bottom"><input type="submit"
							value="登录" /></td>
					</tr>
				</table>
			</form>
			<Script language="JavaScript">
				document.getElementById("password").focus();
			</script>
		</div>
		<div id="rightdown">
			<span class="bold"><a href="img/messystemlflow.png"
				target="_blank">本MES系统流程图与角色定义</a></span><br /> <br />
			<table width="255" border="0" cellpadding="2" cellspacing="2">
				<tr>
					<th scope="col">角色</th>
					<th align="left" scope="col">账号</th>
					<th align="left" scope="col">密码</th>
				</tr>
				<tr>
					<th scope="row">工艺</th>
					<td align="left">process</td>
					<td align="left">process</td>
				</tr>
				<tr>
					<th scope="row">生产</th>
					<td align="left">produce</td>
					<td align="left">produce</td>
				</tr>
				<tr>
					<th scope="row">工装</th>
					<td align="left">fixture</td>
					<td align="left">fixture</td>
				</tr>
				<tr>
					<th scope="row">计划</th>
					<td align="left">plan</td>
					<td align="left">plan</td>
				</tr>
				<tr>
					<th scope="row">采购</th>
					<td align="left">buyer</td>
					<td align="left">buyer</td>
				</tr>
			</table>
			<p>
				<br />
			</p>
		</div>
	</div>
</body>
<script type="text/javascript" src="ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="ext/ext-all.js"></script>
</html>