package com.pelloz.dao;

import java.util.Date;
import java.util.List;

import com.pelloz.po.Plan;

public interface PlanDao extends PODao<Plan>{
	
	public List<Plan> find(String paramname, Object param);

	public List<Plan> findLike(String paramname, String param);

	public List<Plan> find(Date begindate, Date enddate);

}
