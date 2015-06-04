//可以全局替换的变量名 
//toolingXML tooling gettoolingxmlstr bindToolingXML dataStoreTooling gridFormTooling
//submitTooling resetTooling refreshTooling toolings tooling colModelTooling tooling-form

//tooling中使用的全局变量
var toolingXML;
var urlGetXmlTooling = "tooling.do?method=gettoolingxmlstr";
var urlTooling = "tooling.do";
// 用于TabPanel的添加
function tooling(tabPanel, btn) {

	var n = tabPanel.add({
		id : btn.id,
		title : btn.text,
		layout : 'fit',
		items : [ gridFormTooling ],

		autoScroll : true,
		closable : true
	});

	tabPanel.setActiveTab(n);

	ajaxGetText(urlGetXmlTooling, bindToolingXML);
}

function bindToolingXML(xmlstr) {
	toolingXML = loadXMLString(xmlstr);
	dataStoreTooling.loadData(toolingXML);
}

// data bind
var toolings = Ext.data.Record.create([ {
	name : 'id',
	type : 'int'
}, {
	name : 'name'
}, {
	name : 'amount',
	type : 'int'
} ]);

var dataStoreTooling = new Ext.data.Store({
	reader : new Ext.data.XmlReader({
		record : "tooling",
	}, toolings)
});

// 主界面
var colModelTooling = new Ext.grid.ColumnModel([ {
	id : 'toolingid',// 用于和表格的数据绑定
	header : "编号",
	width : 50,
	sortable : true,
	locked : false,
	dataIndex : 'id'// 用于和Ext.data.Record的元素name相对应
}, {
	id : 'toolingname',
	header : "工装名称",
	width : 300,
	sortable : true,
	dataIndex : 'name'
}, {
	id : 'toolingamount',
	header : "总量",
	width : 80,
	sortable : true,
	dataIndex : 'amount'
} ]);

var gridFormTooling = new Ext.FormPanel({
	id : 'tooling-form',
	frame : true,
	labelAlign : 'left',
	bodyStyle : 'padding:5px',
	layout : 'column',
	items : [ {
		id : 'toolingcolumn',
		columnWidth : 1,
		layout : 'fit',
		xtype : 'grid',
		ds : dataStoreTooling,
		cm : colModelTooling,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				rowselect : function(sm, row, rec) {
					Ext.getCmp("tooling-form").getForm().loadRecord(rec);
				}
			}
		}),
		autoExpandColumn : 'toolingname',
		height : 350,
		title : '工装列表',
		border : true,
		listeners : {
			render : function(g) {
				g.getSelectionModel().selectRow(0);
			},
			delay : 500
		}

	}, {
		frame : true,
		width: 350,
		//columnWidth : 0.35,
		xtype : 'fieldset',
		labelWidth : 60,
		title : '&nbsp;工装详情',
		defaults : {
			width : 300,
			border : false
		}, // Default config options for child items
		defaultType : 'textfield',
		autoHeight : true,
		bodyStyle : 'padding:5px 5px 0',
		border : false,
		style : {
			"margin-left" : "20px", // when you add custom margin in IE 6...
			"margin-right" : Ext.isIE6 ? (Ext.isStrict ? "-10px" : "-13px") : "0" // you have to adjust for it somewhere else
		},
		items : [ {
			fieldLabel : '编号',
			id : 'toolingid',
			name : 'id',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'

		}, {
			fieldLabel : '工装名称',
			id : 'toolingname',
			name : 'name'
		}, {
			fieldLabel : '操作数量',
			id : 'toolingopernum',
			name : 'opernum',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数',
			disabled : true
		}, {
			fieldLabel : '可用总量',
			id : 'toolingamount',
			name : 'amount',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数',
			disabled : true
		// disabled 的元素不会被 submit
		}, {
			xtype : 'radiogroup',
			id : 'toolingmethod',
			fieldLabel : '操作',
			columns : 3,
			allowBlank : false,
			items : [ {
				boxLabel : '搜索',
				name : 'method',
				inputValue : 'find',
				checked : true
			}, {
				boxLabel : '借出',
				name : 'method',
				inputValue : 'out'
			}, {
				boxLabel : '回收',
				name : 'method',
				inputValue : 'in'
			}, {
				boxLabel : '添加',
				name : 'method',
				inputValue : 'add'
			}, {
				boxLabel : '修改',
				name : 'method',
				inputValue : 'modify'
			}, {
				boxLabel : '删除',
				name : 'method',
				inputValue : 'delete'
			} ],
			listeners : {
				change : function(radiogroup, checkedradio) {

					var textfieldid = Ext.getCmp("tooling-form").getForm().findField('toolingid');
					var textfieldname = Ext.getCmp("tooling-form").getForm().findField('toolingname');
					var textfieldopernum = Ext.getCmp("tooling-form").getForm().findField('toolingopernum');
					var textfieldamount = Ext.getCmp("tooling-form").getForm().findField('toolingamount');

					switch (checkedradio.inputValue) {
					case 'find':
						textfieldid.enable();
						textfieldname.enable();
						textfieldopernum.disable();
						textfieldopernum.reset();
						textfieldamount.disable();
						textfieldamount.reset();
						break;
					case 'out':
						textfieldid.enable();
						textfieldname.disable();
						textfieldopernum.enable();
						textfieldopernum.reset();
						textfieldamount.disable();
						textfieldamount.reset();
						break;
					case 'in':
						textfieldid.enable();
						textfieldname.disable();
						textfieldopernum.enable();
						textfieldopernum.reset();
						textfieldamount.disable();
						textfieldamount.reset();
						break;
					case 'add':
						textfieldid.disable();
						textfieldid.reset();
						textfieldname.enable();
						textfieldopernum.disable();
						textfieldopernum.reset();
						textfieldamount.enable();
						textfieldamount.reset();
						break;
					case 'modify':
						textfieldid.enable();
						textfieldname.enable();
						textfieldopernum.disable();
						textfieldopernum.reset();
						textfieldamount.enable();
						break;
					case 'delete':
						textfieldid.enable();
						textfieldid.reset();
						textfieldname.disable();
						textfieldname.reset();
						textfieldopernum.disable();
						textfieldopernum.reset();
						textfieldamount.disable();
						textfieldamount.reset();
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
			handler : submitTooling
		}, {
			text : '清空',
			handler : resetTooling
		}, {
			text : '刷新列表',
			handler : refreshTooling
		} ]
	} ]
});

