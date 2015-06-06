package com.pelloz.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.pelloz.service.UserService;

/**
 * 工装操作类，通过processdoc.do访问
 * 
 * @author zp
 *
 */
@Controller
@RequestMapping("/tooling.do")
public class ToolingController {

	@Resource
	private UserService userService;

	@Resource
	private ToolingService toolingService;

	@AuthPassport(department = { "admin", "工装室", "工艺室" })
	@RequestMapping(params = "method=add")
	public void add(String name, Integer amount, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			resp.getWriter().print("{ success: false, infos:{info: '请重新登录'} }");
			return;
		}

		// 验证数据
		if (name == "" || name == null || amount == null) {
			resp.getWriter().print("{ success: false, infos:{info: '提交内容不能为空'} }");
			return;
		}

		// 工艺室没有权限修改数量
		if (user.getDepartment().equals("工艺室")) {
			amount = 0;
		}

		Tooling tooling = new Tooling();
		// 保存数据
		tooling.setName(name);
		tooling.setAmount(amount);

		try {
			toolingService.add(tooling);
			resp.getWriter().print("{ success: true, infos:{id: " + tooling.getId() + "} }");
		} catch (POExistException e) {
			resp.getWriter().print("{ success: false, infos:{info: '工装名称重复，添加工装失败！'} }");
			return;
		}
		return;
	}

	@RequestMapping(params = "method=gettoolingxmlstr")
	public void getToolingXMLStr(HttpServletRequest req, HttpServletResponse resp) throws NoSuchPOException {

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<Tooling> toolings;
		Tooling toolingerr;// 查找的工装不存在时返回的提示

		// 查找tooling
		try {
			toolings = this.toolingService.findAll();
		} catch (NoSuchPOException e) {
			toolingerr = new Tooling();
			toolingerr.setId(-1);
			toolingerr.setName("提示:现在系统中没有任何工装");
			toolingerr.setAmount(-1);
			toolings = new ArrayList<Tooling>();
			toolings.add(toolingerr);
		}

		// 返回XMLstr给客户端
		returnXML(toolings, resp);

		return;
	}

	@RequestMapping(params = "method=find")
	public void find(Integer id, String name, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 优先id，然后name

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<Tooling> toolings = new ArrayList<Tooling>();
		Tooling tooling = new Tooling();
		Tooling toolingerr = new Tooling();// 查找的文件不存在时返回的提示
		toolingerr.setId(-1);
		toolingerr.setName("提示:找不到符合条件的工装");
		toolingerr.setAmount(-1);

		// 查找id
		if (id != null) {
			try {
				tooling = this.toolingService.find(id);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				toolings.add(toolingerr);
				returnXML(toolings, resp);
				return;
			}
			// 将找到的tooling返回
			toolings.add(tooling);
			returnXML(toolings, resp);
			return;
		}

		// 查找name
		if (name != "" && name != null) {
			try {
				toolings = this.toolingService.findLike("name", name);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				toolings.add(toolingerr);
				returnXML(toolings, resp);
				return;
			}
			// 将找到的toolings返回
			returnXML(toolings, resp);
			return;
		}

		// 最后都找不到
		toolings.add(toolingerr);
		returnXML(toolings, resp);
		return;
	}

	@AuthPassport(department = { "admin", "工装室", "工艺室" })
	@RequestMapping(params = "method=modify")
	public void modify(Integer id, String name, Integer amount, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null || name == "" || name == null || amount == null) {
			resp.getWriter().print("{ success: false, infos:{info: '更改工装时，编号、名称、内容都不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 工艺室没有权限修改数量
		if (user.getDepartment().equals("工艺室")) {
			amount = 0;
		}

		// 取得id对应的tooling，修改后保存
		Tooling tooling = new Tooling();
		try {
			tooling.setId(id);
			tooling.setName(name);
			tooling.setAmount(amount);

			this.toolingService.modify(tooling);// 必须保证传入的tooling不是持久化状态的

		} catch (NoSuchPOException e) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工装编号不存在'} }");
			return;
		} catch (POExistException e) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工装名称有重复'} }");
			return;
		}

		resp.getWriter().print("{ success: true, infos:{id: " + tooling.getId() + "} }");
		return;

	}

	@AuthPassport(department = { "admin", "工装室" })
	@RequestMapping(params = "method=delete")
	public void delete(Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null) {
			resp.getWriter().print("{ success: false, infos:{info: '删除工装时，编号不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的tooling
		Tooling tooling;
		try {
			tooling = this.toolingService.find(id);
			// 删除工装
			this.toolingService.delete(tooling);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工装编号不存在'} }");
			return;
		}

		return;
	}

	@AuthPassport(department = { "admin", "工装室" })
	@RequestMapping(params = "method=out")
	public void out(Integer id, Integer opernum, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null || opernum == null) {
			resp.getWriter().print("{ success: false, infos:{info: '借出工装时，编号和操作数量不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的tooling
		Tooling tooling;
		try {
			tooling = this.toolingService.find(id);
			if (tooling.getAmount() - opernum < 0) {
				resp.getWriter().print("{ success: false, infos:{info: '工装数量不够，无法借出'} }");
				return;
			}
			tooling.setAmount(tooling.getAmount() - opernum);
			this.toolingService.update(tooling);
			resp.getWriter().print("{ success: true, infos:{id: " + tooling.getId() + "} }");
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工装编号不存在'} }");
			return;
		}

		return;
	}

	@AuthPassport(department = { "admin", "工装室" })
	@RequestMapping(params = "method=in")
	public void in(Integer id, Integer opernum, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null || opernum == null) {
			resp.getWriter().print("{ success: false, infos:{info: '借出工装时，编号和操作数量不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的tooling
		Tooling tooling;
		try {
			tooling = this.toolingService.find(id);
			tooling.setAmount(tooling.getAmount() + opernum);
			this.toolingService.update(tooling);
			resp.getWriter().print("{ success: true, infos:{id: " + tooling.getId() + "} }");
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工装编号不存在'} }");
			return;
		}

		return;
	}

	@RequestMapping(params = "method=purchase")
	public void purchase(Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null) {
			resp.getWriter().print("{ success: false, infos:{info: '必须指定工装编号'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的tooling
		Tooling tooling;
		try {
			tooling = this.toolingService.find(id);
			if (user.getDepartment().equals("采购部")) {
				tooling.setNeedPurchase(!tooling.isNeedPurchase());
			} else {
				tooling.setNeedPurchase(true);
			}
			this.toolingService.update(tooling);
			resp.getWriter().print("{ success: true, infos:{id: " + tooling.getId() + "} }");
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工装编号不存在'} }");
			return;
		}

		return;
	}

	/**
	 * 将toolings转化为XMLstr，然后使用HttpServletResponse返回给客户端
	 * 
	 * @param toolings
	 * @param resp
	 */
	private void returnXML(List<Tooling> toolings, HttpServletResponse resp) {
		// 生成XML
		Document document = DocumentHelper.createDocument();
		Element roottoolings = document.addElement("toolings");
		for (Tooling tooling : toolings) {
			Element roottooling = roottoolings.addElement("tooling");
			roottooling.addElement("id").addText(String.valueOf(tooling.getId()));
			roottooling.addElement("name").addText(tooling.getName());
			roottooling.addElement("amount").addText(String.valueOf(tooling.getAmount()));
			roottooling.addElement("needpurchase").addText(String.valueOf(tooling.isNeedPurchase()));

			int orderamount = 0;
			Set<OrderForm> orders = tooling.getOrderforms();
			if (orders == null || orders.size() == 0) {
				roottooling.addElement("numonpurchase").addText("0");
			} else {
				for (OrderForm orderForm : orders) {
					if (!orderForm.isComplete()) {
						orderamount += orderForm.getAmount();
					}
				}
				roottooling.addElement("numonpurchase").addText(String.valueOf(orderamount));
			}
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