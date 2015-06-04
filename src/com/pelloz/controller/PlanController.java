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
import com.pelloz.po.Pdoc;
import com.pelloz.po.Plan;
import com.pelloz.po.UserInfo;
import com.pelloz.service.PdocService;
import com.pelloz.service.PlanService;
import com.pelloz.service.UserService;

/**
 * 计划操作类，通过plan.do访问
 * 
 * @author zp
 *
 */
@Controller
@RequestMapping("/plan.do")
public class PlanController {

	@Resource
	private UserService userService;

	@Resource
	private PlanService planService;

	@Resource
	private PdocService pdocService;

	private Plan planerr;

	public PlanController() {
		super();
		planerr = new Plan();
		UserInfo user = new UserInfo();
		Pdoc pdoc = new Pdoc();
		Date date = new Date();
		user.setId(-1);
		user.setFullname("错误");
		pdoc.setId(-1);
		planerr.setId(-1);
		planerr.setNum(-1);
		planerr.setUserinfo(user);
		planerr.setPdoc(pdoc);
		;
		planerr.setEndDate(date);
		planerr.setTitle("错误");
	}

	@AuthPassport(department = { "admin", "计划室" })
	@RequestMapping(params = "method=add")
	public void add(Integer pdocid, String title, Integer num, String enddate, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 验证数据
		if (pdocid == null || title == "" || title == null || num == null || enddate == "" || enddate == null) {
			resp.getWriter().print("{ success: false, infos:{info: '提交内容不能为空'} }");
			return;
		}

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			resp.getWriter().print("{ success: false, infos:{info: '请重新登录'} }");
			return;
		}