function submitTooling() {

	if (!gridFormTooling.getForm().isValid())
		return;

	var textfieldid = gridFormTooling.getForm().findField('toolingid').getValue();
	var textfieldname = gridFormTooling.getForm().findField('toolingname').getValue();
	var textfieldopernum = gridFormTooling.getForm().findField('toolingopernum').getValue();
	var textfieldamount = gridFormTooling.getForm().findField('toolingamount').getValue();

	var method = gridFormTooling.getForm().findField('toolingmethod').getValue().inputValue;

	switch (method) {
	case 'find':
		if (textfieldid == '' && textfieldname == '') {
			Ext.Msg.alert('提示', '搜索工装时请至少指定一项条件');
			return;
		}

		url = urlTooling + "?method=find&id=" + textfieldid + "&name=" + textfieldname;
		ajaxGetText(url, bindToolingXML);
		return;

		break;
	case 'out':
		if (textfieldid == '' || textfieldopernum == '') {
			Ext.Msg.alert('提示', '借出时编号与操作数量必须填写');
			return;
		}
		break;
	case 'in':
		if (textfieldid == '' && textfieldopernum == '') {
			Ext.Msg.alert('提示', '回收时编号与操作数量必须填写');
			return;
		}
		break;
	case 'add':
		if (textfieldid != '') {
			Ext.Msg.alert('提示', '添加工装不能指定编号');
			return;
		}
		if (textfieldname == '' || textfieldamount == '') {
			Ext.Msg.alert('提示', '添加工装必须填写名称和总量');
			return;
		}
		break;
	case 'modify':
		if (textfieldid == '' || textfieldname == '' || textfieldamount == '') {
			Ext.Msg.alert('提示', '更改工装时编号、名称、总量不准为空');
			return;
		}
		break;
	case 'delete':
		if (textfieldid == '') {
			Ext.Msg.alert('提示', '删除工艺时编号必须指定');
			return;
		}
		break;
	default:
		Ext.Msg.alert('提示', '系统错误，请完整加载页面再操作');
		return;
		break;
	}

	gridFormTooling.getForm().submit({
		waitMsg : '正在提交数据，超时不动请刷新页面，或者重新登录',
		waitTitle : '提示',
		url : urlTooling,
		method : 'POST',
		success : function(form, action) {
			try {
				Ext.Msg.alert('提示', '保存成功');
				if (method != "delete") {
					id = action.result.infos.id;
					ajaxGetText(urlTooling + "?method=find&id=" + id, bindToolingXML);
				} else {
					ajaxGetText(urlGetXmlTooling, bindToolingXML);
				}

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

function resetTooling() {
	gridFormTooling.getForm().reset();
}

function refreshTooling() {
	ajaxGetText(urlGetXmlTooling, bindToolingXML);
}
