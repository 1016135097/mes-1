package com.pelloz.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.orm.hibernate4.HibernateTemplate;

/**
 * 接口PODao的虚拟帮助类，已经将接口定义的方法都实现了。本类必须被继承才能使用。
 * 必须在子孙类的构造函数中调用super(Class<T> entityClass)将泛型T设置进来
 * 
 * @author zp
 *
 * @param <T> <T>泛型类
 */
public abstract class DaoImplHelper<T> implements PODao<T> {

	@Resource
	protected HibernateTemplate hibernateTemplate;

	private Class<T> tClass;

	public DaoImplHelper(Class<T> entityClass) {
		super();
		this.tClass = entityClass;
	}

	@Override
	public void save(T pobject) {
		// 它会立即执行Sql insert，不管是不是在transaction内部还是外部
		hibernateTemplate.save(pobject);

	}

	@Override
	public void persist(T pobject) {
		// 当它在一个transaction外部被调用的时候并不触发一个Sql Insert
		hibernateTemplate.persist(pobject);

	}

	@Override
	public void update(T pobject) {

		hibernateTemplate.update(pobject);

	}

	@Override
	public void delete(T pobject) {

		hibernateTemplate.delete(pobject);

	}

	@Override
	public void merge(T pobject) {

		hibernateTemplate.merge(pobject);

	}

	@Override
	public T load(Serializable id) {

		T pobject;
		pobject = hibernateTemplate.load(tClass, id);

		return pobject;

	}

	@Override
	public T get(Serializable id) {

		T pobject = hibernateTemplate.get(tClass, id);
		return pobject;

	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
