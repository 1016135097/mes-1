package com.pelloz.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.pelloz.po.UserInfo;
import com.pelloz.service.PdocService;
import com.pelloz.service.UserService;

/**
 * 工艺文件操作类，通过processdoc.do访问
 * 
 * @author zp
 *
 */
@Controller
@RequestMapping("/processdoc.do")
public class ProcessDocController {

	@Resource
	private UserService userService;

	@Resource
	private PdocService pdocService;

	@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=add")
	public void add(String title, String content, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			resp.getWriter().print("{ success: false, infos:{info: '请重新登录'} }");
			return;
		}
		
		//验证数据
		if(title==""||content==""||title==null||content==null){
			resp.getWriter().print("{ success: false, infos:{info: '提交内容不能为空'} }");
			return;
		}

		Pdoc pdoc = new Pdoc();
		// 保存数据
		pdoc.setTitle(title);
		pdoc.setContent(content);
		pdoc.setUserinfo(user);

		try {
			pdocService.add(pdoc);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (POExistException e) {
			resp.getWriter().print("{ success: false, infos:{info: '文件名重复，添加工艺文件失败！'} }");
			return;
		}
		return;
	}

	@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=getpdocxmlstr")
	public void getPdocXMLStr(HttpServletRequest req, HttpServletResponse resp) {

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<Pdoc> pdocs;
		Pdoc pdocerr;// 查找的文件不存在时返回的提示

		// 查找pdoc
		try {
			pdocs = this.pdocService.find("userinfo", user);
		} catch (NoSuchPOException e) {
			pdocerr = new Pdoc();
			pdocerr.setId(-1);
			pdocerr.setTitle("提示:");
			pdocerr.setContent("此用户名下没有工艺文件");
			pdocerr.setUserinfo(user);
			pdocs = new ArrayList<Pdoc>();
			pdocs.add(pdocerr);
		}

		// 返回XMLstr给客户端
		returnXML(pdocs, resp);

		return;
	}

	@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=find")
	public void find(Integer id, String title, String author, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// 优先id，然后title，然后author

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<Pdoc> pdocs = new ArrayList<Pdoc>();
		Pdoc pdoc = new Pdoc();
		Pdoc pdocerr = new Pdoc();// 查找的文件不存在时返回的提示
		pdocerr.setId(-1);
		pdocerr.setTitle("提示:");
		pdocerr.setContent("找不到符合条件的工艺文件");
		pdocerr.setUserinfo(user);

		// 查找id
		if (id != null) {
			try {
				pdoc = this.pdocService.find(id);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				pdocs.add(pdocerr);
				returnXML(pdocs, resp);
				return;
			}
			// 将找到的pdoc返回
			pdocs.add(pdoc);
			returnXML(pdocs, resp);
			return;
		}

		// 查找title
		if (title != "" && title != null) {
			try {
				pdocs = this.pdocService.findLike("title", title);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				pdocs.add(pdocerr);
				returnXML(pdocs, resp);
				return;
			}
			// 将找到的pdocs返回
			returnXML(pdocs, resp);
			return;
		}

		// 查找author
		if (author != "" && author != null) {
			// 先通过author查找符合条件的用户
			List<UserInfo> users = new ArrayList<UserInfo>();
			try {
				users = this.userService.findLike("fullname", author);

			} catch (NoSuchPOException e1) {
				// 返回错误用户提示
				pdocs.add(pdocerr);
				returnXML(pdocs, resp);
				return;
			}

			// 然后通过users查找pdocs
			for (UserInfo _user : users) {
				try {
					pdocs.addAll(this.pdocService.find("userinfo", _user));
				} catch (NoSuchPOException e) {
					// 找到的用户可能没有创建过工艺，直接继续查找
					continue;
				}
			}
			// 如果遍历完以后还是找不到pdocs就返回错误用户提示
			if (pdocs.size() == 0) {
				// 返回错误用户提示
				pdocs.add(pdocerr);
				returnXML(pdocs, resp);
				return;
			}
			// 将找到的pdocs返回
			returnXML(pdocs, resp);
			return;
		}
		
		//最后都找不到
		pdocs.add(pdocerr);
		returnXML(pdocs, resp);
		return;
	}

	@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=modify")
	public void modify(Integer id, String title, String content, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null || title == "" || title == null || content == "" || content == null) {
			resp.getWriter().print("{ success: false, infos:{info: '更改工艺文件时，编号、标题、内容都不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的pdoc，修改后保存
		Pdoc pdoc = new Pdoc();
		try {
			pdoc.setId(id);
			pdoc.setTitle(title);
			pdoc.setContent(content);
			pdoc.setUserinfo(user);

			this.pdocService.modify(pdoc);//必须保证传入的pdoc不是持久化状态的

		} catch (NoSuchPOException e) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工艺文件编号不存在'} }");
			return;
		} catch (POExistException e) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工艺文件标题有重复'} }");
			return;
		}
		
		resp.getWriter().print("{ success: true, infos:{} }");
		return;
		

	}

	@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=delete")
	public void delete(Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		// 判断参数不为空
		if (id == null) {
			resp.getWriter().print("{ success: false, infos:{info: '删除工艺文件时，编号不能为空'} }");
			return;// 前台页面应该已经判断，这里应该不会执行
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的pdoc，判断作者是否是操作用户
		Pdoc pdoc;
		try {
			pdoc = this.pdocService.find(id);

			// 不是自己名下的工艺文件不能删除
			if (user.getId() != pdoc.getUserinfo().getId()) {
				resp.getWriter().print("{ success: false, infos:{info: '只能删除自己名下的工艺文件'} }");
				return;
			}
			// 删除工艺文件
			this.pdocService.delete(pdoc);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工艺文件编号不存在'} }");
			return;
		}

		return;
	}

	/**
	 * 将pdocs转化为XMLstr，然后使用HttpServletResponse返回给客户端
	 * 
	 * @param pdocs
	 * @param resp
	 */
	private void returnXML(List<Pdoc> pdocs, HttpServletResponse resp) {
		// 生成XML
		Document document = DocumentHelper.createDocument();
		Element rootpdocs = document.addElement("pdocs");
		for (Pdoc pdoc : pdocs) {
			Element rootpdoc = rootpdocs.addElement("pdoc");
			rootpdoc.addElement("id").addText(String.valueOf(pdoc.getId()));//TODO 我觉得还是应该改到id
			rootpdoc.addElement("title").addText(pdoc.getTitle());//TODO 因为已经在上一级确定是属于pdoc了
			rootpdoc.addElement("content").addText(pdoc.getContent());
			rootpdoc.addElement("author").addText(pdoc.getUserinfo().getFullname());

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