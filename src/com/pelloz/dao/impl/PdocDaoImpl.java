package com.pelloz.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.pelloz.dao.DaoImplHelper;
import com.pelloz.dao.PdocDao;
import com.pelloz.po.BOM;
import com.pelloz.po.Pdoc;

@Component
public class PdocDaoImpl extends DaoImplHelper<Pdoc> implements PdocDao {


	@Resource
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 按DaoImplHelper<T>的要求使用构造函数将Pdoc.class传入
	 */
	public PdocDaoImpl() {
		super(Pdoc.class);
	}
	
	@Override
	public void save(Pdoc pdoc) {
		// 它会立即执行Sql insert，不管是不是在transaction内部还是外部
		hibernateTemplate.save(pdoc);

	}

	@Override
	public void persist(Pdoc pdoc) {
		// 当它在一个transaction外部被调用的时候并不触发一个Sql Insert
		hibernateTemplate.persist(pdoc);

	}

	@Override
	public void update(Pdoc pdoc) {

		hibernateTemplate.update(pdoc);

	}

	@Override
	public void delete(Pdoc pdoc) {

		hibernateTemplate.delete(pdoc);

	}
	

	@Override
	public void delete(BOM bom) {

		hibernateTemplate.delete(bom);
		
	}

	@Override
	public void merge(Pdoc pdoc) {

		hibernateTemplate.merge(pdoc);

	}

	@Override
	public Pdoc load(Serializable id) {

		Pdoc pdoc = hibernateTemplate.load(Pdoc.class, id);
		return pdoc;

	}

	@Override
	public Pdoc get(Serializable id) {

		Pdoc pdoc = hibernateTemplate.get(Pdoc.class, id);
		return pdoc;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Pdoc> find(String paramname, Object param) {

		List<Pdoc> pdocs;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Pdoc.class);
		detachedCriteria.add(Restrictions.eq(paramname, param));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		pdocs = (List<Pdoc>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return pdocs;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Pdoc> findLike(String paramname, String param) {

		List<Pdoc> pdocs;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Pdoc.class);
		detachedCriteria.add(Restrictions.like(paramname, param, MatchMode.ANYWHERE));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		pdocs = (List<Pdoc>) this.hibernateTemplate.findByCriteria(detachedCriteria);

		return pdocs;

	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
