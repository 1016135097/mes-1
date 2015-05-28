package com.pelloz.dao;

import java.io.Serializable;

/**
 * 数据库服务接口。
 * 不抛出POExistException和NoSuchPOException，由实体服务接口POService负责
 * @author zp
 *
 * @param <T>
 */
interface PODao<T> {
	
	public void save(T pobject);

	public void persist(T pobject);

	public void update(T pobject);

	public void merge(T pobject);

	public T load(Serializable id);

	public T get(Serializable id);

	public void delete(T pobject);

}
