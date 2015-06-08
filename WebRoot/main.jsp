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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
	<head>
		<base href="<%=basePath%>" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<meta name="description" content="http://www.cnblogs.com/pelloz/"/>
		<title>MES系统面板</title>
		<link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="css/clear.css" />
		<link rel="stylesheet" type="text/css" href="css/pelloz.css" />
<style type="text/css">
/* 防止chrome下日期选择的bug */
.x-date-picker{
border: 1px solid;
border-top:0 none;
position:relative;
width: 185px;
}
/* 防止日期选择按钮遮挡文字 */
.x-form-field{
padding-left: 25px;
}
</style>
		<!-- GC -->
		<!-- LIBS -->
		<script type="text/javascript" src="ext/adapter/ext/ext-base.js"></script>
		<!-- ENDLIBS -->
		<script type="text/javascript" src="ext/ext-all.js"></script>

		<script type="text/javascript" src="ext/ext-lang-zh_CN.js"></script>
		
		<script type="text/javascript" src="js/pelloz.js"></script>
		<!-- 通过JSP脚本动态指定需要加载的js文件 -->
<%
		out.println("<script type='text/javascript' src='js/messupport.js'></script>");
		out.println("<script type='text/javascript' src='js/bom.js'></script>");
		out.println("<script type='text/javascript' src='js/processdoc.js'></script>");
		out.println("<script type='text/javascript' src='js/tooling.js'></script>");
		out.println("<script type='text/javascript' src='js/plan.js'></script>");
		out.println("<script type='text/javascript' src='js/manufacture.js'></script>");
		out.println("<script type='text/javascript' src='js/order.js'></script>");
		out.println("<script type='text/javascript' src='js/usermgr.js'></script>");
%>
<script type="text/javascript">

	Ext.onReady( function() {

		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';

		var addPanel = function(btn, event) {
			var n;
			n = tabPanel.getComponent(btn.id);
			if(n) {
				n.show();
				tabPanel.setActiveTab(n);
				return;
			}
			switch(btn.id)
			{
			case 'processdoc': processdoc(tabPanel,btn);
				break;
			case 'productplan':plan(tabPanel,btn);
				break;
			case 'manufacture':manufacture(tabPanel,btn);
				break;
			case 'tooling':tooling(tabPanel,btn);
				break;
			case 'inventory':inventory(tabPanel,btn);
				break;
			case 'buyingorder':buyingorder(tabPanel,btn);
				break;
			case 'usermgr': usermgr(tabPanel,btn);
				break;
			default:;
				break;
			}
		}
		
		var msgalert = function(btn, event) {
			
			Ext.Msg.show({
				   title:'退出系统',
				   msg: '确认退出系统？',
				   buttons: Ext.Msg.YESNO,
				   fn: processResult,
				   icon: Ext.MessageBox.QUESTION
				});
		}
		
		function processResult(btn) {
			if (btn=="yes")
				{
					window.parent.location="index.jsp";
				}
        }
<% 
//根据department决定操作面板的显示
//TODO 将一下显示部分包装为方法，从文件读取，使用单例模式读取
String department = user.getDepartment(); 
List<String> items = new ArrayList<String>();
if(department.equals("admin")||department.equals("工艺室")){
	items.add("item1");
%>
	var item1 = new Ext.Panel( {
		title : '工艺室',
		cls : 'empty',
		items : [ 
			new Ext.Button({
				id : 'processdoc',
				text : '工艺文件',
				width : '100%',
				listeners : {
					click : addPanel
				}

			}),
			
			new Ext.Button({
				id : 'tooling',
				text : '工装管理',
				width : '100%',
				listeners : {
					click : addPanel
				}
			})

			]
	});
<%
}
%>

<%
if(department.equals("admin")||department.equals("车间生产")){
	items.add("item2");
%>
var item2 = new Ext.Panel( {
	title : '车间生产',
	cls : 'empty',
	items : [ 
		new Ext.Button({
			id : 'manufacture',
			text : '安排生产',
			width : '100%',
			listeners : {
				click : addPanel
			}

		}),
		
		new Ext.Button({
			id : 'processdoc',
			text : '工艺文件',
			width : '100%',
			listeners : {
				click : addPanel
			}

		})

		]
});
<%
}
%>

<%
if(department.equals("admin")||department.equals("工装室")){
	items.add("item3");
%>
var item3 = new Ext.Panel( {
	title : '工装室',
	cls : 'empty',
	items : [ 
		new Ext.Button({
			id : 'tooling',
			text : '工装管理',
			width : '100%',
			listeners : {
				click : addPanel
			}
		})
		]
});
<%
}
%>

<%
if(department.equals("admin")||department.equals("计划室")){
	items.add("item4");
%>
var item4 = new Ext.Panel( {
	title : '计划室',
	cls : 'empty',
	items : [ 
		new Ext.Button({
			id : 'productplan',
			text : '生产计划',
			width : '100%',
			listeners : {
				click : addPanel
			}

		}),
		
		new Ext.Button({
			id : 'processdoc',
			text : '工艺文件',
			width : '100%',
			listeners : {
				click : addPanel
			}

		}),
		
		new Ext.Button({
			id : 'tooling',
			text : '工装管理',
			width : '100%',
			listeners : {
				click : addPanel
			}
		})

		]
});
<%
}
%>

<%
if(department.equals("admin")||department.equals("采购部")){
	items.add("item5");
%>
var item5 = new Ext.Panel( {
	title : '采购部',
	cls : 'empty',
	items : [ 
		new Ext.Button({
			id : 'buyingorder',
			text : '采购下单',
			width : '100%',
			listeners : {
				click : addPanel
			}

		}),
		
		new Ext.Button({
			id : 'tooling',
			text : '工装管理',
			width : '100%',
			listeners : {
				click : addPanel
			}
		})
		]
});
<%
}
%>

		var item6 = new Ext.Panel( {
			title : '系统管理',
			cls : 'empty',
			items : [ 
<%
if(department.equals("admin")){
%>
				new Ext.Button({
					id : 'usermgr',
					text : '用户管理',
					width : '100%',
					listeners : {
						click : addPanel
					}

				}),
<%
}
%>
				new Ext.Button({
					id : 'quit',
					text : '退出系统',
					width : '100%',
					listeners : {
						click : msgalert
					}

				})

				]
		});

		var accordion = new Ext.Panel( {
			region : 'west',
			margins : '5 5 5 5',
			split : true,
			width : 210,
			layout : 'accordion',
			items : [ <% for(int i=0;i<items.size();i++){out.print(items.get(i)+",");} out.print("item6"); %>]
		});

		var tabPanel = new Ext.TabPanel( {
			id : 'tabPanel',
			region : 'center',
			enableTabScroll : true,
			closable : true,
			deferredRender : false,
			autoDestroy: false,
			activeTab : 0,
			items : [ {
				title : '首页',
				autoScroll : true,
				autoLoad : 'webpage/mainpage.html'
			} ]
		});
		
		var topPanel = new Ext.Panel({
			
			    title: '您好 ${user.fullname}',
			    region: 'north',
			    height: 150,
			    minSize: 75,
			    maxSize: 250,
			    cmargins: '5 0 0 0',
			    items : [ {
					autoLoad : {url: 'head.jsp', scripts: true}
				} ]
			
		});

		var viewport = new Ext.Viewport( {
			renderTo: 'mainbody',
			layout : 'border',
			width : 1200,
			items : [ topPanel,accordion, tabPanel ]			
		});

	});
</script>
	</head>
	<body>
		<div id="mainbody" style="width:1200px;height:100%;overflow:auto;"></div>
	</body>
</html>
