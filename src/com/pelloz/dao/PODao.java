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

	public void add(T pobject);

	public void delete(T pobject);

	public void modify(T pobject);

	public T find(Serializable id);
}
