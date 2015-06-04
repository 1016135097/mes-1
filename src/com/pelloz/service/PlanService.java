package com.pelloz.service;

import java.util.Date;
import java.util.List;

import com.pelloz.exception.NoSuchPOException;
import com.pelloz.po.Plan;

public interface PlanService extends POService<Plan> {

	public List<Plan> find(String paramname, Object param) throws NoSuchPOException;

	public List<Plan> findLike(String paramname, String param) throws NoSuchPOException;

	/**
	 * 查找时间在begindate和enddate之间的plan。使用null代表不限制时间。
	 * @param begindate
	 * @param enddate
	 * @return
	 * @throws NoSuchPOException
	 */
	public List<Plan> findFromDate(Date begindate, Date enddate) throws NoSuchPOException;
}
