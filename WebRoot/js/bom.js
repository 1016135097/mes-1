//可以全局替换的变量名 
//bomXML bom getbomxmlstr bindBomXML dataStoreBom gridFormBom
//submitBom resetBom refreshBom boms bom colModelBom bom-form

//bom中使用的全局变量
//必须先设置bomPdocid以后才能使用urlGetXmlBom获取数据
var bomXML;
var bomPdocid = null;
var urlBom = "bom.do";
function urlGetXmlBom() {
	return urlBom + "?method=getbomxmlstr&pdocid=" + bomPdocid;
}


function activeBomPanel(pdocid, title) {

	bomPdocid = pdocid;
	ajaxGetText(urlGetXmlBom(), bindBomXML);
	gridFormBom.getComponent('bomcolumn').setTitle('BOM列表####工艺编号: ' + pdocid + '####工艺标题: ' + title);

	var n;
	tabPanel = Ext.getCmp("tabPanel");
	n = tabPanel.getComponent('bom');
	if (n) {
		n.show();
		tabPanel.setActiveTab(n);
		return;
	}
	n = tabPanel.add({
		id : 'bom',
		title : '物料清单',
		layout : 'fit',
		items : [ gridFormBom ],

		autoScroll : true,
		closable : true
	});

	tabPanel.setActiveTab(n);

	return;
}


function bindBomXML(xmlstr) {
	bomXML = loadXMLString(xmlstr);
	dataStoreBom.loadData(bomXML);
}

// data bind
var boms = Ext.data.Record.create([ {
	name : 'id',
	type : 'int'
}, {
	name : 'toolingid',
	type : 'int'
}, {
	name : 'toolingname'
}, {
	name : 'amount',
	type : 'int'
}, {
	name : 'inventory',
	type : 'int'
} ]);

var dataStoreBom = new Ext.data.Store({
	sortInfo: { field: "id", direction: "ASC" },
	reader : new Ext.data.XmlReader({
		record : "bom",
	}, boms)
});

// 主界面
var colModelBom = new Ext.grid.ColumnModel([ {
	id : 'bomid',// 用于和表格的数据绑定
	header : "BOM编号",
	width : 80,
	sortable : true,
	locked : false,
	dataIndex : 'id'// 用于和Ext.data.Record的元素name相对应
}, {
	id : 'bomtoolingid',
	header : "工装编号",
	width : 80,
	sortable : true,
	dataIndex : 'toolingid'
}, {
	id : 'bomtoolingname',
	header : "工装名称",
	width : 300,
	sortable : true,
	dataIndex : 'toolingname'
}, {
	id : 'bomamount',
	header : "需求数量",
	width : 80,
	sortable : true,
	dataIndex : 'amount'
}, {
	id : 'inventory',
	header : "库存数量",
	width : 80,
	sortable : true,
	renderer : function(value, metaData, record, rowIndex, colIndex, store) {
		needNum = dataStoreBom.getAt(rowIndex).data["amount"];
		if (value < needNum * 2) {
			return '<span style="color: red;">' + value + '</span>';
		}
		return '<span >' + value + '</span>';
	},
	dataIndex : 'inventory'
} ]);

