package com.pelloz.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.pelloz.dao.DaoImplHelper;
import com.pelloz.dao.ToolingDao;
import com.pelloz.po.Tooling;

@Component
public class ToolingDaoImpl extends DaoImplHelper<Tooling> implements ToolingDao {

	@Resource
	private HibernateTemplate hibernateTemplate;

	/**
	 * 按DaoImplHelper<T>的要求使用构造函数将Tooling.class传入
	 */
	public ToolingDaoImpl() {
		super(Tooling.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Tooling> find(String paramname, Object param) {

		List<Tooling> toolings;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tooling.class);
		detachedCriteria.add(Restrictions.eq(paramname, param));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		toolings = (List<Tooling>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return toolings;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Tooling> findLike(String paramname, String param) {

		List<Tooling> toolings;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tooling.class);
		detachedCriteria.add(Restrictions.like(paramname, param, MatchMode.ANYWHERE));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		toolings = (List<Tooling>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return toolings;

	}

	@Override
	public List<Tooling> findAll() {
		List<Tooling> toolings;
		toolings = this.hibernateTemplate.loadAll(Tooling.class);
		return toolings;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
