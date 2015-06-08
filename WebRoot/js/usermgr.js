//user中使用的全局变量
var userXML;
var urlGetXmlUser = "user.do?method=getuserxmlstr";
var urlUser = "user.do";
// 用于TabPanel的添加
function usermgr(tabPanel, btn) {

	var n = tabPanel.add({
		id : btn.id,
		title : btn.text,
		layout : 'fit',
		items : [ gridFormUser ],

		autoScroll : true,
		closable : true
	});

	tabPanel.setActiveTab(n);

	// 更新userData,需要提前引用pelloz.js，服务器返回的是字符串形式的xml
	ajaxGetText(urlGetXmlUser, bindUserXML);

}

function bindUserXML(xmlstr) {
	userXML = loadXMLString(xmlstr);
	dataStoreUser.loadData(userXML);
}

var userdepartment = [ [ 1, 'admin' ], [ 2, '工艺室' ], [ 3, '车间生产' ], [ 4, '工装室' ], [ 5, '计划室' ], [ 6, '采购部' ] ]
var usertitle = [ [ 1, 'admin' ], [ 2, '工艺工程师' ], [ 3, '生产管理员' ], [ 4, '工装工程师' ], [ 5, '计划员' ], [ 6, '采购工程师' ] ]

// data bind
var users = Ext.data.Record.create([ {
	name : 'id',
	type : 'int'
}, {
	name : 'username'
}, {
	name : 'fullname'
}, {
	name : 'department'
}, {
	name : 'title'
} ]);

var dataStoreUser = new Ext.data.Store({
	sortInfo : {
		field : "id",
		direction : "ASC"
	},
	reader : new Ext.data.XmlReader({
		record : 'user'// The repeated element which contains row information
	}, users)
});

// 主界面
var colModelUser = new Ext.grid.ColumnModel([ {
	id : 'userid',// 用于和表格的数据绑定
	header : "编号",
	width : 50,
	sortable : true,
	locked : false,
	dataIndex : 'id'// 用于和Ext.data.Record的元素name相对应
}, {
	id : 'username',
	header : "用户名",
	width : 100,
	sortable : true,
	dataIndex : 'username'
}, {
	id : 'fullname',
	header : "姓名",
	width : 100,
	sortable : true,
	dataIndex : 'fullname'
}, {
	id : 'userdepartment',
	header : "部门",
	width : 100,
	sortable : true,
	dataIndex : 'department'
}, {
	id : 'usertitle',
	header : "职称",
	width : 100,
	sortable : true,
	dataIndex : 'title'
} ]);

var gridFormUser = new Ext.FormPanel({
	id : 'user-form',
	frame : true,
	labelAlign : 'left',
	bodyStyle : 'padding:5px',
	layout : 'column', // Specifies that the items will now be arranged in columns
	items : [ {
		id : 'usercolumn',
		columnWidth : 1,
		layout : 'fit',
		xtype : 'grid',
		ds : dataStoreUser,
		cm : colModelUser,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				rowselect : function(sm, row, rec) {
					Ext.getCmp("user-form").getForm().loadRecord(rec);
				}
			}
		}),
		autoExpandColumn : 'username',
		height : 350,
		title : '用户列表',
		border : true,
		listeners : {
			render : function(g) {
				g.getSelectionModel().selectRow(0);
			},
			delay : 300
		}

	}, {
		frame : true,
		width : 400,
		// columnWidth : 0.35,
		xtype : 'fieldset',
		labelWidth : 60,
		title : '&nbsp;用户详情',
		defaults : {
			width : 280,
			border : false
		}, // Default config options for child items
		defaultType : 'textfield',
		autoHeight : true,
		bodyStyle : 'padding:5px',
		border : false,
		style : {
			"margin-left" : "20px", // when you add custom margin in IE 6...
			"margin-right" : Ext.isIE6 ? (Ext.isStrict ? "-10px" : "-13px") : "0" // you have to adjust for it somewhere else
		},
		items : [ {
			fieldLabel : '编号',
			id : 'userid',
			name : 'id',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'
		}, {
			fieldLabel : '用户名',
			id : 'username',
			name : 'username'
		}, {
			inputType : 'password',
			fieldLabel : '密码',
			disabled : true,
			id : 'password',
			name : 'password'
		}, {
			fieldLabel : '姓名',
			id : 'fullname',
			name : 'fullname'
		}, {
			xtype : 'combo',
			fieldLabel : '部门',
			id : 'userdepartment',
			hiddenName : 'department',// 提交时的 combo Name
			editable : false,
			allowBlank: true,
			emptyText : '请选择',
			triggerAction : 'all',
			store : new Ext.data.ArrayStore({
				id : 0,
				fields : [ 'myId', 'displayText' ],
				data : userdepartment
			}),
			mode : 'local',
			valueField : 'displayText',// 选项的 value 值，提交时传递的该值
			displayField : 'displayText' // 选项的显示值
		}, {
			xtype : 'combo',
			fieldLabel : '职称',
			id : 'usertitle',
			hiddenName : 'title',// 提交时的 combo Name
			editable : false,
			allowBlank: true,
			emptyText : '请选择',
			triggerAction : 'all',
			store : new Ext.data.ArrayStore({
				id : 0,
				fields : [ 'myId', 'displayText' ],
				data : usertitle
			}),
			mode : 'local',
			valueField : 'displayText',// 选项的 value 值，提交时传递的该值
			displayField : 'displayText' // 选项的显示值
		}, {
			xtype : 'radiogroup',
			style : 'padding-left: 0px',
			id : 'usermethod',
			fieldLabel : '操作',
			columns : 3,
			allowBlank : false,
			items : [ {
				boxLabel : '添加',
				name : 'method',
				inputValue : 'add'
			}, {
				boxLabel : '更改',
				name : 'method',
				inputValue : 'modify'
			}, {
				boxLabel : '删除',
				name : 'method',
				inputValue : 'delete'
			}, {
				boxLabel : '搜索',
				name : 'method',
				inputValue : 'find',
				checked : true
			} ],
			listeners : {
				change : function(radiogroup, checkedradio) {

					var textfieldid = Ext.getCmp("user-form").getForm().findField('id');
					var textfieldusername = Ext.getCmp("user-form").getForm().findField('username');
					var textfieldpassword = Ext.getCmp("user-form").getForm().findField('password');
					var textfieldfullname = Ext.getCmp("user-form").getForm().findField('fullname');
					var textfielddepartment = Ext.getCmp("user-form").getForm().findField('department');
					var textfieldtitle = Ext.getCmp("user-form").getForm().findField('title');

					switch (checkedradio.inputValue) {
					case 'add':
						textfieldid.reset();
						textfieldid.disable();
						textfieldpassword.enable();
						textfieldpassword.reset();
						textfieldusername.enable();
						textfieldfullname.enable();
						textfielddepartment.enable();
						textfieldtitle.enable();
						break;
					case 'modify':
						textfieldid.enable();
						textfieldpassword.enable();
						textfieldpassword.reset();
						textfieldusername.enable();
						textfieldfullname.enable();
						textfielddepartment.enable();
						textfieldtitle.enable();
						break;
					case 'delete':
						textfieldid.reset();
						textfieldid.enable();
						textfieldpassword.disable();
						textfieldpassword.reset();
						textfieldusername.disable();
						textfieldfullname.disable();
						textfielddepartment.disable();
						textfieldtitle.disable();
						break;
					case 'find':
						textfieldid.enable();
						textfieldpassword.disable();
						textfieldpassword.reset();
						textfieldusername.enable();
						textfieldfullname.enable();
						textfielddepartment.enable();
						textfieldtitle.enable();
						break;
					default:
						;
						break;
					}

				}
			}
		} ],

		buttonAlign : 'left',

		buttons : [ {
			text : '提交',
			style : 'margin-left:60px',
			handler : submitUser
		}, {
			text : '清空',
			handler : resetUser
		}, {
			text : '刷新列表',
			handler : refreshUser
		} ]
	} ]
});

