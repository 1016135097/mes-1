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
	public void add(UserInfo userInfo) {

		hibernateTemplate.save(userInfo);

	}

	@Override
	public void delete(UserInfo userInfo) {

		hibernateTemplate.delete(userInfo);

	}

	@Override
	public void modify(UserInfo userInfo) {
		// update必须是从数据库中取出来的对象在更改后update，一直处于session管控之下
		// merge可以是用户自己new出来的对象，然后更新覆盖到数据库中，是脱管的
		// this.hibernateTemplate.update(user);
		hibernateTemplate.merge(userInfo);

	}

	@Override
	public UserInfo find(Serializable id) {
		UserInfo userInfo = hibernateTemplate.get(UserInfo.class, id);
		return userInfo;
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
