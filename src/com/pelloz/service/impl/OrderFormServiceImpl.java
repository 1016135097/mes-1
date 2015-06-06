package com.pelloz.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.pelloz.dao.OrderFormDao;
import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
import com.pelloz.po.OrderForm;
import com.pelloz.service.OrderFormService;

/**
 * 订单CRUD服务类
 * 
 * @author zp
 *
 */
@Component
public class OrderFormServiceImpl implements OrderFormService {

	@Resource
	private OrderFormDao orderDao;

	@Override
	public void add(OrderForm order) throws POExistException {

		this.orderDao.save(order);
	}

	@Override
	public void delete(OrderForm order) throws NoSuchPOException {

		OrderForm ordertmp = this.orderDao.get(order.getId());
		if (ordertmp == null) {
			throw new NoSuchPOException("要删除订单不存在,id = " + order.getId());
		}
		this.orderDao.delete(ordertmp);
	}

	/**
	 * 必须保证传入的order是非持久化状态
	 */
	@Override
	public void modify(OrderForm order) throws POExistException, NoSuchPOException {

		// 必须保证传入的order不是持久化状态的
		OrderForm ordertmp = this.orderDao.get(order.getId());
		if (ordertmp == null) {
			throw new NoSuchPOException("修改的订单不存在，id = " + order.getId());
		}

		this.orderDao.merge(order);
		return;

	}

	/**
	 * 必须保证传入的order是持久化状态
	 */
	@Override
	public void update(OrderForm order) {

		this.orderDao.update(order);

	}

	@Override
	public OrderForm find(Serializable id) throws NoSuchPOException {

		OrderForm ordertmp = this.orderDao.get(id);// 使用load时必须保证数据存在，不然会报错
		if (ordertmp == null) {
			throw new NoSuchPOException("寻找的订单不存在，id = " + id);
		}
		return ordertmp;
	}

	@Override
	public List<OrderForm> find(String paramname, Object param) throws NoSuchPOException {

		List<OrderForm> orders = this.orderDao.find(paramname, param);
		if (orders == null || orders.size() == 0) {
			throw new NoSuchPOException("find " + paramname + " = " + param);
		}
		return orders;
	}

	@Override
	public List<OrderForm> findLike(String paramname, String param) throws NoSuchPOException {

		List<OrderForm> orders = this.orderDao.findLike(paramname, param);
		if (orders == null || orders.size() == 0) {
			throw new NoSuchPOException("find " + paramname + " = " + param);
		}
		return orders;
	}

	public OrderFormDao getOrderFormDao() {
		return orderDao;
	}

	public void setOrderFormDao(OrderFormDao orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public List<OrderForm> findFromDate(Date begindate, Date enddate) throws NoSuchPOException {
		List<OrderForm> orders = this.orderDao.find(begindate, enddate);
		if (orders == null || orders.size() == 0) {
			throw new NoSuchPOException("时间段" + begindate + "到" + enddate + "找不到订单");
		}
		return orders;
	}

}
