package com.pelloz.dao;

import java.util.Date;
import java.util.List;

import com.pelloz.po.OrderForm;

public interface OrderFormDao extends PODao<OrderForm>{
	
	public List<OrderForm> find(String paramname, Object param);

	public List<OrderForm> findLike(String paramname, String param);

	public List<OrderForm> find(Date begindate, Date enddate);

}
