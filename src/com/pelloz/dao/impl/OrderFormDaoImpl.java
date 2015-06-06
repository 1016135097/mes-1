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
import com.pelloz.dao.OrderFormDao;
import com.pelloz.po.OrderForm;

@Component
public class OrderFormDaoImpl extends DaoImplHelper<OrderForm> implements OrderFormDao {

	@Resource
	private HibernateTemplate hibernateTemplate;

	/**
	 * 按DaoImplHelper<T>的要求使用构造函数将OrderForm.class传入
	 */
	public OrderFormDaoImpl() {
		super(OrderForm.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrderForm> find(String paramname, Object param) {

		List<OrderForm> orders;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(OrderForm.class);
		detachedCriteria.add(Restrictions.eq(paramname, param));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		orders = (List<OrderForm>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return orders;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrderForm> findLike(String paramname, String param) {

		List<OrderForm> orders;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(OrderForm.class);
		detachedCriteria.add(Restrictions.like(paramname, param, MatchMode.ANYWHERE));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		orders = (List<OrderForm>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return orders;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrderForm> find(Date begindate, Date enddate) {

		List<OrderForm> orders;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(OrderForm.class);
		if (begindate != null) {
			detachedCriteria.add(Restrictions.ge("date", begindate));
		}
		if (enddate != null) {
			detachedCriteria.add(Restrictions.le("date", enddate));
		}
		orders = (List<OrderForm>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return orders;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
