package com.pelloz.service;

import java.io.Serializable;
import java.util.List;

import com.pelloz.exception.NoSuchPOException;
import com.pelloz.po.Tooling;

public interface ToolingService extends POService<Tooling>{

	public void delete(Serializable id) throws NoSuchPOException;
	
	public List<Tooling> find(String paramname, Object param) throws NoSuchPOException;

	public List<Tooling> findLike(String paramname, String param) throws NoSuchPOException;

	public List<Tooling> findAll() throws NoSuchPOException;
}
