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
		userDao.add(user);

		List<UserInfo> users = this.userDao
				.find("username", user.getUsername());
		if (users.size() > 0) {
			throw new POExistException("用户名有重复:" + user.getTitle());
		}
		this.userDao.add(user);

	}

	@Override
	public void add(String username, String password, String fullname,
			String department, String title) throws POExistException {
		List<UserInfo> userInfos = this.userDao.find(username, "username");
		if (userInfos == null || userInfos.size() == 0) {
			UserInfo userInfo = new UserInfo();
			userInfo.setUsername(username);
			userInfo.setPassword(password);
			userInfo.setFullname(fullname);
			userInfo.setDepartment(department);
			userInfo.setTitle(title);
			userDao.add(userInfo);
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
	public void delete(UserInfo userInfo) throws NoSuchPOException {

		UserInfo userInfotmp = this.userDao.find(userInfo.getId());
		if (userInfotmp == null) {
			throw new NoSuchPOException("要删除的用户不存在,id = " + userInfo.getId());
		}
		this.userDao.delete(userInfo);

	}

	@Override
	public void modify(UserInfo userInfo) throws NoSuchPOException {
		UserInfo usertmp = this.userDao.find(userInfo.getId());
		if (usertmp == null) {
			throw new NoSuchPOException("修改的用户不存在，id = " + userInfo.getId());
		}
		this.userDao.modify(userInfo);

	}

	@Override
	public UserInfo find(Serializable id) throws NoSuchPOException {
		UserInfo userInfo = this.userDao.find(id);
		if (userInfo == null) {
			throw new NoSuchPOException("find user id = " + id);
		}
		return userInfo;
	}

	@Override
	public List<UserInfo> find(String param, String paramname)
			throws NoSuchPOException {
		List<UserInfo> userInfos = this.userDao.find(param, paramname);
		if (userInfos == null || userInfos.size() == 0) {
			throw new NoSuchPOException("find " + paramname + " = " + param);
		}
		return userInfos;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
