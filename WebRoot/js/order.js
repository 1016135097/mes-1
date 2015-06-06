//order中使用的全局变量
var orderXML
var urlGetXmlOrder = "order.do?method=getorderxmlstr";
var urlOrder = "order.do";
// 用于TabPanel的添加
function buyingorder(tabPanel, btn) {

	var n = tabPanel.add({
		id : btn.id,
		title : btn.text,
		layout : 'fit',
		items : [ gridFormOrder ],

		autoScroll : true,
		closable : true
	});

	tabPanel.setActiveTab(n);

	// 更新orderData,需要提前引用pelloz.js，服务器返回的是字符串形式的xml
	ajaxGetText(urlGetXmlOrder, bindOrderXML);

}

function bindOrderXML(xmlstr) {
	orderXML = loadXMLString(xmlstr);
	dataStoreOrder.loadData(orderXML);
}

// data bind
var orders = Ext.data.Record.create([ {
	name : 'id',
	type : 'int'
}, {
	name : 'toolingid',
	type : 'int'
}, {
	name : 'title'
}, {
	name : 'amount',
	type : 'int'
}, {
	name : 'price',
	type : 'float'
}, {
	name : 'totalprice',
	type : 'float'
}, {
	name : 'date',
	type : 'date',
	dateFormat : 'Y-m-d'
}, {
	name : 'complete',
	type : 'boolean'
}, {
	name : 'user',
} ]);

var dataStoreOrder = new Ext.data.Store({
	sortInfo : {
		field : "id",
		direction : "ASC"
	},
	reader : new Ext.data.XmlReader({
		record : "order",// The repeated element which contains row information
	}, orders)
});

// 主界面
var colModelOrder = new Ext.grid.ColumnModel([ {
	id : 'orderid',// 用于和表格的数据绑定
	header : "编号",
	width : 50,
	sortable : true,
	locked : false,
	dataIndex : 'id'// 用于和Ext.data.Record的元素name相对应
}, {
	id : 'ordertoolingid',
	header : "工装编号",
	width : 65,
	sortable : true,
	dataIndex : 'toolingid'
}, {
	id : 'ordertitle',
	header : "订单标题",
	width : 150,
	sortable : true,
	dataIndex : 'title'
}, {
	id : 'orderamount',
	header : "购买数量",
	width : 65,
	sortable : true,
	dataIndex : 'amount'
}, {
	id : 'orderprice',
	header : "单价",
	width : 65,
	sortable : true,
	renderer : cnMoney,
	dataIndex : 'price'
}, {
	header : "总价",
	width : 65,
	sortable : true,
	renderer : cnMoney,
	dataIndex : 'totalprice'
}, {
	id : 'orderdate',
	header : "节点时间",
	width : 100,
	sortable : true,
	renderer : changDateToRed,
	dataIndex : 'date'
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

var gridFormOrder = new Ext.FormPanel({
	id : 'order-form',
	frame : true,
	labelAlign : 'left',
	bodyStyle : 'padding:5px',
	layout : 'column', // Specifies that the items will now be arranged in columns
	items : [ {
		id : 'ordercolumn',
		columnWidth : 1,
		layout : 'fit',
		xtype : 'grid',
		ds : dataStoreOrder,
		cm : colModelOrder,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				rowselect : function(sm, row, rec) {
					Ext.getCmp("order-form").getForm().loadRecord(rec);
				}
			}
		}),
		autoExpandColumn : 'ordertitle',
		height : 350,
		title : '订单列表(单击修改，双击查看工装详情)',
		border : true,
		listeners : {
			render : function(g) {
				g.getSelectionModel().selectRow(0);
			},
			rowdblclick : function(grid, row) {
				activeToolingPanel(dataStoreOrder.getAt(row).data["toolingid"]);
			},
			delay : 400
		}

	}, {
		frame : true,
		width : 400,
		// columnWidth : 0.35,
		xtype : 'fieldset',
		labelWidth : 60,
		title : '&nbsp;订单详情',
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
			id : 'orderid',
			name : 'id',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'

		}, {
			fieldLabel : '工装编号',
			id : 'ordertoolingid',
			name : 'toolingid',
			regex : /^\d+$/,
			regexText : '编号只准输入正整数'
		}, {
			fieldLabel : '订单标题',
			id : 'ordertitle',
			name : 'title'
		}, {
			fieldLabel : '购买数量',
			id : 'orderamount',
			name : 'amount',
			regex : /^\d+$/,
			regexText : '生产数量只准输入正整数',
			disabled : true
		}, {
			fieldLabel : '单价',
			id : 'orderprice',
			name : 'price',
			regex : /^\d+\.*\d{0,2}$/,
			regexText : '请正确输入价格',
			disabled : true
		}, {
			xtype : 'datefield',
			fieldLabel : '节点时间',
			id : 'orderdate',
			name : 'date',
			editable : false,
			format : 'Y-m-d'
		}, {
			xtype : 'radiogroup',
			style : 'padding-left: 0px',// TODO
			id : 'ordermethod',
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
				boxLabel : '查看工装',
				name : 'method',
				inputValue : 'tooling'

			}, {
				boxLabel : '完成订单',
				name : 'method',
				inputValue : 'finish'
			} ],
			listeners : {
				change : function(radiogroup, checkedradio) {

					var textfieldid = Ext.getCmp("order-form").getForm().findField('orderid');
					var textfieldtoolingid = Ext.getCmp("order-form").getForm().findField('ordertoolingid');
					var textfieldtitle = Ext.getCmp("order-form").getForm().findField('ordertitle');
					var textfieldamount = Ext.getCmp("order-form").getForm().findField('orderamount');
					var textfieldprice = Ext.getCmp("order-form").getForm().findField('orderprice');
					var textfielddate = Ext.getCmp("order-form").getForm().findField('orderdate');

					switch (checkedradio.inputValue) {
					case 'add':
						textfieldid.reset();
						textfieldid.enable();
						textfieldtoolingid.reset();
						textfieldtoolingid.enable();
						textfieldtitle.enable();
						textfieldamount.enable();
						textfieldprice.enable();
						textfielddate.enable();
						break;
					case 'modify':
						textfieldid.enable();
						textfieldtoolingid.enable();
						textfieldtitle.enable();
						textfieldamount.enable();
						textfieldprice.enable();
						textfielddate.enable();
						break;
					case 'delete':
						textfieldid.enable();
						textfieldtoolingid.reset();
						textfieldtoolingid.disable();
						textfieldtitle.reset();
						textfieldtitle.disable();
						textfieldamount.reset();
						textfieldamount.disable();
						textfieldprice.reset();
						textfieldprice.disable();
						textfielddate.reset();
						textfielddate.disable();
						break;
					case 'find':
						textfieldid.enable();
						textfieldtoolingid.enable();
						textfieldtitle.enable();
						textfieldamount.reset();
						textfieldamount.disable();
						textfieldprice.disable();
						textfieldprice.reset();
						textfielddate.enable();
						break;
					case 'tooling':
						textfieldid.enable();
						textfieldtoolingid.enable();
						textfieldtitle.reset();
						textfieldtitle.disable();
						textfieldamount.reset();
						textfieldamount.disable();
						textfieldprice.reset();
						textfieldprice.disable();
						textfielddate.reset();
						textfielddate.disable();
						break;
					case 'finish':
						textfieldid.enable();
						textfieldtoolingid.reset();
						textfieldtoolingid.disable();
						textfieldtitle.reset();
						textfieldtitle.disable();
						textfieldamount.reset();
						textfieldamount.disable();
						textfieldprice.reset();
						textfieldprice.disable();
						textfielddate.reset();
						textfielddate.disable();
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
			handler : submitOrder
		}, {
			text : '清空',
			width : 55,
			handler : resetOrder
		}, {
			text : '刷新列表',
			width : 55,
			handler : refreshOrder
		} ]
	} ]
});

