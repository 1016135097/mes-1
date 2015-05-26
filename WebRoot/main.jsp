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
		<!-- GC -->
		<!-- LIBS -->
		<script type="text/javascript" src="ext/adapter/ext/ext-base.js"></script>
		<!-- ENDLIBS -->
		<script type="text/javascript" src="ext/ext-all.js"></script>

		<script type="text/javascript" src="ext/ext-lang-zh_CN.js"></script>

		<script type="text/javascript" src="js/pelloz.js"></script>

<style type="text/css">
	html,body {
		font: normal 12px verdana;
		margin: 0;
		padding: 0;
		border: 0 none;
		overflow: hidden;
		height: 100%;
	}
	
	.empty .x-panel-body {
		padding-top: 0;
		text-align: center;
		font-style: italic;
		color: gray;
		font-size: 11px;
	}
	
	.x-btn button {
		font-size: 14px;
	}
	
	.x-panel-header {
		font-size: 14px;
	}
</style>

<script type="text/javascript">
	Ext.onReady( function() {

		var addPanel = function(btn, event) {
			var n;
			n = tabPanel.getComponent(btn.id);
			if(n) {
				tabPanel.setActiveTab(n);
				return;
			}
			n = tabPanel.add( {
				id : btn.id,
				title : btn.text,
				//html : '<iframe width=100% height=100% src="webpage/' + btn.id + '.jsp" />',
				autoLoad : 'webpage/' + btn.id + '.jsp',
				autoScroll : true,
				closable : 'true'
			});
			
			tabPanel.setActiveTab(n);
			//Ext.Msg.alert('ext',' "webpage/' + btn.id + '" ');
		}
		
		var msgalert = function(btn, event) {
			
			Ext.Msg.show({
				   title:'退出系统',
				   msg: '确认退出系统？',
				   buttons: Ext.Msg.YESNO,
				   fn: processResult,
				   //animEl: 'elId',//动画效果
				   icon: Ext.MessageBox.QUESTION
				});
		}
		
		function processResult(btn) {
            //Ext.Msg.alert('结果', btn);
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
				id : 'bom',
				text : '物料清单',
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
			id : 'productplan',
			text : '生产计划',
			width : '100%',
			listeners : {
				click : addPanel
			}

		}),

		new Ext.Button({
			id : 'productarrange',
			text : '安排生产',
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
			id : 'fixturemanage',
			text : '工装管理',
			width : '100%',
			listeners : {
				click : addPanel
			}
		}),
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
			id : 'inventory',
			text : '库存',
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
			id : 'buyingrequire',
			text : '采购需求',
			width : '100%',
			listeners : {
				click : addPanel
			}

		}),
		]
});
<%
}
%>

		var item6 = new Ext.Panel( {
			title : '系统管理',
			cls : 'empty',
			items : [ 
				new Ext.Button({
					id : 'system',
					text : '系统管理',
					width : '100%',
					listeners : {
						click : addPanel
					}

				}),

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
			margins : '5 0 5 5',
			split : true,
			width : 210,
			layout : 'accordion',
			items : [ <% for(int i=0;i<items.size();i++){out.print(items.get(i)+",");} out.print("item6"); %>]
		});

		var tabPanel = new Ext.TabPanel( {
			region : 'center',
			enableTabScroll : true,
			deferredRender : false,
			activeTab : 0,
			items : [ {
				title : '首页',
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
			    scripts: true,
			    items : [ {
					//title : 'top',
					//autoLoad : 'head.jsp'
					autoLoad : {url: 'head.jsp', scripts: true}
				} ]
			
		})

		var viewport = new Ext.Viewport( {
			layout : 'border',
			items : [ topPanel,accordion, tabPanel ]
		});

	});
</script>
	</head>
	<body>
		
	</body>
</html>
