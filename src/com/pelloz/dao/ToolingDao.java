package com.pelloz.dao;

import java.util.List;

import com.pelloz.po.Tooling;

public interface ToolingDao extends PODao<Tooling>{
	
	public List<Tooling> find(String paramname, Object param);

	public List<Tooling> findLike(String paramname, String param);

	public List<Tooling> findAll();
	
}
