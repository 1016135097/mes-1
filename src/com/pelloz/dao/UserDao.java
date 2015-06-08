package com.pelloz.dao;

import java.util.List;

import com.pelloz.po.UserInfo;

public interface UserDao extends PODao<UserInfo>{
	
	public List<UserInfo> find(String paramname, Object param);
	
	public List<UserInfo> findLike(String paramname, String param);

	public List<UserInfo> findAll();
	
}
