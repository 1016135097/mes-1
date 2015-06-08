package com.pelloz.service;

import java.io.Serializable;
import java.util.List;

import com.pelloz.exception.NoSuchPOException;
import com.pelloz.po.UserInfo;

public interface UserService extends POService<UserInfo>{
	
	public void delete(Serializable id) throws NoSuchPOException;
	
	public List<UserInfo> find(String paramname, Object param) throws NoSuchPOException;

	public List<UserInfo> findLike(String paramname, String param) throws NoSuchPOException;

	public List<UserInfo> findAll() throws NoSuchPOException;
	
}
