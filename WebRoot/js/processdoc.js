//processdoc中使用的全局变量
var pdocXML
var urlGetXmlPdoc = "processdoc.do?method=getpdocxmlstr";
var urlPdoc = "processdoc.do";
// 用于TabPanel的添加
function processdoc(tabPanel, btn) {

	var n = tabPanel.add({
		id : btn.id,
		title : btn.text,
		layout : 'fit',
		items : [ gridFormPdoc ],

		autoScroll : true,
		closable : true
	});

	tabPanel.setActiveTab(n);

	// 更新pdocData,需要提前引用pelloz.js，服务器返回的是字符串形式的xml
	ajaxGetText(urlGetXmlPdoc, bindPdocXML);

}

function bindPdocXML(xmlstr) {
	pdocXML = loadXMLString(xmlstr);
	dataStorePdoc.loadData(pdocXML);
}

// data bind
var pdocs = Ext.data.Record.create([ {
	name : 'id',
	type : 'int'
}, {
	name : 'title'
}, {
	name : 'content'
}, {
	name : 'author'
} ]);

var dataStorePdoc = new Ext.data.Store({
	reader : new Ext.data.XmlReader({
		record : "pdoc",// The repeated element which contains row information
	}, pdocs)
});

// 主界面
var colModelPdoc = new Ext.grid.ColumnModel([ {
	id : 'pdocid',// 用于和表格的数据绑定
	header : "编号",
	width : 50,
	sortable : true,
	locked : false,
	dataIndex : 'id'// 用于和Ext.data.Record的元素name相对应
}, {
	id : 'pdoctitle',
	header : "标题",
	width : 100,
	sortable : true,
	dataIndex : 'title'
}, {
	id : 'pdoccontent',
	header : "内容",
	width : 300,
	sortable : true,
	dataIndex : 'content'
}, {
	id : 'pdocauthor',
	header : "作者",
	width : 80,
	sortable : true,
	dataIndex : 'author'
} ]);

var gridFormPdoc = new Ext.FormPanel({
	id : 'pdoc-form',
	frame : true,
	labelAlign : 'left',
	bodyStyle : 'padding:5px',
	layout : 'column', // Specifies that the items will now be arranged in columns
	items : [ {
		columnWidth : 0.60,
		layout : 'fit',
		xtype : 'grid',
		ds : dataStorePdoc,
		cm : colModelPdoc,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				rowselect : function(sm, row, rec) {
					Ext.getCmp("pdoc-form").getForm().loadRecord(rec);
				}
			}
		}),
		autoExpandColumn : 'pdoctitle',
		height : 350,
		title : '工艺文件列表(单击显示详情，双击查看修改BOM)',
		border : true,
		listeners : {
			render : function(g) {
				g.getSelectionModel().selectRow(0);
			},
			rowdblclick : function(grid, row) {
				activeBomPanel(dataStorePdoc.getAt(row).data["id"], dataStorePdoc.getAt(row).data["title"]);
			},
			delay : 400
		}

	}, {
		frame : true,
		columnWidth : 0.4,
		xtype : 'fieldset',
		labelWidth : 60,
		title : '&nbsp;工艺文件详情',
		defaults : {
			width : 300,
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
			id : 'pdocid',
			name : 'id',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'

		}, {
			fieldLabel : '标题',
			id : 'pdoctitle',
			name : 'title'
		}, {
			fieldLabel : '内容',
			id : 'pdoccontent',
			name : 'content',
			disabled : true
		}, {
			fieldLabel : '作者',
			id : 'pdocauthor',
			name : 'author'
		}, {
			xtype : 'radiogroup',
			id : 'method',
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
				boxLabel : '查看BOM',
				name : 'method',
				inputValue : 'bom'

			} ],
			listeners : {
				change : function(radiogroup, checkedradio) {

					var textfieldid = Ext.getCmp("pdoc-form").getForm().findField('pdocid');
					var textfieldtitle = Ext.getCmp("pdoc-form").getForm().findField('pdoctitle');
					var textfieldauthor = Ext.getCmp("pdoc-form").getForm().findField('pdocauthor');
					var textfieldcontent = Ext.getCmp("pdoc-form").getForm().findField('pdoccontent');

					switch (checkedradio.inputValue) {
					case 'add':
						textfieldid.reset();
						textfieldid.disable();
						textfieldtitle.enable();
						textfieldcontent.enable();
						textfieldauthor.reset();
						textfieldauthor.disable();
						break;
					case 'modify':
						textfieldid.enable();
						textfieldtitle.enable();
						textfieldcontent.enable();
						textfieldauthor.reset();// 谁更改谁负责，不能指定作者
						textfieldauthor.disable();
						break;
					case 'delete':
						textfieldid.enable();
						textfieldtitle.disable();
						textfieldcontent.disable();
						textfieldauthor.disable();
						break;
					case 'find':
						textfieldid.enable();
						textfieldtitle.enable();
						textfieldcontent.disable();
						textfieldcontent.reset();
						textfieldauthor.enable();
						break;
					case 'bom':
						textfieldid.enable();
						textfieldtitle.disable();
						textfieldcontent.disable();
						textfieldauthor.disable();
						break;
					default:
						;
						break;
					}

				}
			// Allow rows to be rendered.
			}
		} ],

		buttonAlign : 'left',

		buttons : [ {
			text : '提交',
			style : 'margin-left:60px',
			handler : submitPdoc
		}, {
			text : '清空',
			handler : resetPdoc
		}, {
			text : '刷新列表',
			handler : refreshPdoc
		} ]
	} ]
});

