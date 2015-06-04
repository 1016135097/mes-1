package com.pelloz.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.pelloz.dao.DaoImplHelper;
import com.pelloz.dao.PlanDao;
import com.pelloz.po.Plan;

@Component
public class PlanDaoImpl extends DaoImplHelper<Plan> implements PlanDao {

	@Resource
	private HibernateTemplate hibernateTemplate;

	/**
	 * 按DaoImplHelper<T>的要求使用构造函数将Plan.class传入
	 */
	public PlanDaoImpl() {
		super(Plan.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Plan> find(String paramname, Object param) {

		List<Plan> plans;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Plan.class);
		detachedCriteria.add(Restrictions.eq(paramname, param));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		plans = (List<Plan>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return plans;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Plan> findLike(String paramname, String param) {

		List<Plan> plans;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Plan.class);
		detachedCriteria.add(Restrictions.like(paramname, param, MatchMode.ANYWHERE));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		plans = (List<Plan>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return plans;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Plan> find(Date begindate, Date enddate) {

		List<Plan> plans;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Plan.class);
		if (begindate != null) {
			detachedCriteria.add(Restrictions.ge("endDate", begindate));
		}
		if (enddate != null) {
			detachedCriteria.add(Restrictions.le("endDate", enddate));
		}
		plans = (List<Plan>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return plans;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
