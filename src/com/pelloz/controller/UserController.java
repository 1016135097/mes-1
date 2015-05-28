package com.pelloz.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pelloz.auth.AuthPassport;
import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
import com.pelloz.po.UserInfo;
import com.pelloz.service.UserService;

@Controller
@RequestMapping("/user.do")
public class UserController {

	@Resource
	private UserService userService;

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
	public String add(String username, String password, String fullname, String department, String title, ModelMap map) {

		UserInfo userinfo = new UserInfo();
		userinfo.setUsername(username);
		userinfo.setPassword(password);
		userinfo.setFullname(fullname);
		userinfo.setDepartment(department);
		userinfo.setTitle(title);
		try {
			userService.add(userinfo);
		} catch (POExistException e) {
			map.clear();
			map.addAttribute("errmsg", e.getMessage());
			return "err/err.jsp";
		}
		map.clear();
		return "webpage/system.jsp";
	}

	@RequestMapping(params = "method=deletebyid")
	public String delete(int id, HttpServletRequest req) {
		try {
			userService.delete(id);
		} catch (NoSuchPOException e) {
			req.setAttribute("errmsg", e.getMessage());
			return "err/err.jsp";
		}
		return "webpage/system.jsp";
	}

	@RequestMapping(params = "method=modify")
	public String modify(int id, String username, String password, String fullname, String department, String title,
			HttpServletRequest req) {
		
		UserInfo userinfo;
		try {
			userinfo = userService.find(id);
			userinfo.setUsername(username);
			userinfo.setPassword(password);
			userinfo.setFullname(fullname);
			userinfo.setDepartment(department);
			userinfo.setTitle(title);
			userService.modify(userinfo);
			
		} catch (NoSuchPOException e) {
			req.setAttribute("errmsg", e.getMessage());
			return "err/err.jsp";
		} catch (POExistException e) {
			req.setAttribute("errmsg", e.getMessage());
			return "err/err.jsp";
		}
		
		return "webpage/system.jsp";
	}

	@RequestMapping(params = { "method=find", "by=id" })
	public String find(int param, HttpServletRequest req) {
		UserInfo userinfo;
		try {
			userinfo = this.userService.find(param);

		} catch (NoSuchPOException e) {
			req.getSession().setAttribute("errmsg", e.getMessage());

			return "redirect:err/err.jsp";
		}

		req.setAttribute("user", userinfo);
		return "webpage/system.jsp";
	}

	@RequestMapping(params = { "method=find", "by!=id" })
	public String find(String param, String by, HttpServletRequest req) {
		List<UserInfo> userinfos;
		try {
			userinfos = this.userService.find(by, param);

		} catch (NoSuchPOException e) {
			req.getSession().setAttribute("errmsg", e.getMessage());

			return "redirect:err/err.jsp";
		}

		req.setAttribute("users", userinfos);
		return "webpage/system.jsp";
	}

}

/*
 * @RequestMapping(params="method=reg4") public String reg4(ModelMap map) {
 * System.out.println("HelloController.handleRequest()"); return
 * "forward:index.jsp"; return "forward:user.do?method=reg5"; //转发 return
 * "redirect:user.do?method=reg5"; //重定向 return "redirect:http://www.baidu.com";
 * //重定向 }
 */
