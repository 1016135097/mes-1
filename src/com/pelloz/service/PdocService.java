package com.pelloz.service;

import java.io.Serializable;
import java.util.List;

import com.pelloz.exception.NoSuchPOException;
import com.pelloz.po.Pdoc;

public interface PdocService extends POService<Pdoc>{

	public void delete(Serializable id) throws NoSuchPOException;
	
	public List<Pdoc> find(String param, String paramname) throws NoSuchPOException;
}
