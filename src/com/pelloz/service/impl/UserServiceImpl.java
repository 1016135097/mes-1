package com.pelloz.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.pelloz.dao.UserDao;
import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
import com.pelloz.po.UserInfo;
import com.pelloz.service.UserService;

/**
 * 用户CRUD服务类
 * 
 * @author zp
 *
 */
@Component
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;

	@Override
	public void add(UserInfo user) throws POExistException {

		List<UserInfo> users = this.userDao.find("username", user.getUsername());
		if (users != null && users.size() > 0) {
			throw new POExistException("用户名有重复:" + user.getUsername());
		}
		this.userDao.add(user);

	}

	@Override
	public void add(String username, String password, String fullname, String department, String title)
			throws POExistException {
		List<UserInfo> userinfos = this.userDao.find("username", username);
		if (userinfos == null || userinfos.size() == 0) {
			UserInfo userinfo = new UserInfo();
			userinfo.setUsername(username);
			userinfo.setPassword(password);
			userinfo.setFullname(fullname);
			userinfo.setDepartment(department);
			userinfo.setTitle(title);
			userDao.add(userinfo);
		} else {
			throw new POExistException();
		}

	}

	@Override
	public void delete(Serializable id) throws NoSuchPOException {

		UserInfo userInfotmp = this.userDao.find(id);
		if (userInfotmp == null) {
			throw new NoSuchPOException("要删除的用户不存在,id = " + id);
		}
		this.userDao.delete(userInfotmp);

	}

	@Override
	public void delete(UserInfo userinfo) throws NoSuchPOException {

		UserInfo userInfotmp = this.userDao.find(userinfo.getId());
		if (userInfotmp == null) {
			throw new NoSuchPOException("要删除的用户不存在,id = " + userinfo.getId());
		}
		this.userDao.delete(userinfo);

	}

	@Override
	public void modify(UserInfo userinfo) throws NoSuchPOException, POExistException {
		UserInfo usertmp = this.userDao.find(userinfo.getId());
		if (usertmp == null) {
			throw new NoSuchPOException("修改的用户不存在，id = " + userinfo.getId());
		}
		List<UserInfo> usertmps = this.userDao.find("username", userinfo.getUsername());
		if ((usertmps != null && usertmps.get(0).getId() == userinfo.getId()) || usertmps == null
				|| usertmps.size() == 0) {
			// 找到的usertmps和要存入的userinfo的ID相同，或者找不到usertmps，就可以修改
			this.userDao.modify(userinfo);
			return;
		} else {
			throw new POExistException("修改的工艺标题有重复，title = " + userinfo.getUsername());
		}

	}

	@Override
	public UserInfo find(Serializable id) throws NoSuchPOException {
		UserInfo userinfo = this.userDao.find(id);
		if (userinfo == null) {
			throw new NoSuchPOException("查找的用户不存在 id = " + id);
		}
		return userinfo;
	}

	@Override
	public List<UserInfo> find(String paramname, Object param) throws NoSuchPOException {
		List<UserInfo> userinfos = this.userDao.find(paramname, param);
		if (userinfos == null || userinfos.size() == 0) {
			throw new NoSuchPOException("查找的用户不存在:参数 " + paramname + " = " + param);
		}
		return userinfos;
	}

	@Override
	public List<UserInfo> findLike(String paramname, String param) throws NoSuchPOException {
		List<UserInfo> userinfos = this.userDao.findLike(paramname, param);
		if (userinfos == null || userinfos.size() == 0) {
			throw new NoSuchPOException("查找的用户不存在:参数 " + paramname + " = " + param);
		}
		return userinfos;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
