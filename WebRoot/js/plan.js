//plan中使用的全局变量
var planXML
var urlGetXmlPlan = "plan.do?method=getplanxmlstr";
var urlPlan = "plan.do";
// 用于TabPanel的添加
function plan(tabPanel, btn) {

	var n = tabPanel.add({
		id : btn.id,
		title : btn.text,
		layout : 'fit',
		items : [ gridFormPlan ],

		autoScroll : true,
		closable : true
	});

	tabPanel.setActiveTab(n);

	// 更新planData,需要提前引用pelloz.js，服务器返回的是字符串形式的xml
	ajaxGetText(urlGetXmlPlan, bindPlanXML);

}

function bindPlanXML(xmlstr) {
	planXML = loadXMLString(xmlstr);
	dataStorePlan.loadData(planXML);
}

// data bind
var plans = Ext.data.Record.create([ {
	name : 'id',
	type : 'int'
}, {
	name : 'pdocid',
	type : 'int'
}, {
	name : 'title'
}, {
	name : 'num',
	type : 'int'
}, {
	name : 'enddate',
	type : 'date',
	dateFormat : 'Y-m-d'
}, {
	name : 'onplan',
	type : 'boolean'
}, {
	name : 'onproducting',
	type : 'boolean'
}, {
	name : 'complete',
	type : 'boolean'
}, {
	name : 'user'
} ]);

var dataStorePlan = new Ext.data.Store({
	sortInfo : {
		field : "id",
		direction : "ASC"
	},
	reader : new Ext.data.XmlReader({
		record : "plan",// The repeated element which contains row information
	}, plans)
});

// 主界面
var colModelPlan = new Ext.grid.ColumnModel([ {
	id : 'planid',// 用于和表格的数据绑定
	header : "编号",
	width : 50,
	sortable : true,
	locked : false,
	dataIndex : 'id'// 用于和Ext.data.Record的元素name相对应
}, {
	id : 'planpdocid',
	header : "工艺编号",
	width : 65,
	sortable : true,
	dataIndex : 'pdocid'
}, {
	id : 'plantitle',
	header : "计划标题",
	width : 150,
	sortable : true,
	dataIndex : 'title'
}, {
	id : 'plannum',
	header : "生产数量",
	width : 65,
	sortable : true,
	dataIndex : 'num'
}, {
	id : 'planenddate',
	header : "完成时间",
	width : 100,
	sortable : true,
	renderer : changDateToRed,
	dataIndex : 'enddate'
}, {
	header : "已上线",
	width : 55,
	sortable : true,
	renderer : trueToGreen,
	dataIndex : 'onplan'
}, {
	header : "在生产",
	width : 55,
	sortable : true,
	renderer : trueToGreen,
	dataIndex : 'onproducting'
}, {
	header : "已完成",
	width : 55,
	sortable : true,
	renderer : trueToGreen,
	dataIndex : 'complete'
}, {
	header : "负责人",
	width : 55,
	sortable : true,
	dataIndex : 'user'
} ]);

var gridFormPlan = new Ext.FormPanel({
	id : 'plan-form',
	frame : true,
	labelAlign : 'left',
	bodyStyle : 'padding:5px',
	layout : 'column', // Specifies that the items will now be arranged in columns
	items : [ {
		id : 'plancolumn',
		columnWidth : 1,
		layout : 'fit',
		xtype : 'grid',
		ds : dataStorePlan,
		cm : colModelPlan,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				rowselect : function(sm, row, rec) {
					Ext.getCmp("plan-form").getForm().loadRecord(rec);
				}
			}
		}),
		autoExpandColumn : 'plantitle',
		height : 350,
		title : '生产计划列表(单击修改，双击查看工艺详情)',
		border : true,
		listeners : {
			render : function(g) {
				g.getSelectionModel().selectRow(0);
			},
			rowdblclick : function(grid, row) {
				activePdocPanel(dataStorePlan.getAt(row).data["pdocid"]);
			},
			delay : 400
		}

	}, {
		frame : true,
		width : 400,
		// columnWidth : 0.35,
		xtype : 'fieldset',
		labelWidth : 60,
		title : '&nbsp;计划详情',
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
			id : 'planid',
			name : 'id',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'

		}, {
			fieldLabel : '工艺编号',
			id : 'planpdocid',
			name : 'pdocid',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'
		}, {
			fieldLabel : '计划标题',
			id : 'plantitle',
			name : 'title'
		}, {
			fieldLabel : '生产数量',
			id : 'plannum',
			name : 'num',
			regex : /^\d+$/,
			regexText : '生产数量只准输入正整数',
			disabled : true
		}, {
			xtype : 'datefield',
			fieldLabel : '完成时间',
			id : 'planenddate',
			name : 'enddate',
			editable : false,
			format : 'Y-m-d',

		}, {
			xtype : 'radiogroup',
			id : 'planmethod',
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
			}, {
				boxLabel : '查看工艺',
				name : 'method',
				inputValue : 'pdoc'

			} ],
			listeners : {
				change : function(radiogroup, checkedradio) {

					var textfieldid = Ext.getCmp("plan-form").getForm().findField('planid');
					var textfieldpdocid = Ext.getCmp("plan-form").getForm().findField('planpdocid');
					var textfieldtitle = Ext.getCmp("plan-form").getForm().findField('plantitle');
					var textfieldnum = Ext.getCmp("plan-form").getForm().findField('plannum');
					var textfieldenddate = Ext.getCmp("plan-form").getForm().findField('planenddate');

					switch (checkedradio.inputValue) {
					case 'add':
						textfieldid.reset();
						textfieldid.disable();
						textfieldpdocid.reset();
						textfieldpdocid.enable();
						textfieldtitle.enable();
						textfieldnum.enable();
						textfieldenddate.enable();
						break;
					case 'modify':
						textfieldid.enable();
						textfieldpdocid.enable();
						textfieldtitle.enable();
						textfieldnum.enable();
						textfieldenddate.enable();
						break;
					case 'delete':
						textfieldid.enable();
						textfieldpdocid.disable();
						textfieldtitle.disable();
						textfieldnum.disable();
						textfieldenddate.disable();
						break;
					case 'find':
						textfieldid.enable();
						textfieldpdocid.disable();
						textfieldtitle.enable();
						textfieldnum.disable();
						textfieldenddate.enable();
						break;
					case 'pdoc':
						textfieldid.enable();
						textfieldpdocid.disable();
						textfieldtitle.disable();
						textfieldnum.disable();
						textfieldenddate.disable();
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
			width : 55,
			style : 'margin-left:60px',
			handler : submitPlan
		}, {
			text : '清空',
			width : 55,
			handler : resetPlan
		}, {
			text : '上线计划',
			width : 55,
			handler : onPlan
		}, {
			text : '刷新列表',
			width : 55,
			handler : refreshPlan
		} ]
	} ]
});