		// 取得pdoc
		Pdoc pdoc;
		try {
			pdoc = this.pdocService.find(pdocid);
		} catch (NoSuchPOException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的计划编号不存在'} }");
			return;
		}
		// 转化日期格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = dateFormat.parse(enddate);
		} catch (ParseException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '日期格式不正确'} }");
			return;
		}

		// 保存数据
		Plan plan = new Plan();
		plan.setTitle(title);
		plan.setNum(num);
		plan.setEndDate(date);
		plan.setPdoc(pdoc);
		plan.setUserinfo(user);

		try {
			planService.add(plan);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (POExistException e) {
			resp.getWriter().print("{ success: false, infos:{info: '存在重复，添加计划文件失败！'} }");
			return;
		}
		return;
	}

	@AuthPassport(department = { "admin", "计划室" })
	@RequestMapping(params = "method=getplanxmlstr")
	public void getPlanXMLStr(HttpServletRequest req, HttpServletResponse resp) {

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<Plan> plans;

		// 查找plan
		try {
			plans = this.planService.find("userinfo", user);
		} catch (NoSuchPOException e) {
			planerr.setTitle("提示:此用户下没有计划存在");
			plans = new ArrayList<Plan>();
			plans.add(planerr);
		}

		// 返回XMLstr给客户端
		returnXML(plans, resp);
		return;
	}

	@AuthPassport(department = { "admin", "计划室" })
	@RequestMapping(params = "method=find")
	public void find(Integer id, String title, String enddate, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// 优先id，然后title，然后enddate

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<Plan> plans = new ArrayList<Plan>();
		Plan plan = new Plan();
		planerr.setTitle("提示:找不到符合条件的计划文件");

		// 查找id
		if (id != null) {
			try {
				plan = this.planService.find(id);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				plans.add(planerr);
				returnXML(plans, resp);
				return;
			}
			// 将找到的plan返回
			plans.add(plan);
			returnXML(plans, resp);
			return;
		}

		// 查找title
		if (title != "" && title != null) {
			try {
				plans = this.planService.findLike("title", title);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				plans.add(planerr);
				returnXML(plans, resp);
				return;
			}
			// 将找到的plans返回
			returnXML(plans, resp);
			return;
		}

		// 查找date以后的计划
		if (enddate != "" && enddate != null) {
			// 转化日期格式
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = dateFormat.parse(enddate);
			} catch (ParseException e1) {
				planerr.setTitle("提示:日期格式不正确");
				plans.add(planerr);
				returnXML(plans, resp);
				return;
			}

			try {
				plans = this.planService.findFromDate(date, null);
			} catch (NoSuchPOException e2) {
				plans.add(planerr);
				returnXML(plans, resp);
				return;
			}

			// 将找到的计划返回
			returnXML(plans, resp);
			return;
		}

		// 最后都找不到
		plans.add(planerr);
		returnXML(plans, resp);
		return;
	}

	@AuthPassport(department = { "admin", "计划室" })
	@RequestMapping(params = "method=modify")
	public void modify(Integer id, String title, Integer pdocid, Integer num, String enddate, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null || title == "" || title == null || pdocid == null || num == null || enddate == ""
				|| enddate == null) {
			resp.getWriter().print("{ success: false, infos:{info: '更改计划文件时，必须填写所有内容'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}
		// 转化日期
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = dateFormat.parse(enddate);
		} catch (ParseException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的日期格式不正确'} }");
			return;
		}
		// 取得计划
		Pdoc pdoc;
		try {
			pdoc = this.pdocService.find(pdocid);
		} catch (NoSuchPOException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的计划编号不存在'} }");
			return;
		}

		// 取得id对应的plan，修改后保存
		Plan plan = new Plan();
		try {
			plan.setId(id);
			plan.setTitle(title);
			plan.setNum(num);
			plan.setEndDate(date);
			plan.setUserinfo(user);
			plan.setPdoc(pdoc);

			this.planService.modify(plan);// 必须保证传入的plan不是持久化状态的

		} catch (NoSuchPOException e) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的计划编号不存在'} }");
			return;
		} catch (POExistException e) {
			resp.getWriter().print("{ success: false, infos:{info: ''} }"); // TODO
			return;
		}

		resp.getWriter().print("{ success: true, infos:{} }");
		return;

	}

	@AuthPassport(department = { "admin", "计划室" })
	@RequestMapping(params = "method=delete")
	public void delete(Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null) {
			resp.getWriter().print("{ success: false, infos:{info: '删除计划时，编号不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的plan，判断作者是否是操作用户
		Plan plan;
		try {
			plan = this.planService.find(id);

			// 不是自己名下的计划不能删除
			if (user.getId() != plan.getUserinfo().getId()) {
				resp.getWriter().print("{ success: false, infos:{info: '只能删除自己名下的计划'} }");
				return;
			}
			// 删除计划
			this.planService.delete(plan);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的计划编号不存在'} }");
			return;
		}

		return;
	}

	@AuthPassport(department = { "admin", "计划室" })
	@RequestMapping(params = "method=onplan")
	public void onPlan(Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// 判断参数不为空
		if (id == null) {
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的plan，判断作者是否是操作用户
		Plan plan;
		List<Plan> plans = new ArrayList<Plan>();
		;
		try {
			plan = this.planService.find(id);

			// 不是自己名下的计划不能上线
			if (user.getId() != plan.getUserinfo().getId()) {
				planerr.setTitle("提示:只能上线自己名下的计划");
				plans.add(planerr);
				returnXML(plans, resp);
				return;
			}
			// 上线计划
			plan.setOnPlan(true);
			this.planService.update(plan);
			plans.add(plan);
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			planerr.setTitle("提示:此用户下没有计划存在");
			plans.add(planerr);
		}

		returnXML(plans, resp);
		return;
	}

	/**
	 * 将plans转化为XMLstr，然后使用HttpServletResponse返回给客户端
	 * 
	 * @param plans
	 * @param resp
	 */
	private void returnXML(List<Plan> plans, HttpServletResponse resp) {
		// 生成XML
		Document document = DocumentHelper.createDocument();
		Element rootplans = document.addElement("plans");
		for (Plan plan : plans) {
			Element rootplan = rootplans.addElement("plan");
			rootplan.addElement("id").addText(String.valueOf(plan.getId()));
			rootplan.addElement("pdocid").addText(String.valueOf(plan.getPdoc().getId()));
			rootplan.addElement("title").addText(plan.getTitle());
			rootplan.addElement("num").addText(String.valueOf(plan.getNum()));
			rootplan.addElement("enddate").addText(String.valueOf(plan.getEndDate()));
			rootplan.addElement("planner").addText(plan.getUserinfo().getFullname());
			rootplan.addElement("onplan").addText(String.valueOf(plan.isOnPlan()));
			rootplan.addElement("onproducting").addText(String.valueOf(plan.isOnProducting()));

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