function submitOrder() {

	if (!gridFormOrder.getForm().isValid())
		return;

	var textfieldid = Ext.getCmp("order-form").getForm().findField('orderid').getValue();
	var textfieldtoolingid = Ext.getCmp("order-form").getForm().findField('ordertoolingid').getValue();
	var textfieldtitle = Ext.getCmp("order-form").getForm().findField('ordertitle').getValue();
	var textfieldamount = Ext.getCmp("order-form").getForm().findField('orderamount').getValue();
	var textfieldprice = Ext.getCmp("order-form").getForm().findField('orderprice').getValue();
	var textfielddate = Ext.getCmp("order-form").getForm().findField('orderdate').getValue();

	var method = gridFormOrder.getForm().findField('ordermethod').getValue().inputValue;

	switch (method) {
	case 'add':
		if (textfieldid != '') {
			Ext.Msg.alert('提示', '添加订单不能指定编号');
			return;
		}
		if (textfieldtitle == '' || textfieldtoolingid == '' || textfieldamount == '' || textfielddate == ''
				|| textfieldprice == '') {
			Ext.Msg.alert('提示', '添加订单必须填写标题、工装编号，数量，单价和完成日期');
			return;
		}
		break;
	case 'modify':
		if (textfieldid = '' || textfieldtitle == '' || textfieldtoolingid == '' || textfieldamount == ''
				|| textfielddate == '' || textfieldprice == '') {
			Ext.Msg.alert('提示', '修改订单必须填写订单编号、标题、工装编号，生产数量和完成日期');
			return;
		}
		break;
	case 'delete':
		if (textfieldid == '') {
			Ext.Msg.alert('提示', '删除订单时编号不能为空');
			return;
		}
		break;
	case 'finish':
		if (textfieldid == '') {
			Ext.Msg.alert('提示', '完成订单时编号不能为空');
			return;
		}
		break;
	case 'find':
		if (textfieldid == '' && textfieldtoolingid == '' && textfieldtitle == '' && textfielddate == '') {
			Ext.Msg.alert('提示', '搜索订单时请指定订单编号、工装编号、标题或者时间');
			return;
		}

		url = urlOrder + "?method=find&id=" + textfieldid + "&toolingid=" + textfieldtoolingid + "&title="
				+ textfieldtitle + "&date=" + textfielddate.format('Y-m-d');
		ajaxGetText(url, bindOrderXML);
		return;
		break;
	case 'tooling':
		if (textfieldtoolingid == '') {
			Ext.Msg.alert('提示', '查看工装文件时必须指定工装文件编号');
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

	gridFormOrder.getForm().submit({
		waitMsg : '正在提交数据，超时不动请刷新页面，或者重新登录',
		waitTitle : '提示',
		url : urlOrder,
		method : 'POST',
		success : function(form, action) {
			try {
				Ext.Msg.alert('提示', '保存成功');
				ajaxGetText(urlGetXmlOrder, bindOrderXML);
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

function resetOrder() {
	gridFormOrder.getForm().reset();
}

function refreshOrder() {
	ajaxGetText(urlGetXmlOrder, bindOrderXML);
}
