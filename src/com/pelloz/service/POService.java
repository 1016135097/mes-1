package com.pelloz.service;

import java.io.Serializable;

import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
import com.pelloz.po.Pdoc;
import com.pelloz.po.Tooling;
/**
 * 实体类服务接口，会抛出POExistException和NoSuchPOException
 * @author zp
 *
 * @param <T>
 */
interface POService<T> {
	
	public void add(T pobject) throws POExistException;
	
	public void delete(T pobject) throws NoSuchPOException;
	
	/**
	 * pobject必须是脱管状态(Detached)
	 * @param pobject
	 * @throws NoSuchPOException
	 * @throws POExistException
	 */
	public void modify(T pobject) throws NoSuchPOException, POExistException;
	
	/**
	 * pobject必须是持久化状态（Persistent）
	 * @param pobject
	 */
	public void update(T pobject);
	
	public T find(Serializable id) throws NoSuchPOException;

}
