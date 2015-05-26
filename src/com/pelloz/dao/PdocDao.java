package com.pelloz.dao;

import java.util.List;

import com.pelloz.po.Pdoc;

public interface PdocDao extends PODao<Pdoc>{
	
	public List<Pdoc> find(String param, String paramname);
	
}
