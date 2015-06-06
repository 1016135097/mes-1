package com.pelloz.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pelloz.auth.AuthPassport;
import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
import com.pelloz.po.OrderForm;
import com.pelloz.po.Tooling;
import com.pelloz.po.UserInfo;
import com.pelloz.service.ToolingService;
import com.pelloz.service.OrderFormService;
import com.pelloz.service.UserService;

/**
 * 订单操作类，通过order.do访问
 * 
 * @author zp
 *
 */
@Controller
@RequestMapping("/order.do")
public class OrderController {

	@Resource
	private UserService userService;

	@Resource
	private OrderFormService orderService;

	@Resource
	private ToolingService toolingService;

	private OrderForm errmsg;

	public OrderController() {
		super();
		errmsg = new OrderForm();
		UserInfo user = new UserInfo();
		Tooling tooling = new Tooling();
		Date date = new Date();
		user.setId(-1);
		user.setFullname("错误");
		tooling.setId(-1);
		errmsg.setId(-1);
		errmsg.setTitle("错误");
		errmsg.setAmount(-1);
		errmsg.setPrice(-1.0);
		errmsg.setUserinfo(user);
		errmsg.setTooling(tooling);
		errmsg.setDate(date);
	}

	@AuthPassport(department = { "admin", "采购部" })
	@RequestMapping(params = "method=add")
	public void add(Integer toolingid, String title, Integer amount, String date, Double price, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 验证数据
		if (toolingid == null || title == "" || title == null || amount == null || date == "" || date == null
				|| price == null) {
			resp.getWriter().print("{ success: false, infos:{info: '提交内容不能为空'} }");
			return;
		}

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			resp.getWriter().print("{ success: false, infos:{info: '请重新登录'} }");
			return;
		}

