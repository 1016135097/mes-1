package com.pelloz.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.pelloz.dao.PdocDao;
import com.pelloz.po.Pdoc;

@Component
public class PdocDaoImpl implements PdocDao {

	@Resource
	private HibernateTemplate hibernateTemplate;

	@Override
	public void add(Pdoc pdoc) {

		hibernateTemplate.save(pdoc);

	}

	@Override
	public void delete(Pdoc pdoc) {

		hibernateTemplate.delete(pdoc);

	}

	@Override
	public void modify(Pdoc pdoc) {
		hibernateTemplate.merge(pdoc);
	}

	@Override
	public Pdoc find(Serializable id) {
		Pdoc pdoc = hibernateTemplate.get(Pdoc.class, id);
		return pdoc;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Pdoc> find(String param, String paramname) {

		List<Pdoc> pdocs;
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(Pdoc.class);
		detachedCriteria.add(Restrictions.like(paramname, param));
		pdocs = (List<Pdoc>) this.hibernateTemplate
				.findByCriteria(detachedCriteria);

		return pdocs;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
