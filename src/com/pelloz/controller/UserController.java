package com.pelloz.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
import com.pelloz.po.UserInfo;
import com.pelloz.properties.PropertyMgr;
import com.pelloz.service.UserService;

@Controller
@RequestMapping("/user.do")
public class UserController {

	@Resource
	private UserService userService;

	private static Properties props = new Properties();
	private static Boolean isauthed;

	static {
		try {
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("usermgr.properties"));
			String userMgrEnable = (String) props.get("userMgrEnable");
			if (userMgrEnable.equalsIgnoreCase("true")) {
				isauthed = true;
			}else {
				isauthed = false;
			}
		} catch (IOException e) {
			System.out.println("配置文件读取错误");
			e.printStackTrace();
		}
	}

	// 登录判断
	@RequestMapping(params = "method=login")
	public String login(String username, String password, HttpServletRequest req) {

		List<UserInfo> userinfos;
		UserInfo userinfo;
		try {
			userinfos = this.userService.find("username", username);
			userinfo = userinfos.get(0);
			if (userinfo.getPassword().equals(password)) {
				req.getSession().setAttribute("user", userinfo);

				return "redirect:main.jsp";
			} else {
				req.getSession().invalidate();
				return "redirect:index.jsp?loginmsg=-1";// 密码不正确
			}
		} catch (NoSuchPOException e) {
			req.getSession().invalidate();
			return "redirect:index.jsp?loginmsg=-2";// 账号不存在
		}
	}

	@AuthPassport(department = { "admin" })
	@RequestMapping(params = "method=add")
	public void add(String username, String password, String fullname, String department, String title,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		if (!isauthed) {
			resp.getWriter().print("{ success: false, infos:{info: '管理员限制，请联系管理员解除限制'} }");
			return;
		}

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			resp.getWriter().print("{ success: false, infos:{info: '请重新登录'} }");
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 验证数据
		if (username == "" || password == "" || fullname == "" || department == "" || title == "" || username == null
				|| password == null || fullname == null || department == null || title == null) {
			resp.getWriter().print("{ success: false, infos:{info: '提交内容不能为空'} }");
			return;
		}

		UserInfo useradd = new UserInfo();
		// 保存数据
		useradd.setUsername(username);
		useradd.setPassword(password);
		useradd.setFullname(fullname);
		useradd.setDepartment(department);
		useradd.setTitle(title);

		try {
			userService.add(useradd);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (POExistException e) {
			resp.getWriter().print("{ success: false, infos:{info: '用户名重复，添加失败！'} }");
			return;
		}
		return;
	}

	@RequestMapping(params = "method=getuserxmlstr")
	public void getUserInfoXMLStr(HttpServletRequest req, HttpServletResponse resp) throws NoSuchPOException {

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<UserInfo> users;
		UserInfo usererr;// 查找的用户不存在时返回的提示

		// 查找user
		try {
			users = this.userService.findAll();
		} catch (NoSuchPOException e) {
			usererr = new UserInfo();
			usererr.setId(-1);
			usererr.setUsername("提示:现在系统中没有任何用户");
			usererr.setFullname("提示:现在系统中没有任何用户");
			usererr.setDepartment("提示:现在系统中没有任何用户");
			usererr.setTitle("提示:现在系统中没有任何用户");
			users = new ArrayList<UserInfo>();
			users.add(usererr);
		}

		// 返回XMLstr给客户端
		returnXML(users, resp);

		return;
	}

	@RequestMapping(params = "method=find")
	public void find(Integer id, String username, String fullname, String department, String title,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// 取得操作用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		List<UserInfo> users = new ArrayList<UserInfo>();
		UserInfo usererr = new UserInfo();// 查找的文件不存在时返回的提示
		usererr.setId(-1);
		usererr.setUsername("提示:找不到符合条件的用户");
		usererr.setFullname("提示:找不到符合条件的用户");
		usererr.setDepartment("提示:找不到符合条件的用户");
		usererr.setTitle("提示:找不到符合条件的用户");

		// 查找id
		if (id != null) {
			try {
				user = this.userService.find(id);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				users.add(usererr);
				returnXML(users, resp);
				return;
			}
			// 将找到的user返回
			users.add(user);
			returnXML(users, resp);
			return;
		}

		// 查找username
		if (username != "" && username != null) {
			try {
				users = this.userService.findLike("username", username);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				users.add(usererr);
				returnXML(users, resp);
				return;
			}
			// 将找到的users返回
			returnXML(users, resp);
			return;
		}

		// 查找fullname
		if (fullname != "" && fullname != null) {
			try {
				users = this.userService.findLike("fullname", fullname);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				users.add(usererr);
				returnXML(users, resp);
				return;
			}
			// 将找到的users返回
			returnXML(users, resp);
			return;
		}

		// 查找department
		if (department != "" && department != null) {
			try {
				users = this.userService.findLike("department", department);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				users.add(usererr);
				returnXML(users, resp);
				return;
			}
			// 将找到的users返回
			returnXML(users, resp);
			return;
		}

		// 查找title
		if (title != "" && title != null) {
			try {
				users = this.userService.findLike("title", title);
			} catch (NoSuchPOException e) {
				// 返回错误用户提示
				users.add(usererr);
				returnXML(users, resp);
				return;
			}
			// 将找到的users返回
			returnXML(users, resp);
			return;
		}

		// 最后都找不到
		users.add(usererr);
		returnXML(users, resp);
		return;
	}

	@AuthPassport(department = { "admin" })
	@RequestMapping(params = "method=modify")
	public void modify(Integer id, String username, String password, String fullname, String department, String title,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		if (!isauthed) {
			resp.getWriter().print("{ success: false, infos:{info: '管理员限制，请联系管理员解除限制'} }");
			return;
		}

		// 验证数据
		if (id == null || username == "" || password == "" || fullname == "" || department == "" || title == ""
				|| username == null || password == null || fullname == null || department == null || title == null) {
			resp.getWriter().print("{ success: false, infos:{info: '提交内容不能为空'} }");
			return;
		}

		// 取得操作的用户
		UserInfo user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;// 已经在权限检查中保证用户的存在，这里应该不会执行
		}

		// 取得id对应的user，修改后保存
		UserInfo usermodify = new UserInfo();
		try {
			usermodify.setId(id);
			usermodify.setUsername(username);
			usermodify.setPassword(password);
			usermodify.setFullname(fullname);
			usermodify.setDepartment(department);
			usermodify.setTitle(title);

			this.userService.modify(usermodify);// 必须保证传入的user不是持久化状态的

		} catch (NoSuchPOException e) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的用户编号不存在'} }");
			return;
		} catch (POExistException e) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的用户名重复'} }");
			return;
		}

		resp.getWriter().print("{ success: true, infos:{id: " + user.getId() + "} }");
		return;

	}

	@AuthPassport(department = { "admin" })
	@RequestMapping(params = "method=delete")
	public void delete(Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setCharacterEncoding("utf-8");// 防止中文乱码
		resp.setContentType("text/json");

		if (!isauthed) {
			resp.getWriter().print("{ success: false, infos:{info: '管理员限制，请联系管理员解除限制'} }");
			return;
		}

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

		// 取得id对应的user
		UserInfo userdelete;
		try {
			userdelete = this.userService.find(id);
			// 删除工装
			this.userService.delete(userdelete);
			resp.getWriter().print("{ success: true, infos:{} }");
		} catch (NoSuchPOException | ObjectNotFoundException e1) {
			resp.getWriter().print("{ success: false, infos:{info: '提交的工装编号不存在'} }");
			return;
		}

		return;
	}

	/**
	 * 将users转化为XMLstr，然后使用HttpServletResponse返回给客户端
	 * 
	 * @param users
	 * @param resp
	 */
	private void returnXML(List<UserInfo> users, HttpServletResponse resp) {
		// 生成XML
		Document document = DocumentHelper.createDocument();
		Element rootusers = document.addElement("users");
		for (UserInfo user : users) {
			Element rootuser = rootusers.addElement("user");
			rootuser.addElement("id").addText(String.valueOf(user.getId()));
			rootuser.addElement("username").addText(user.getUsername());
			rootuser.addElement("fullname").addText(user.getFullname());
			rootuser.addElement("department").addText(user.getDepartment());
			rootuser.addElement("title").addText(user.getTitle());

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
