//processdoc中使用的全局变量
var pdocXML
// 用于TabPanel的添加
function processdoc(tabPanel, btn) {

	var n = tabPanel.add({
		id : btn.id,
		title : btn.text,
		// html : '<iframe width=100% height=100% src="webpage/' + btn.id + '.jsp" />',
		// autoLoad : {url: 'webpage/' + btn.id + '.jsp', scripts: true},
		layout : 'fit',
		items : [ gridForm ],

		autoScroll : true,
		closable : false,// TODO 关闭后再打开会出错

	});

	tabPanel.setActiveTab(n);
	
	// 更新pdocData,需要提前引用pelloz.js，服务器返回的是字符串形式的xml
	ajaxGetText("processdoc.do?method=getpdocxmlstr", bindPdocXML);

}

function bindPdocXML(xmlstr) {
	pdocXML = loadXMLString(xmlstr);
	dataStore1.loadData(pdocXML);
}

// data bind
var pdocs = Ext.data.Record.create([ {
	name : 'id'
}, {
	name : 'title'
}, {
	name : 'content'
}, {
	name : 'author'
}, ]);

var dataStore1 = new Ext.data.Store({
	reader : new Ext.data.XmlReader({
		record : "pdoc",// The repeated element which contains row information
	}, pdocs)
});

// 主界面
var colModel = new Ext.grid.ColumnModel([ {
	id : 'id',
	header : "编号",
	width : 50,
	sortable : true,
	locked : false,
	dataIndex : 'id'
}, {
	id : 'title',
	header : "标题",
	width : 100,
	sortable : true,
	dataIndex : 'title'
}, {
	id : 'content',
	header : "内容",
	width : 300,
	sortable : true,
	dataIndex : 'content'
}, {
	id : 'author',
	header : "作者",
	width : 80,
	sortable : true,
	dataIndex : 'author'
}, ]);

var gridForm = new Ext.FormPanel({
	id : 'company-form',
	frame : true,
	labelAlign : 'left',
	bodyStyle : 'padding:5px',
	layout : 'column', // Specifies that the items will now be arranged in columns
	items : [
			{
				columnWidth : 0.60,
				layout : 'fit',
				items : {
					xtype : 'grid',
					ds : dataStore1,
					cm : colModel,
					sm : new Ext.grid.RowSelectionModel({
						singleSelect : true,
						listeners : {
							rowselect : function(sm, row, rec) {
								Ext.getCmp("company-form").getForm()
										.loadRecord(rec);
							}
						}
					}),
					autoExpandColumn : 'title',
					height : 350,
					title : '工艺文件列表',
					border : true,
					listeners : {
						render : function(g) {
							g.getSelectionModel().selectRow(0);
						},
						delay : 10
					// Allow rows to be rendered.
					}
				}
			},
			{
				frame : true,
				columnWidth : 0.4,
				xtype : 'fieldset',
				labelWidth : 60,
				title : '&nbsp;工艺详情',
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
					"margin-right" : Ext.isIE6 ? (Ext.isStrict ? "-10px"
							: "-13px") : "0" // you have to adjust for it somewhere else
				},
				items : [
						{
							fieldLabel : '编号',
							id : 'id',
							name : 'id',
							regex : /^\d+$/,
							regexText : '编号只准输入正整数',

						},
						{
							fieldLabel : '标题',
							id : 'title',
							name : 'title'
						},
						{
							fieldLabel : '内容',
							id : 'content',
							name : 'content',
							disabled : true,
						},
						{
							fieldLabel : '作者',
							id : 'author',
							name : 'author',
						},
						{
							xtype : 'radiogroup',
							id : 'method',
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
							}, ],
							listeners : {
								change : function(radiogroup, checkedradio) {

									var textfieldid = Ext
											.getCmp("company-form").getForm()
											.findField('id');
									var textfieldtitle = Ext.getCmp(
											"company-form").getForm()
											.findField('title');
									var textfieldauthor = Ext.getCmp(
											"company-form").getForm()
											.findField('author');
									var textfieldcontent = Ext.getCmp(
											"company-form").getForm()
											.findField('content');

									switch (checkedradio.inputValue) {
									case 'add':
										textfieldid.reset();
										textfieldid.disable();
										textfieldcontent.enable();
										textfieldauthor.reset();
										textfieldauthor.disable();
										break;
									case 'modify':
										textfieldid.enable();
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
										textfieldcontent.disable();
										textfieldcontent.reset();
										textfieldauthor.enable();
										break;
									default:
										;
										break;
									}

								},
							// Allow rows to be rendered.
							}
						} ],

				buttons : [ {
					text : '提交',
					handler : submit
				}, {
					text : '清空',
					handler : reset
				}, {
					text : '刷新列表',
					handler : refresh
				} ]
			} ]
});

function submit() {

	if (!gridForm.getForm().isValid())
		return;

	var textfieldid = gridForm.getForm().findField('id').getValue();
	var textfieldtitle = gridForm.getForm().findField('title').getValue();
	var textfieldauthor = gridForm.getForm().findField('author').getValue();
	var textfieldcontent = gridForm.getForm().findField('content').getValue();

	switch (gridForm.getForm().findField('method').getValue().inputValue) {
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

	if (gridForm.getForm().findField('method').getValue().inputValue == 'find') {
		url = "processdoc.do?method=find&id=" + textfieldid + "&title="
				+ textfieldtitle + "&author=" + textfieldauthor;
		ajaxGetText(url, bindPdocXML);
		return;
	}

	gridForm.getForm().submit({
		waitMsg : '正在提交数据，超时不动可能权限不足，请刷新页面',
		waitTitle : '提示',
		url : 'processdoc.do',
		method : 'POST',
		success : function(form, action) {
			try {
				Ext.Msg.alert('提示', '保存成功');
				ajaxGetText("processdoc.do?method=getpdocxmlstr", bindPdocXML);
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

function reset() {
	gridForm.getForm().reset();
}

function refresh() {
	ajaxGetText("processdoc.do?method=getpdocxmlstr", bindPdocXML);
}