function submitPlan() {

	if (!gridFormPlan.getForm().isValid())
		return;

	var textfieldid = Ext.getCmp("plan-form").getForm().findField('planid').getValue();
	var textfieldpdocid = Ext.getCmp("plan-form").getForm().findField('planpdocid').getValue();
	var textfieldtitle = Ext.getCmp("plan-form").getForm().findField('plantitle').getValue();
	var textfieldnum = Ext.getCmp("plan-form").getForm().findField('plannum').getValue();
	var textfieldenddate = Ext.getCmp("plan-form").getForm().findField('planenddate').getValue();

	var method = gridFormPlan.getForm().findField('planmethod').getValue().inputValue;

	switch (method) {
	case 'add':
		if (textfieldid != '') {
			Ext.Msg.alert('提示', '添加生产计划不能指定编号');
			return;
		}
		if (textfieldtitle == '' || textfieldpdocid == '' || textfieldnum == '' || textfieldenddate == '') {
			Ext.Msg.alert('提示', '添加生产计划必须填写标题、工艺编号，生产数量和完成日期');
			return;
		}
		break;
	case 'modify':
		if (textfieldid = '' || textfieldtitle == '' || textfieldpdocid == '' || textfieldnum == ''
				|| textfieldenddate == '') {
			Ext.Msg.alert('提示', '修改生产计划必须填写计划编号、标题、工艺编号，生产数量和完成日期');
			return;
		}
		break;
	case 'delete':
		if (textfieldid == '') {
			Ext.Msg.alert('提示', '删除生产计划时编号不能为空');
			return;
		}
		break;
	case 'find':
		if (textfieldid == '' && textfieldtitle == '' && textfieldenddate == '') {
			Ext.Msg.alert('提示', '搜索生产计划时请指定计划编号、标题或者时间');
			return;
		}

		url = urlPlan + "?method=find&id=" + textfieldid + "&title=" + textfieldtitle + "&enddate="
				+ textfieldenddate.format('Y-m-d');
		ajaxGetText(url, bindPlanXML);
		return;

		break;
	case 'pdoc':
		if (textfieldpdocid == '') {
			Ext.Msg.alert('提示', '查看工艺文件时必须指定工艺文件编号');
			return;
		}
		activePdocPanel(textfieldpdocid);
		return;
		break;
	default:
		Ext.Msg.alert('提示', '系统错误，请完整加载页面再操作');
		return;
		break;
	}

	gridFormPlan.getForm().submit({
		waitMsg : '正在提交数据，超时不动请刷新页面，或者重新登录',
		waitTitle : '提示',
		url : urlPlan,
		method : 'POST',
		success : function(form, action) {
			try {
				Ext.Msg.alert('提示', '保存成功');
				ajaxGetText(urlGetXmlPlan, bindPlanXML);
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

function resetPlan() {
	gridFormPlan.getForm().reset();
}

function refreshPlan() {
	ajaxGetText(urlGetXmlPlan, bindPlanXML);
}

function onPlan() {
	var textfieldid = Ext.getCmp("plan-form").getForm().findField('planid').getValue();
	if (textfieldid == '') {
		Ext.Msg.alert('提示', '上线计划必须指定计划编号');
		return;
	}
	url = urlPlan + "?method=onplan&id=" + textfieldid;
	ajaxGetText(url, bindPlanXML);
}
