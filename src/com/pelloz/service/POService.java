package com.pelloz.service;

import java.io.Serializable;

import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
/**
 * 实体类服务接口，会抛出POExistException和NoSuchPOException
 * @author zp
 *
 * @param <T>
 */
interface POService<T> {
	
	public void add(T pobject) throws POExistException;
	
	public void delete(T pobject) throws NoSuchPOException;
	
	public void modify(T pobject) throws NoSuchPOException;
	
	public T find(Serializable id) throws NoSuchPOException;
	
}
