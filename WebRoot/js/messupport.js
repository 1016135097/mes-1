/* pelloz's mes support js library*/

/**
 * 在价格前面显示¥ 符号，只保留两位小数
 * @param val
 * @returns {String}
 */
function cnMoney(val) {
	return '<span>¥ ' + Ext.util.Format.number(val,'0.00') + '</span>';
}

function trueToGreen(val) {
	if (val == true) {
		return '<span style="color: green;">是</span>';
	}
	if (val == false) {
		return '<span style="color: red;">否</span>';
	}
	return '<span style="color: yellow;">错误</span>';
}

function changDateToRed(val) {
	var todate = new Date(); // 得到系统日期
	if (val.format('Y-m-d') < todate.format('Y-m-d')) {
		return '<span style="color: red;">' + val.format('Y-m-d') + '</span>';
	}
	return '<span >' + val.format('Y-m-d') + '</span>';
}

function trueToRed(val) {
	if (val == true) {
		return '<span style="color: red;">是</span>';
	}
	if (val == false) {
		return '<span style="color: green;">否</span>';
	}
	return '<span style="color: yellow;">错误</span>';
}

