package com.pelloz.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pelloz.auth.AuthPassport;
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
	public String add(String title, String content, HttpServletRequest req) {

		Pdoc pdoc = new Pdoc();
		UserInfo user = (UserInfo) req.getAttribute("user");
		if(user==null) {
			req.setAttribute("errmsg", "无法取得用户");
			return "err";
		}
		System.out.println(user.getUsername()); // TODO 删除
		pdoc.setTitle(title);
		pdoc.setContent(content);
		pdoc.setUserinfo(user);
		
		try {
			pdocService.add(pdoc);
			
			req.setAttribute("msgcode", 1);
			req.setAttribute("msg", "添加工艺文件成功");
		} catch (POExistException e) {
			req.setAttribute("msgcode", -1);
			req.setAttribute("msg", "添加工艺文件失败，有同名文件存在");
			
			return "forward:processdoc.jsp";
		} finally {
			req.setAttribute("pdoc", pdoc);
		}

		return "processdoc";
	}
	
	
	/*@AuthPassport(department = { "admin", "工艺室" })
	@RequestMapping(params = "method=add")
	public String add(String title, String content, ModelMap map) {

		Pdoc pdoc = new Pdoc();
		UserInfo user = (UserInfo) map.get("user");
		System.out.println(user.getUsername()); // TODO 删除
		pdoc.setTitle(title);
		pdoc.setContent(content);
		pdoc.setUserinfo(user);

		try {
			pdocService.add(pdoc);
			
			map.addAttribute("msgcode", 1);
			map.addAttribute("msg", "添加工艺文件成功");
		} catch (POExistException e) {
			map.addAttribute("msgcode", -1);
			map.addAttribute("msg", "添加工艺文件失败，有同名文件存在");
			
			return "forward:processdoc.jsp";
		} finally {
			map.addAttribute("pdoc", pdoc);
		}

		return "processdoc";
	}*/

}