var gridFormBom = new Ext.FormPanel({
	id : 'bom-form',
	frame : true,
	labelAlign : 'left',
	bodyStyle : 'padding:5px',
	layout : 'column',
	items : [ {
		id : 'bomcolumn',
		columnWidth : 1,
		layout : 'fit',
		xtype : 'grid',
		ds : dataStoreBom,
		cm : colModelBom,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				rowselect : function(sm, row, rec) {
					Ext.getCmp("bom-form").getForm().loadRecord(rec);
				}
			}
		}),
		autoExpandColumn : 'bomtoolingname',
		height : 350,
		title : 'BOM列表##',
		border : true,
		listeners : {
			render : function(g) {
				g.getSelectionModel().selectRow(0);
			},
			rowdblclick : function(grid, row) {
				if (dataStoreBom.getAt(row).data["toolingid"] <= 0) {
					return;
				}
				activeToolingPanel(dataStoreBom.getAt(row).data["toolingid"]);
			},
			delay : 400
		}

	}, {
		frame : true,
		width : 400,
		// columnWidth : 0.35,
		xtype : 'fieldset',
		labelWidth : 60,
		title : '&nbsp;BOM详情',
		defaults : {
			width : 280,
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
			id : 'bomid',
			name : 'id',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'

		}, {
			fieldLabel : '工装编号',
			id : 'bomtoolingid',
			name : 'toolingid',
			disabled : true
		}, {
			fieldLabel : '需求数量',
			id : 'bomamount',
			name : 'amount',
			regex : /^\d+$/,
			regexText : '需求数量只准输入正整数'
		}, {
			id : 'bomPdocid',
			name : 'pdocid',
			hidden : true
		}, {
			xtype : 'radiogroup',
			style : 'padding-left: 0px',
			id : 'bommethod',
			fieldLabel : '操作',
			columns : 3,
			allowBlank : false,
			items : [ {
				boxLabel : '添加',
				name : 'method',
				inputValue : 'add'
			}, {
				boxLabel : '修改',
				name : 'method',
				inputValue : 'modify',
				checked : true
			}, {
				boxLabel : '删除',
				name : 'method',
				inputValue : 'delete'
			}, {
				boxLabel : '查看工装',
				name : 'method',
				inputValue : 'tooling'
			} ],
			listeners : {
				change : function(radiogroup, checkedradio) {

					var textfieldid = Ext.getCmp("bom-form").getForm().findField('bomid');
					var textfieldtoolingid = Ext.getCmp("bom-form").getForm().findField('bomtoolingid');
					var textfieldamount = Ext.getCmp("bom-form").getForm().findField('bomamount');

					switch (checkedradio.inputValue) {
					case 'add':
						textfieldid.disable();
						textfieldid.reset();
						textfieldtoolingid.enable();
						textfieldamount.enable();
						break;
					case 'modify':
						textfieldid.enable();
						textfieldtoolingid.disable();
						textfieldamount.enable();
						break;
					case 'delete':
						textfieldid.enable();
						textfieldid.reset();
						textfieldtoolingid.disable();
						textfieldtoolingid.reset();
						textfieldamount.disable();
						textfieldamount.reset();
						break;
					case 'tooling':
						textfieldid.disable();
						textfieldtoolingid.enable();
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
			handler : submitBom
		}, {
			text : '清空',
			handler : resetBom
		}, {
			text : '刷新列表',
			handler : refreshBom
		} ]
	} ]
});

function submitBom() {

	if (!gridFormBom.getForm().isValid())
		return;

	var textfieldid = Ext.getCmp("bom-form").getForm().findField('bomid').getValue();
	var textfieldtoolingid = Ext.getCmp("bom-form").getForm().findField('bomtoolingid').getValue();
	var textfieldamount = Ext.getCmp("bom-form").getForm().findField('bomamount').getValue();

	var method = gridFormBom.getForm().findField('bommethod').getValue().inputValue;

	switch (method) {
	case 'add':
		if (textfieldtoolingid == '' || textfieldamount == '') {
			Ext.Msg.alert('提示', '添加BOM必须填写名称和总量');
			return;
		}
		break;
	case 'modify':
		if (textfieldid == '' || textfieldtoolingid == '' || textfieldamount == '') {
			Ext.Msg.alert('提示', '更改BOM时编号、名称、总量不准为空');
			return;
		}

		break;
	case 'delete':
		if (textfieldid == '') {
			Ext.Msg.alert('提示', '删除工艺时编号必须指定');
			return;
		}
		break;
	case 'tooling':
		if (textfieldtoolingid == '') {
			Ext.Msg.alert('提示', '必须指定工装编号');
			return;
		}
		activeToolingPanel(textfieldtoolingid);
		return;
		break;
	default:
		Ext.Msg.alert('提示', '系统错误，请完整加载页面再操作');
		return;
		break;
	}

	// 设置对应的pdoc
	Ext.getCmp("bom-form").getForm().findField('bomPdocid').setValue(bomPdocid);

	gridFormBom.getForm().submit({
		waitMsg : '正在提交数据，超时不动请刷新页面，或者重新登录',
		waitTitle : '提示',
		url : urlBom,
		method : 'POST',
		success : function(form, action) {
			try {
				Ext.Msg.alert('提示', '保存成功');
				ajaxGetText(urlGetXmlBom(), bindBomXML);
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

function resetBom() {
	gridFormBom.getForm().reset();
}

function refreshBom() {
	ajaxGetText(urlGetXmlBom(), bindBomXML);
}
