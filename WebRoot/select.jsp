<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'main.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  Add User
    <form action="user.do" method="get">
    <input name="method" type="hidden" value="add" />
    username:<input name="username" type="text" value="admin" size="25" maxlength="23"/>
    <input type="submit" value="submit" >
    </form> 
    <br />
    Delete User
    <form action="user.do" method="get">
    <input name="method" type="hidden" value="deletebyid" />
    id:<input name="id" type="text"  size="25" maxlength="23"/>
    <input type="submit" value="submit" >
    </form> <br />
    Modify User
    <form action="user.do" method="get">
    <input name="method" type="hidden" value="modify" />
    id:<input name="id" type="text"  size="25" maxlength="23"/>
    <input type="submit" value="submit" >
    </form> <br />
    Find User By
    <form action="user.do" method="get">
    <select name="by">
      <option value="id" selected="selected">ID</option>
      <option value="username">UserName</option>
      <option value="fullname">FullName</option>
      <option value="department">Department</option>
      <option value="title">Title</option>
    </select>
    <input name="method" type="hidden" value="find" />
    param:<input name="param" type="text"  size="25" maxlength="23"/>
    <input type="submit" value="submit" >
    </form> <br />
    
    
    ${ requestScope.user }
    <c:forEach var="user" items="${ requestScope.users }" >
    	${ user }<br/>
    </c:forEach>
  </body>
</html>