function submitUser() {

	if (!gridFormUser.getForm().isValid())
		return;

	var textfieldid = Ext.getCmp("user-form").getForm().findField('id').getValue();
	var textfieldusername = Ext.getCmp("user-form").getForm().findField('username').getValue();
	var textfieldpassword = Ext.getCmp("user-form").getForm().findField('password').getValue();
	var textfieldfullname = Ext.getCmp("user-form").getForm().findField('fullname').getValue();
	var textfielddepartment = Ext.getCmp("user-form").getForm().findField('department').getValue();
	var textfieldtitle = Ext.getCmp("user-form").getForm().findField('title').getValue();

	var method = gridFormUser.getForm().findField('usermethod').getValue().inputValue;

	switch (method) {
	case 'add':
		if (textfieldusername == '' || textfieldfullname == '' || textfielddepartment == '' || textfieldtitle == ''
				|| textfieldpassword == '') {
			Ext.Msg.alert('提示', '添加用户必须填写用户名、密码、姓名、部门、职称');
			return;
		}
		break;
	case 'modify':
		if (textfieldid == '' || textfieldusername == '' || textfieldfullname == '' || textfielddepartment == ''
				|| textfieldtitle == '' || textfieldpassword == '') {
			Ext.Msg.alert('提示', '更改用户时必须填写用户编号、用户名、密码、姓名、部门、职称');
			return;
		}
		break;
	case 'delete':
		if (textfieldid == '') {
			Ext.Msg.alert('提示', '删除用户必须指定编号');
			return;
		}
		break;
	case 'find':
		if (textfieldid == '' && textfieldusername == '' && textfieldfullname == '' && textfielddepartment == ''
				&& textfieldtitle == '') {
			Ext.Msg.alert('提示', '搜索用户时请至少指定一项条件');
			return;
		}

		url = urlUser + "?method=find&id=" + textfieldid + "&username=" + textfieldusername + "&fullname="
				+ textfieldfullname + "&department=" + textfielddepartment + "&title=" + textfieldtitle;
		ajaxGetText(url, bindUserXML);
		return;

		break;
	default:
		Ext.Msg.alert('提示', '系统错误，请完整加载页面再操作');
		return;
		break;
	}

	gridFormUser.getForm().submit({
		waitMsg : '正在提交数据，超时不动请刷新页面，或者重新登录',
		waitTitle : '提示',
		url : urlUser,
		method : 'POST',
		success : function(form, action) {
			try {
				Ext.Msg.alert('提示', '保存成功');
				ajaxGetText(urlGetXmlUser, bindUserXML);
			} catch (e) {
				Ext.Msg.alert('提示', '服务器返回数据错误或者权限不足');
			}
		},
		failure : function(form, action) {
			try {
				Ext.Msg.alert('错误提示', '原因：' + action.result.infos.info);
			} catch (e) {
				Ext.Msg.alert('错误提示', '原因：无正确数据返回');
			}
		}
	});
}

function resetUser() {
	gridFormUser.getForm().reset();
}

function refreshUser() {
	ajaxGetText(urlGetXmlUser, bindUserXML);
}
