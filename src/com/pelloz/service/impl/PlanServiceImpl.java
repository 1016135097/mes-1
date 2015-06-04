package com.pelloz.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.pelloz.dao.PlanDao;
import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
import com.pelloz.po.Plan;
import com.pelloz.service.PlanService;

/**
 * 计划文件CRUD服务类
 * 
 * @author zp
 *
 */
@Component
public class PlanServiceImpl implements PlanService {

	@Resource
	private PlanDao planDao;

	@Override
	public void add(Plan plan) throws POExistException {

		this.planDao.save(plan);
	}

	@Override
	public void delete(Plan plan) throws NoSuchPOException {

		Plan plantmp = this.planDao.get(plan.getId());
		if (plantmp == null) {
			throw new NoSuchPOException("要删除计划文件不存在,id = " + plan.getId());
		}
		this.planDao.delete(plantmp);
	}

	/**
	 * 必须保证传入的plan是非持久化状态
	 */
	@Override
	public void modify(Plan plan) throws POExistException, NoSuchPOException {

		// 必须保证传入的plan不是持久化状态的
		Plan plantmp = this.planDao.get(plan.getId());
		if (plantmp == null) {
			throw new NoSuchPOException("修改的计划不存在，id = " + plan.getId());
		}

		this.planDao.merge(plan);
		return;

	}

	/**
	 * 必须保证传入的plan是持久化状态
	 */
	@Override
	public void update(Plan plan) {

		this.planDao.update(plan);

	}

	@Override
	public Plan find(Serializable id) throws NoSuchPOException {

		Plan plantmp = this.planDao.get(id);// 使用load时必须保证数据存在，不然会报错
		if (plantmp == null) {
			throw new NoSuchPOException("寻找的计划不存在，id = " + id);
		}
		return plantmp;
	}

	@Override
	public List<Plan> find(String paramname, Object param) throws NoSuchPOException {

		List<Plan> plans = this.planDao.find(paramname, param);
		if (plans == null || plans.size() == 0) {
			throw new NoSuchPOException("find " + paramname + " = " + param);
		}
		return plans;
	}

	@Override
	public List<Plan> findLike(String paramname, String param) throws NoSuchPOException {

		List<Plan> plans = this.planDao.findLike(paramname, param);
		if (plans == null || plans.size() == 0) {
			throw new NoSuchPOException("find " + paramname + " = " + param);
		}
		return plans;
	}

	public PlanDao getPlanDao() {
		return planDao;
	}

	public void setPlanDao(PlanDao planDao) {
		this.planDao = planDao;
	}

	@Override
	public List<Plan> findFromDate(Date begindate, Date enddate) throws NoSuchPOException {
		List<Plan> plans = this.planDao.find(begindate, enddate);
		if (plans == null || plans.size() == 0) {
			throw new NoSuchPOException("时间段" + begindate + "到" + enddate + "找不到计划");
		}
		return plans;
	}

}
