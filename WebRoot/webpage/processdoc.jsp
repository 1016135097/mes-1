<%@ page language="java" import="java.util.*,com.pelloz.po.UserInfo" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
	UserInfo user = (UserInfo)session.getAttribute("user");
	/*if(user==null){
		out.print("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body><h1>请先登录</h1></body>");
		return;
	}*/
	//System.out.println(user.getId()+user.getUsername()+user.getDepartment());
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
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link href="../css/clear.css" rel="stylesheet" type="text/css">
  	<link href="../css/pelloz.css" rel="stylesheet" type="text/css">
    <link href="../css/crudpage.css" rel="stylesheet" type="text/css">
    
    <script type="text/javascript">
    Ext.onReady(function(){

        var simple = new Ext.FormPanel({
            labelWidth: 75, // label settings here cascade unless overridden
            url:'save-form.php',
            frame:true,
            title: 'Simple Form',
            bodyStyle:'padding:5px 5px 0',
            width: 350,
            defaults: {width: 230},
            defaultType: 'textfield',
            
            region: 'north',

            items: [{
                    fieldLabel: 'First Name',
                    name: 'first',
                    allowBlank:false
                },{
                    fieldLabel: 'Last Name',
                    name: 'last'
                },{
                    fieldLabel: 'Company',
                    name: 'company'
                }, {
                    fieldLabel: 'Email',
                    name: 'email',
                    vtype:'email'
                }, new Ext.form.TimeField({
                    fieldLabel: 'Time',
                    name: 'time',
                    minValue: '8:00am',
                    maxValue: '6:00pm'
                })
            ],

            buttons: [{
                text: 'Save'
            },{
                text: 'Cancel'
            }]
        });

        var viewport1 = new Ext.Viewport( {
			layout : 'border',
			items : [ simple ]
		});
        
        
    })
    </script>
  </head>
  
  <body>
  </body>
</html>
