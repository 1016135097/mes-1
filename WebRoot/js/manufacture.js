//manufacture中使用的全局变量
var manufactureXML
var urlGetXmlManufacture = "plan.do?method=getplanxmlstr";
var urlManufacture = "plan.do";
// 用于TabPanel的添加
function manufacture(tabPanel, btn) {

	var n = tabPanel.add({
		id : btn.id,
		title : btn.text,
		layout : 'fit',
		items : [ gridFormManufacture ],

		autoScroll : true,
		closable : true
	});

	tabPanel.setActiveTab(n);

	// 更新manufactureData,需要提前引用pelloz.js，服务器返回的是字符串形式的xml
	ajaxGetText(urlGetXmlManufacture, bindManufactureXML);

}

function bindManufactureXML(xmlstr) {
	manufactureXML = loadXMLString(xmlstr);
	dataStoreManufacture.loadData(manufactureXML);
}

// data bind
var manufactures = Ext.data.Record.create([ {
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

var dataStoreManufacture = new Ext.data.Store({
	sortInfo : {
		field : "id",
		direction : "ASC"
	},
	reader : new Ext.data.XmlReader({
		record : "plan",// The repeated element which contains row information
	}, manufactures)
});

// 主界面
var colModelManufacture = new Ext.grid.ColumnModel([ {
	id : 'manufactureid',// 用于和表格的数据绑定
	header : "编号",
	width : 40,
	sortable : true,
	locked : false,
	dataIndex : 'id'// 用于和Ext.data.Record的元素name相对应
}, {
	id : 'manufacturepdocid',
	header : "工艺编号",
	width : 65,
	sortable : true,
	dataIndex : 'pdocid'
}, {
	id : 'manufacturetitle',
	header : "计划标题",
	width : 150,
	sortable : true,
	dataIndex : 'title'
}, {
	id : 'manufacturenum',
	header : "生产数量",
	width : 65,
	sortable : true,
	dataIndex : 'num'
}, {
	id : 'manufactureenddate',
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

var gridFormManufacture = new Ext.FormPanel({
	id : 'manufacture-form',
	frame : true,
	labelAlign : 'left',
	bodyStyle : 'padding:5px',
	layout : 'column', // Specifies that the items will now be arranged in columns
	items : [ {
		id : 'manufacturecolumn',
		columnWidth : 1,
		layout : 'fit',
		xtype : 'grid',
		ds : dataStoreManufacture,
		cm : colModelManufacture,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				rowselect : function(sm, row, rec) {
					Ext.getCmp("manufacture-form").getForm().loadRecord(rec);
				}
			}
		}),
		autoExpandColumn : 'manufacturetitle',
		height : 350,
		title : '生产计划列表(单击修改，双击查看工艺详情)',
		border : true,
		listeners : {
			render : function(g) {
				g.getSelectionModel().selectRow(0);
			},
			rowdblclick : function(grid, row) {
				if (dataStoreManufacture.getAt(row).data["pdocid"] <= 0) {
					return;
				}
				activePdocPanel(dataStoreManufacture.getAt(row).data["pdocid"]);
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
			id : 'manufactureid',
			name : 'id',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'
		}, {
			fieldLabel : '工艺编号',
			id : 'manufacturepdocid',
			name : 'pdocid',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数',
			disabled : true
		}, {
			fieldLabel : '计划标题',
			id : 'manufacturetitle',
			name : 'title',
			disabled : true
		}, {
			fieldLabel : '生产数量',
			id : 'manufacturenum',
			name : 'num',
			regex : /^\d+$/,
			regexText : '生产数量只准输入正整数',
			disabled : true
		}, {
			xtype : 'datefield',
			fieldLabel : '完成时间',
			id : 'manufactureenddate',
			name : 'enddate',
			editable : false,
			format : 'Y-m-d',
		}, {
			xtype : 'radiogroup',
			style : 'padding-left: 0px',
			id : 'manufacturemethod',
			fieldLabel : '操作',
			columns : 3,
			allowBlank : false,
			items : [ {
				boxLabel : '开始/暂停',
				name : 'method',
				inputValue : 'begin'

			}, {
				boxLabel : '查看工艺',
				name : 'method',
				inputValue : 'pdoc'

			}, {
				boxLabel : '搜索',
				name : 'method',
				inputValue : 'find',
				checked : true
			}, {
				boxLabel : '完成生产',
				name : 'method',
				inputValue : 'finish'

			} ]
		} ],

		buttonAlign : 'left',

		buttons : [ {
			text : '提交',
			width : 55,
			style : 'margin-left:60px',
			handler : submitManufacture
		}, {
			text : '清空',
			width : 55,
			handler : resetManufacture
		}, {
			text : '刷新列表',
			width : 55,
			handler : refreshManufacture
		} ]
	} ]
});

function submitManufacture() {

	if (!gridFormManufacture.getForm().isValid())
		return;

	var textfieldid = Ext.getCmp("manufacture-form").getForm().findField('manufactureid').getValue();
	var textfieldpdocid = Ext.getCmp("manufacture-form").getForm().findField('manufacturepdocid').getValue();
	var textfieldtitle = Ext.getCmp("manufacture-form").getForm().findField('manufacturetitle').getValue();
	var textfieldnum = Ext.getCmp("manufacture-form").getForm().findField('manufacturenum').getValue();
	var textfieldenddate = Ext.getCmp("manufacture-form").getForm().findField('manufactureenddate').getValue();

	var method = gridFormManufacture.getForm().findField('manufacturemethod').getValue().inputValue;

	switch (method) {
	case 'find':
		if (textfieldid == '' && textfieldenddate == '') {
			Ext.Msg.alert('提示', '搜索生产计划时请指定计划编号或者时间');
			return;
		}

		url = urlManufacture + "?method=find&id=" + textfieldid + "&enddate=" + textfieldenddate.format('Y-m-d');
		ajaxGetText(url, bindManufactureXML);
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
	case 'begin':
		if (textfieldpdocid == '') {
			Ext.Msg.alert('提示', '开始生产计划时必须指定计划编号');
			return;
		}
		break;
	case 'finish':
		if (textfieldpdocid == '') {
			Ext.Msg.alert('提示', '完成生产计划时必须指定计划编号');
			return;
		}
		break;
	default:
		Ext.Msg.alert('提示', '系统错误，请完整加载页面再操作');
		return;
		break;
	}

	gridFormManufacture.getForm().submit({
		waitMsg : '正在提交数据，超时不动请刷新页面，或者重新登录',
		waitTitle : '提示',
		url : urlManufacture,
		method : 'POST',
		success : function(form, action) {
			try {
				Ext.Msg.alert('提示', '保存成功');
				ajaxGetText(urlGetXmlManufacture, bindManufactureXML);
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

function resetManufacture() {
	gridFormManufacture.getForm().reset();
}

function refreshManufacture() {
	ajaxGetText(urlGetXmlManufacture, bindManufactureXML);
}
