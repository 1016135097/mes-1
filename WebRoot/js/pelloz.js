/* pelloz's js library*/

//指定elementid，调用showLocale(objD)显示日期时间
function tick(elementid) {
	var today;
	today = new Date();
	document.getElementById(elementid).innerHTML = showLocale(today);
	window.setTimeout(function() {
		tick(elementid)
	}, 1000);// 使用匿名函数回调，循环调用必须
}

function showLocale(objD) {
	var str, colorhead, colorfoot;
	var yy = objD.getYear();
	if (yy < 1900)
		yy = yy + 1900;
	var MM = objD.getMonth() + 1;
	if (MM < 10)
		MM = '0' + MM;
	var dd = objD.getDate();
	if (dd < 10)
		dd = '0' + dd;
	var hh = objD.getHours();
	if (hh < 10)
		hh = '0' + hh;
	var mm = objD.getMinutes();
	if (mm < 10)
		mm = '0' + mm;
	var ss = objD.getSeconds();
	if (ss < 10)
		ss = '0' + ss;
	var ww = objD.getDay();
	if (ww == 0)
		colorhead = "<font color=\"#FF0000\">";
	if (ww > 0 && ww < 6)
		colorhead = "<font color=\"#373737\">";
	if (ww == 6)
		colorhead = "<font color=\"#008000\">";
	if (ww == 0)
		ww = "星期日";
	if (ww == 1)
		ww = "星期一";
	if (ww == 2)
		ww = "星期二";
	if (ww == 3)
		ww = "星期三";
	if (ww == 4)
		ww = "星期四";
	if (ww == 5)
		ww = "星期五";
	if (ww == 6)
		ww = "星期六";
	colorfoot = "</font>"
	str = colorhead + yy + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + ss + "  " + ww + "&nbsp;&nbsp;"
			+ colorfoot;
	return (str);
}

// Ajax异步通讯
var xmlHttp = null;
// 接收普通文本地址 GET方式
function ajaxGetText(url, fun) {
	// url表示获取数据的地址
	// fun表示异步调用的函数，他需要接受返回的文本数据
	if (url.length == 0) {
		return;
	}
	try {// Firefox, Opera 8.0+, Safari, IE7
		xmlHttp = new XMLHttpRequest();
	} catch (e) {// Old IE
		try {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e) {
			alert("Your browser does not support XMLHTTP!");
			return;
		}
	}
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			fun(xmlHttp.responseText);
		}
	};
	xmlHttp.open("GET", url, true);
	xmlHttp.send(null);

}
// 接收XML地址 GET方式
function ajaxGetXML(url, fun) {
	// url表示获取数据的地址
	// fun表示异步调用的函数，他需要接受返回的XML数据
	if (url.length == 0) {
		return;
	}
	try {// Firefox, Opera 8.0+, Safari, IE7
		xmlHttp = new XMLHttpRequest();
	} catch (e) {// Old IE
		try {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e) {
			alert("Your browser does not support XMLHTTP!");
			return;
		}
	}
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			fun(xmlHttp.responseXML);// fun处理的直接就是xmlDoc，不需要转换
		}
	}
	xmlHttp.open("GET", url, true);
	xmlHttp.send();
}

// 接收普通文本地址 POST方式
function ajaxPostText(url, param, fun) {
	// url表示获取数据的地址
	// fun表示异步调用的函数，他需要接受返回的文本数据
	if (url.length == 0) {
		return;
	}
	try {// Firefox, Opera 8.0+, Safari, IE7
		xmlHttp = new XMLHttpRequest();
	} catch (e) {// Old IE
		try {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e) {
			alert("Your browser does not support XMLHTTP!");
			return;
		}
	}
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			fun(xmlHttp.responseText);
		}
	}
	xmlHttp.open("POST", url, true);
	xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlHttp.send(param);

}

// 将str形式的xml解析后返回，应该配合ajaxGetText使用
function loadXMLString(xmlstr) {
	try // Internet Explorer
	{
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = "false";
		xmlDoc.loadXML(xmlstr);
		return (xmlDoc);
	} catch (e) {
		try // Firefox, Mozilla, Opera, etc.
		{
			parser = new DOMParser();
			xmlDoc = parser.parseFromString(xmlstr, "text/xml");
			return (xmlDoc);
		} catch (e) {
			alert(e.message)
		}
	}
	return (null);
}

// 在IE下单击复制到剪贴板，仅在IE下有效
function copyText(text, id) { /* 仅在IE下有效 */
	window.clipboardData.setData("text", text);
	x = document.getElementById(id); // 查找元素
	x.innerHTML = "邮件地址已复制"; // 改变内容
	var t = setTimeout(function() {
		hide(id)
	}, 2000);
}
function hide(id) {
	y = document.getElementById(id); // 查找元素
	y.innerHTML = " "; // 改变内容
}
