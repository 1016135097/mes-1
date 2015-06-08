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
import com.pelloz.dao.UserDao;
import com.pelloz.po.UserInfo;

@Component
public class UserDaoImpl extends DaoImplHelper<UserInfo> implements UserDao {

	@Resource
	private HibernateTemplate hibernateTemplate;
	
	public UserDaoImpl() {
		super(UserInfo.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> find(String paramname, Object param) {

		if (paramname.equalsIgnoreCase("password")) {
			return null;// 禁止穷举密码
		}

		List<UserInfo> userInfos;
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(UserInfo.class);
		detachedCriteria.add(Restrictions.eq(paramname, param));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		userInfos = (List<UserInfo>) this.hibernateTemplate
				.findByCriteria(detachedCriteria);

		return userInfos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> findLike(String paramname, String param) {

		if (paramname.equalsIgnoreCase("password")) {
			return null;// 禁止穷举密码
		}

		List<UserInfo> userInfos;
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(UserInfo.class);
		detachedCriteria.add(Restrictions.like(paramname, param, MatchMode.ANYWHERE));
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		userInfos = (List<UserInfo>) this.hibernateTemplate
				.findByCriteria(detachedCriteria);

		return userInfos;
	}
	
	@Override
	public List<UserInfo> findAll() {
		List<UserInfo> userInfos;
		userInfos = this.hibernateTemplate.loadAll(UserInfo.class);
		return userInfos;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
