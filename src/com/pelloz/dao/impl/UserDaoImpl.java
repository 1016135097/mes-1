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

import com.pelloz.dao.UserDao;
import com.pelloz.po.UserInfo;

@Component
public class UserDaoImpl implements UserDao {

	@Resource
	private HibernateTemplate hibernateTemplate;

	@Override
	public void save(UserInfo userInfo) {

		hibernateTemplate.save(userInfo);

	}

	@Override
	public void delete(UserInfo userInfo) {

		hibernateTemplate.delete(userInfo);

	}

	@Override
	public void merge(UserInfo userInfo) {
		//当我们使用update的时候，执行完成后，我们提供的对象A的状态变成持久化状态
		//但当我们使用merge的时候，执行完成，我们提供的对象A还是脱管状态
		hibernateTemplate.merge(userInfo);

	}

	@Override
	public UserInfo get(Serializable id) {
		
		UserInfo userInfo = hibernateTemplate.get(UserInfo.class, id);
		return userInfo;
		
	}
	
	@Override
	public UserInfo load(Serializable id) {
		
		UserInfo userInfo = hibernateTemplate.load(UserInfo.class, id);
		return userInfo;
		
	}
	
	@Override
	public void persist(UserInfo pdoc) {
		
		hibernateTemplate.persist(pdoc);
		
	}

	@Override
	public void update(UserInfo pdoc) {

		hibernateTemplate.update(pdoc);
		
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

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
