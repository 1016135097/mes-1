package com.pelloz.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pelloz.auth.AuthPassport;
import com.pelloz.exception.NoSuchPOException;
import com.pelloz.po.BOM;
import com.pelloz.po.Pdoc;
import com.pelloz.po.Tooling;
import com.pelloz.po.UserInfo;
import com.pelloz.service.PdocService;
import com.pelloz.service.ToolingService;
import com.pelloz.service.UserService;

/**
 * BOM操作类，通过processdoc.do访问
 * 
 * @author zp
 *
 */
@Controller
@RequestMapping("/bom.do")
public class BOMController {

	@Resource
	private UserService userService;

	@Resource
	private PdocService pdocService;

	@Resource
	private ToolingService toolingService;

	@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=add")
	public void add(Integer pdocid, Integer toolingid, Integer amount, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			resp.getWriter().print("{ success: false, infos:{info: '请重新登录'} }");
			return;
		}

		// 验证数据
		if (pdocid == null || toolingid == null || amount == null) {
			resp.getWriter().print("{ success: false, infos:{info: '提交内容不能为空'} }");
			return;
		}

		Pdoc pdoc;
		Tooling tooling;
		BOM bom;

		try {
			pdoc = this.pdocService.find(pdocid);
			if (pdoc.getUserinfo().getId() != user.getId()) {
				resp.getWriter().print("{ success: false, infos:{info: '只能修改自己的工艺文件'} }");
				return;
			}
		} catch (NoSuchPOException e) {
			resp.getWriter().print("{ success: false, infos:{info: '找不到对应的工艺文件'} }");
			return;
		}

		try {
			tooling = this.toolingService.find(toolingid);
		} catch (NoSuchPOException e) {
			resp.getWriter().print("{ success: false, infos:{info: '找不到对应的工装'} }");
			return;
		}

		bom = new BOM();
		bom.setPdoc(pdoc);
		bom.setTooling(tooling);
		bom.setAmount(amount);

		pdoc.getBoms().add(bom);
		this.pdocService.update(pdoc);

		resp.getWriter().print("{ success: true, infos:{} }");
		return;
	}

	@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=getbomxmlstr")
	public void getBOMXMLStr(Integer pdocid, HttpServletRequest req, HttpServletResponse resp) {

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		Pdoc pdoc;
		Tooling tooling;
		BOM bom = new BOM();
		Set<BOM> boms;

		// 查找pdocid对应的pdoc
		try {
			pdoc = this.pdocService.find(pdocid);
		} catch (NoSuchPOException e) {
			// 传入错误提示
			tooling = new Tooling();
			tooling.setId(-1);
			tooling.setName("错误:找不到对应的pdoc，请联系管理员");
			bom.setTooling(tooling);
			boms = new HashSet<BOM>();
			boms.add(bom);
			returnXML(null, resp);
			return;
		}

		// 取出pdoc中的bom
		boms = pdoc.getBoms();

		if (boms == null || boms.size() == 0) {
			tooling = new Tooling();
			tooling.setId(-1);
			tooling.setName("错误:找不到对应的pdoc，请联系管理员");
			bom.setTooling(tooling);
			boms = new HashSet<BOM>();
			boms.add(bom);
		}

		// 返回XMLstr给客户端
		returnXML(boms, resp);

		return;
	}

	@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=modify")
	public void modify(Integer pdocid, Integer id, Integer amount, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (pdocid == null || id == null || amount == null) {
			resp.getWriter().print("{ success: false, infos:{info: '更改BOM时，编号、需求数量，对应的工艺文件不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的bom，修改后保存
		Pdoc pdoc;
		Set<BOM> boms;

		try {
			pdoc = this.pdocService.find(pdocid);
			if (pdoc.getUserinfo().getId() != user.getId()) {
				resp.getWriter().print("{ success: false, infos:{info: '只能修改自己的工艺文件'} }");
				return;
			}
		} catch (NoSuchPOException e) {
			resp.getWriter().print("{ success: false, infos:{info: '找不到对应的工艺文件'} }");
			return;
		}

		boms = pdoc.getBoms();
		for (BOM bomtmp : boms) {
			if (bomtmp.getId() == id) {
				bomtmp.setAmount(amount);
				this.pdocService.update(pdoc);
				resp.getWriter().print("{ success: true, infos:{} }");
				return;
			}
		}

		resp.getWriter().print("{ success: false, infos:{info: '找不到对应的BOM'} }");
		return;

	}

	@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=delete")
	public void delete(Integer pdocid, Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null || pdocid == null) {
			resp.getWriter().print("{ success: false, infos:{info: '删除BOM时，编号与对应的工艺文件不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的bom
		Pdoc pdoc;
		Set<BOM> boms;

		try {
			pdoc = this.pdocService.find(pdocid);
			if (pdoc.getUserinfo().getId() != user.getId()) {
				resp.getWriter().print("{ success: false, infos:{info: '只能修改自己的工艺文件'} }");
				return;
			}
		} catch (NoSuchPOException e) {
			resp.getWriter().print("{ success: false, infos:{info: '找不到对应的工艺文件'} }");
			return;
		}

		boms = pdoc.getBoms();
		Boolean hasRemoved = false;
		Iterator<BOM> iterator = boms.iterator();
		while (iterator.hasNext()) {
			BOM bomtmp = iterator.next();
			if (bomtmp.getId() == id) {
				bomtmp.setPdoc(null);
				bomtmp.setTooling(null);
				iterator.remove();
				this.pdocService.update(pdoc);
				pdocService.delete(bomtmp);
				hasRemoved = true;
				break;
			}
		}

		if (hasRemoved) {
			// this.pdocService.update(pdoc);
			resp.getWriter().print("{ success: true, infos:{} }");
			return;
		}

		resp.getWriter().print("{ success: false, infos:{info: '找不到对应的BOM'} }");
		return;
	}

	/**
	 * 将boms转化为XMLstr，然后使用HttpServletResponse返回给客户端
	 * 
	 * @param boms
	 * @param resp
	 */
	private void returnXML(Set<BOM> boms, HttpServletResponse resp) {
		// 生成XML
		Document document = DocumentHelper.createDocument();
		Element rootboms = document.addElement("boms");
		for (BOM bom : boms) {
			Element rootbom = rootboms.addElement("bom");
			rootbom.addElement("id").addText(String.valueOf(bom.getId()));
			rootbom.addElement("toolingid").addText(String.valueOf(bom.getTooling().getId()));
			rootbom.addElement("toolingname").addText(bom.getTooling().getName());
			rootbom.addElement("amount").addText(String.valueOf(bom.getAmount()));
			rootbom.addElement("inventory").addText(String.valueOf(bom.getTooling().getAmount()));

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