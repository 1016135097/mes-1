<%@ page language="java" import="java.util.*,com.pelloz.po.UserInfo" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
	UserInfo user = (UserInfo)session.getAttribute("user");
	if(user==null){
		out.print("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body><h1>请先登录</h1></body>");
		return;
	}
	System.out.println(user.getId()+user.getUsername()+user.getDepartment());
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>工艺文件</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <link href="css/crudpage.css" rel="stylesheet" type="text/css"></head>
  
  <body bgcolor="#D4E0F2">
  <div class="msg">
  ${sessionScope.user.getUsername()}<br/>
  </div>
  添加工艺文件<br/>
  <form action="processdoc.do" method="get">
  	<input name="method" type="hidden" value="add">
  	<table width="800" border="0">
  <tr>
    <th width="30%" height="25" align="center" valign="middle" nowrap="nowrap" scope="col" >文件名称</th>
    <th align="center" valign="middle" nowrap="nowrap" scope="col" >文件内容</th>
  </tr>
  <tr>
    <td height="25" align="center" valign="middle" nowrap="nowrap"><input name="title" type="text" size="20"></td>
    <td align="center" valign="middle" nowrap="nowrap"><input name="content" type="text" size="50" ></td>
  </tr>
</table><br/>
<input name="submit" type="submit" value="添加">
  </form>
    <hr/>
    可以通过以下条件搜索，请至少填写一栏<br/>
    <form action="processdoc.do" method="get">
    <input name="method" type="hidden" value="search">
    <table width="500" border="0">
  <tr>
    <th width="20%" height="25" align="center" valign="middle" nowrap="nowrap" scope="col" >文件ID</th>
    <th width="40%" align="center" valign="middle" nowrap="nowrap" scope="col" >文件名称</th>
    <th align="center" valign="middle" scope="col" >文件作者</th>
  </tr>
  <tr>
    <td width="20%" height="25" align="center" valign="middle" nowrap="nowrap"><input name="processdocid" type="text" size="8" ></td>
    <td width="40%" align="center" valign="middle" nowrap="nowrap"><input name="processdocname" type="text" size="25" ></td>
    <td align="center" valign="middle"><input name="processdocauthor" type="text" size="25" ></td>
  </tr>
</table><br/>
<input name="submit" type="submit" value="搜索">
    </form>
    
  <hr/>
  默认显示本账号下10本工艺文件<br/>
  <form action="processdoc.do" method="get">
  <input name="method" type="hidden" value="delete">
	<label>文件ID----文件名称----文件内容----文件作者</label><br>
  <select name="processlist" size="10" >
    <option value="1" selected="selected">#1----ERT型-T01-前传动轴----车-钻-倒角(示例工艺简化处理)----张一</option>
    <option value="2">#2----ERT型-T01-前传动轴----车-钻-倒角(示例工艺简化处理)----张一</option>
  </select><br/>
  <input name="modify" type="button" value="修改选中项">&nbsp;&nbsp;
  <input name="submit" type="submit" value="删除选中项">
  </form>
  <hr/>
  <form action="processdoc.do" method="get">
  <input name="method" type="hidden" value="modify">
  <table width="800" border="0">
  <tr>
    <th width="10%" height="25" align="center" valign="middle" nowrap="nowrap" scope="col" >文件ID</th>
    <th width="30%" align="center" valign="middle" nowrap="nowrap" scope="col" >文件名称</th>
    <th align="center" valign="middle" scope="col" >文件内容</th>
  </tr>
  <tr>
    <td width="10%" height="25" align="center" valign="middle" nowrap="nowrap"><input name="processdocid" type="text" disabled="disabled" size="8" readonly="readonly" ></td>
    <td width="30%" align="center" valign="middle" nowrap="nowrap"><input name="processdocname" type="text" size="20" ></td>
    <td align="center" valign="middle"><input name="processdocauthor" type="text" size="50" ></td>
  </tr>
</table><br/>
<input name="submit" type="submit" value="提交修改">
  </form>
  </body>
</html>