function submitPdoc() {

	if (!gridFormPdoc.getForm().isValid())
		return;

	var textfieldid = gridFormPdoc.getForm().findField('pdocid').getValue();
	var textfieldtitle = gridFormPdoc.getForm().findField('pdoctitle').getValue();
	var textfieldauthor = gridFormPdoc.getForm().findField('pdocauthor').getValue();
	var textfieldcontent = gridFormPdoc.getForm().findField('pdoccontent').getValue();

	var method = gridFormPdoc.getForm().findField('method').getValue().inputValue;

	switch (method) {
	case 'add':
		if (textfieldid != '') {
			Ext.Msg.alert('提示', '添加工艺不能指定编号');
			return;
		}
		if (textfieldauthor != '') {
			Ext.Msg.alert('提示', '添加工艺不能指定作者');
			return;
		}
		if (textfieldtitle == '') {
			Ext.Msg.alert('提示', '添加工艺必须填写标题');
			return;
		}
		if (textfieldcontent == '') {
			Ext.Msg.alert('提示', '添加工艺必须填写内容');
			return;
		}
		break;
	case 'modify':
		if (textfieldid == '' || textfieldtitle == '' || textfieldcontent == '') {
			Ext.Msg.alert('提示', '更改工艺时编号、标题、内容不准为空');
			return;
		}
		break;
	case 'delete':
		if (textfieldid == '' && textfieldtitle == '') {
			Ext.Msg.alert('提示', '删除工艺时编号或标题至少指定一项，且以编号为优先');
			return;
		}
		break;
	case 'find':
		if (textfieldid == '' && textfieldtitle == '' && textfieldauthor == '') {
			Ext.Msg.alert('提示', '搜索工艺时请至少指定一项除工艺内容的条件');
			return;
		}

		url = urlPdoc + "?method=find&id=" + textfieldid + "&title=" + textfieldtitle + "&author=" + textfieldauthor;
		ajaxGetText(url, bindPdocXML);
		return;

		break;
	case 'bom':
		if (textfieldid == '') {
			Ext.Msg.alert('提示', '查看BOM时必须指定工艺文件编号');
			return;
		}
		activeBomPanel(textfieldid, textfieldtitle);
		return;
		break;
	default:
		Ext.Msg.alert('提示', '系统错误，请完整加载页面再操作');
		return;
		break;
	}

	gridFormPdoc.getForm().submit({
		waitMsg : '正在提交数据，超时不动请刷新页面，或者重新登录',
		waitTitle : '提示',
		url : urlPdoc,
		method : 'POST',
		success : function(form, action) {
			try {
				Ext.Msg.alert('提示', '保存成功');
				ajaxGetText(urlGetXmlPdoc, bindPdocXML);
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

function resetPdoc() {
	gridFormPdoc.getForm().reset();
}

function refreshPdoc() {
	ajaxGetText(urlGetXmlPdoc, bindPdocXML);
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
