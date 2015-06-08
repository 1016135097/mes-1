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
		this.userDao.save(user);

	}

	@Override
	public void delete(Serializable id) throws NoSuchPOException {

		UserInfo userInfotmp = this.userDao.get(id);
		if (userInfotmp == null) {
			throw new NoSuchPOException("要删除的用户不存在,id = " + id);
		}
		this.userDao.delete(userInfotmp);

	}

	@Override
	public void delete(UserInfo userinfo) throws NoSuchPOException {

		UserInfo userInfotmp = this.userDao.get(userinfo.getId());
		if (userInfotmp == null) {
			throw new NoSuchPOException("要删除的用户不存在,id = " + userinfo.getId());
		}
		this.userDao.delete(userInfotmp);

	}

	@Override
	public void modify(UserInfo userinfo) throws NoSuchPOException, POExistException {
		
		// 必须保证传入的userinfo不是持久化状态的
		UserInfo usertmp = this.userDao.get(userinfo.getId());
		if (usertmp == null) {
			throw new NoSuchPOException("修改的用户不存在，id = " + userinfo.getId());
		}
		
		List<UserInfo> usertmps = this.userDao.find("username", userinfo.getUsername());

		if (usertmps.size() == 0) {
			this.userDao.merge(userinfo);
			return;
		} else if (usertmps.get(0).getId() == userinfo.getId()) {
			this.userDao.merge(userinfo);
			return;
		} else{
			throw new POExistException("修改的用户名有重复，username = " + userinfo.getUsername());
		}
		
	}

	@Override
	public UserInfo find(Serializable id) throws NoSuchPOException {
		
		UserInfo userinfo = this.userDao.get(id);
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
	
	@Override
	public List<UserInfo> findAll() throws NoSuchPOException {
		List<UserInfo> userinfos = this.userDao.findAll();
		if (userinfos == null || userinfos.size() == 0) {
			throw new NoSuchPOException("系统中现在没有任何工装");
		}
		return userinfos;
	}
	
	@Override
	public void update(UserInfo userinfo){
		this.userDao.update(userinfo);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