		// 取得tooling
		Tooling tooling;
		try {
			tooling = this.toolingService.find(toolingid);
		} catch (NoSuchPOException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的编号不存在'} }");
			return;
		}
		// 转化日期格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date enddate;
		try {
			enddate = dateFormat.parse(date);
		} catch (ParseException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '日期格式不正确'} }");
			return;
		}

		// 保存数据
		OrderForm order = new OrderForm();
		order.setTitle(title);
		order.setAmount(amount);
		order.setPrice(price);
		order.setDate(enddate);
		order.setTooling(tooling);
		order.setUserinfo(user);

		try {
			orderService.add(order);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (POExistException e) {
			resp.getWriter().print("{ success: false, infos:{info: '存在重复，添加订单失败！'} }");
			return;
		}
		return;
	}

	@RequestMapping(params = "method=getorderxmlstr")
	public void getOrderFormXMLStr(HttpServletRequest req, HttpServletResponse resp) {

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<OrderForm> orders;

		// 查找order
		try {
			orders = this.orderService.find("isComplete", false);
		} catch (NoSuchPOException e) {
			errmsg.setTitle("提示:没有未完成订单存在");
			orders = new ArrayList<OrderForm>();
			orders.add(errmsg);
		}

		// 返回XMLstr给客户端
		returnXML(orders, resp);
		return;
	}

	@RequestMapping(params = "method=find")
	public void find(Integer id, Integer toolingid, String title, String date, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		// 优先id，然后title，然后date

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<OrderForm> orders = new ArrayList<OrderForm>();
		OrderForm order = new OrderForm();
		errmsg.setTitle("提示:找不到符合条件的订单文件");

		// 查找id
		if (id != null) {
			try {
				order = this.orderService.find(id);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				orders.add(errmsg);
				returnXML(orders, resp);
				return;
			}
			// 将找到的order返回
			orders.add(order);
			returnXML(orders, resp);
			return;
		}

		// 查找toolingid
		if (toolingid != null) {
			try {
				Tooling tooling = toolingService.find(toolingid);
				orders = this.orderService.find("tooling", tooling);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				orders.add(errmsg);
				returnXML(orders, resp);
				return;
			}
			// 将找到的orders返回
			returnXML(orders, resp);
			return;
		}

		// 查找title
		if (title != "" && title != null) {
			try {
				orders = this.orderService.findLike("title", title);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				orders.add(errmsg);
				returnXML(orders, resp);
				return;
			}
			// 将找到的orders返回
			returnXML(orders, resp);
			return;
		}

		// 查找date以后的订单
		if (date != "" && date != null) {
			// 转化日期格式
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date begindate;
			try {
				begindate = dateFormat.parse(date);
			} catch (ParseException e1) {
				errmsg.setTitle("提示:日期格式不正确");
				orders.add(errmsg);
				returnXML(orders, resp);
				return;
			}

			try {
				orders = this.orderService.findFromDate(begindate, null);
			} catch (NoSuchPOException e2) {
				orders.add(errmsg);
				returnXML(orders, resp);
				return;
			}

			// 将找到的订单返回
			returnXML(orders, resp);
			return;
		}

		// 最后都找不到
		orders.add(errmsg);
		returnXML(orders, resp);
		return;
	}

	@AuthPassport(department = { "admin", "采购部" })
	@RequestMapping(params = "method=modify")
	public void modify(Integer id, String title, Integer toolingid, Integer amount, String date, Double price,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null || title == "" || title == null || toolingid == null || amount == null || date == ""
				|| date == null || price == null) {
			resp.getWriter().print("{ success: false, infos:{info: '更改订单文件时，必须填写所有内容'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		OrderForm order;
		try {
			// 取得id对应的order
			order = this.orderService.find(id);

			// 不是自己名下的订单不能修改，已经完成的订单不能修改
			if (user.getId() != order.getUserinfo().getId()) {
				resp.getWriter().print("{ success: false, infos:{info: '只能修改自己名下的订单'} }");
				return;
			} else if (order.isComplete()) {
				resp.getWriter().print("{ success: false, infos:{info: '已经完成的订单不能修改'} }");
				return;
			}
		} catch (NoSuchPOException e2) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的订单编号不存在'} }");
			return;
		}

		// 转化日期
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date enddate;
		try {
			enddate = dateFormat.parse(date);
		} catch (ParseException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的日期格式不正确'} }");
			return;
		}
		// 取得工装
		Tooling tooling;
		try {
			tooling = this.toolingService.find(toolingid);
		} catch (NoSuchPOException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工装编号不存在'} }");
			return;
		}

		// 修改后保存
		order.setTitle(title);
		order.setAmount(amount);
		order.setPrice(price);
		order.setDate(enddate);
		order.setUserinfo(user);
		order.setTooling(tooling);

		this.orderService.update(order);

		resp.getWriter().print("{ success: true, infos:{} }");
		return;

	}

	@AuthPassport(department = { "admin", "采购部" })
	@RequestMapping(params = "method=delete")
	public void delete(Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null) {
			resp.getWriter().print("{ success: false, infos:{info: '删除订单时，编号不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的order，判断作者是否是操作用户
		OrderForm order;
		try {
			order = this.orderService.find(id);

			// 不是自己名下的订单不能删除，生产中订单不能删除
			if (user.getId() != order.getUserinfo().getId()) {
				resp.getWriter().print("{ success: false, infos:{info: '只能删除自己名下的订单'} }");
				return;
			}
			// 删除订单
			order.setTooling(null);
			order.setUserinfo(null);
			this.orderService.update(order);
			this.orderService.delete(order);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的订单编号不存在'} }");
			return;
		}

		return;
	}

	@AuthPassport(department = { "admin", "采购部" })
	@RequestMapping(params = "method=finish")
	public void finish(Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null) {
			resp.getWriter().print("{ success: false, infos:{info: '完成订单时，编号不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的order，判断作者是否是操作用户
		OrderForm order;
		try {
			order = this.orderService.find(id);

			// 不是自己名下的订单不能修改
			if (order.getUserinfo() != null && user.getId() != order.getUserinfo().getId()) {
				resp.getWriter().print("{ success: false, infos:{info: '只能修改自己名下的订单'} }");
				return;
			}
			// 修改订单
			order.setComplete(true);
			this.orderService.update(order);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的订单编号不存在'} }");
			return;
		}

		return;
	}

	/**
	 * 将orders转化为XMLstr，然后使用HttpServletResponse返回给客户端
	 * 
	 * @param orders
	 * @param resp
	 */
	private void returnXML(List<OrderForm> orders, HttpServletResponse resp) {
		// 生成XML
		Document document = DocumentHelper.createDocument();
		Element rootorders = document.addElement("orders");
		for (OrderForm order : orders) {
			Element rootorder = rootorders.addElement("order");
			rootorder.addElement("id").addText(String.valueOf(order.getId()));
			rootorder.addElement("toolingid").addText(String.valueOf(order.getTooling().getId()));
			rootorder.addElement("title").addText(order.getTitle());
			rootorder.addElement("amount").addText(String.valueOf(order.getAmount()));
			rootorder.addElement("date").addText(String.valueOf(order.getDate()));
			rootorder.addElement("user").addText(order.getUserinfo().getFullname());
			rootorder.addElement("price").addText(String.valueOf(order.getPrice()));
			rootorder.addElement("totalprice").addText(String.valueOf(order.getPrice() * order.getAmount()));
			rootorder.addElement("complete").addText(String.valueOf(order.isComplete()));

		}

		// 返回xmlstr
		try {
			System.out.println(document.asXML());// TODO 删除
			resp.setCharacterEncoding("utf-8");// 防止中文乱码
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().print(document.asXML());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}