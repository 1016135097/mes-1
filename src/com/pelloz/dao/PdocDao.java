package com.pelloz.dao;

import java.util.List;

import com.pelloz.po.BOM;
import com.pelloz.po.Pdoc;

public interface PdocDao extends PODao<Pdoc>{
	
	public List<Pdoc> find(String paramname, Object param);

	public List<Pdoc> findLike(String paramname, String param);

	public void delete(BOM bom);
	
}
