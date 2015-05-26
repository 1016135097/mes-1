package com.pelloz.dao;

import java.util.List;

import com.pelloz.po.UserInfo;

public interface UserDao extends PODao<UserInfo>{
	
	public List<UserInfo> find(String param, String paramname);
	
}
