//可以全局替换的变量名 
//bomXML bom getbomxmlstr bindBomXML dataStoreBom gridFormBom
//submitBom resetBom refreshBom boms bom colModelBom bom-form

//bom中使用的全局变量
var bomXML;
var urlGetXmlBom = "bom.do?method=getbomxmlstr";
var urlBom = "bom.do";
// 用于TabPanel的添加
function bom(tabPanel, btn) {

	var n = tabPanel.add({
		id : btn.id,
		title : btn.text,
		layout : 'fit',
		items : [ gridFormBom ],

		autoScroll : true,
		closable : false
	// TODO 关闭后再打开会出错

	});

	tabPanel.setActiveTab(n);

	ajaxGetText(urlGetXmlBom, bindBomXML);
}

function bindBomXML(xmlstr) {
	bomXML = loadXMLString(xmlstr);
	dataStoreBom.loadData(bomXML);
}

// data bind
var boms = Ext.data.Record.create([ {
	name : 'id'
}, {
	name : 'title'
}, {
	name : 'content'
}, {
	name : 'author'
} ]);

var dataStoreBom = new Ext.data.Store({
	reader : new Ext.data.XmlReader({
		record : "bom",// TODO mark The repeated element which contains row information
	}, boms)
});

// 主界面
var colModelBom = new Ext.grid.ColumnModel([ {
	id : 'bomid',
	header : "编号",
	width : 50,
	sortable : true,
	locked : false,
	dataIndex : 'id'//用于和Ext.data.Record的元素name相对应
}, {
	id : 'bomtitle',
	header : "标题",
	width : 100,
	sortable : true,
	dataIndex : 'title'
}, {
	id : 'bomcontent',
	header : "内容",
	width : 300,
	sortable : true,
	dataIndex : 'content'
}, {
	id : 'bomauthor',
	header : "作者",
	width : 80,
	sortable : true,
	dataIndex : 'author'
} ]);

var gridFormBom = new Ext.FormPanel({
	id : 'bom-form',
	frame : true,
	labelAlign : 'left',
	bodyStyle : 'padding:5px',
	layout : 'column', // Specifies that the items will now be arranged in columns
	items : [ {
		columnWidth : 0.60,
		layout : 'fit',
		items : {
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
			autoExpandColumn : 'bomtitle',
			height : 350,
			title : '工艺文件列表1',
			border : true,
			listeners : {
				render : function(g) {
					g.getSelectionModel().selectRow(0);
				},
				delay : 10
			// Allow rows to be rendered.
			}
		}
	}, {
		frame : true,
		columnWidth : 0.4,
		xtype : 'fieldset',
		labelWidth : 60,
		title : '&nbsp;工艺详情1',
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
			id : 'bomid',
			name : 'id',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'

		}, {
			fieldLabel : '标题',
			id : 'bomtitle',
			name : 'title'
		}, {
			fieldLabel : '内容',
			id : 'bomcontent',
			name : 'content',
			disabled : true
		}, {
			fieldLabel : '作者',
			id : 'bomauthor',
			name : 'author'
		}, {
			xtype : 'radiogroup',
			id : 'bommethod',
			fieldLabel : '操作',
			columns : 3,
			allowBlank : false,
			anchor : '95%',
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

					var textfieldid = Ext.getCmp("bom-form").getForm().findField('bomid');
					var textfieldtitle = Ext.getCmp("bom-form").getForm().findField('bomtitle');
					var textfieldauthor = Ext.getCmp("bom-form").getForm().findField('bomauthor');
					var textfieldcontent = Ext.getCmp("bom-form").getForm().findField('bomcontent');

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
					default:
						;
						break;
					}

				}
			// Allow rows to be rendered.
			}
		} ],

		buttons : [ {
			text : '提交',
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

	var textfieldid = gridFormBom.getForm().findField('bomid').getValue();
	var textfieldtitle = gridFormBom.getForm().findField('bomtitle').getValue();
	var textfieldauthor = gridFormBom.getForm().findField('bomauthor').getValue();
	var textfieldcontent = gridFormBom.getForm().findField('bomcontent').getValue();

	switch (gridFormBom.getForm().findField('bommethod').getValue().inputValue) {
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
		break;
	default:
		Ext.Msg.alert('提示', '系统错误，请完整加载页面再操作');
		return;
		break;
	}

	if (gridFormBom.getForm().findField('bommethod').getValue().inputValue == 'find') {
		url = urlBom + "?method=find&id=" + textfieldid + "&title=" + textfieldtitle + "&author=" + textfieldauthor;
		ajaxGetText(url, bindBomXML);
		return;
	}

	gridFormBom.getForm().submit({
		waitMsg : '正在提交数据，超时不动请刷新页面，或者重新登录',
		waitTitle : '提示',
		url : urlBom,
		method : 'POST',
		success : function(form, action) {
			try {
				Ext.Msg.alert('提示', '保存成功');
				ajaxGetText(urlGetXmlBom, bindBomXML);
			} catch (e) {
				Ext.Msg.alert('提示', '服务器返回数据错误或者权限不足');
			}
		},
		failure : function(form, action) {
			try {
				Ext.Msg.alert('错误提示', '原因：' + action.result.errors.info);
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
	ajaxGetText(urlGetXmlBom, bindBomXML);
}
