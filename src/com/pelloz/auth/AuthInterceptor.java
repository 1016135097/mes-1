package com.pelloz.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pelloz.po.UserInfo;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 判断handler是否能够转换为HandlerMethod，然后使用HandlerMethod的方法取得注解
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			AuthPassport authPassport = ((HandlerMethod) handler)
					.getMethodAnnotation(AuthPassport.class);

			// 没有声明需要权限,或者声明不验证权限就退出
			if (authPassport == null || authPassport.validate() == false) {
				return true;
			} else {
				// 实现权限验证逻辑
				response.setCharacterEncoding("utf-8");// 防止中文乱码
				response.setContentType("text/json");
				Boolean isAuthorised = false; //默认无权限
				String department;
				UserInfo userInfo = (UserInfo) request.getSession().getAttribute("user");
				if (userInfo != null) {
					department = userInfo.getDepartment();
				} else {
					response.getWriter().print("{ success: false, infos:{info: '没有权限，请使用正确用户登录'} }");
					return false;
				}
				
				for (int i = 0; i < authPassport.department().length; i++) {
					if(authPassport.department()[i].equals(department)){
						isAuthorised = true;
						break;
					}
				}

				if (isAuthorised)
					return true;
				else// 如果验证失败
				{
					response.getWriter().print("{ success: false, infos:{info: '没有权限，请使用正确用户登录'} }");
					return false;
				}
			}
		} else
			return true;
	}
}