package com.pelloz.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.pelloz.dao.ToolingDao;
import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
import com.pelloz.po.Tooling;
import com.pelloz.service.ToolingService;

/**
 * 工装CRUD服务类
 * 
 * @author zp
 *
 */
@Component
public class ToolingServiceImpl implements ToolingService {

	@Resource
	private ToolingDao toolingDao;

	@Override
	public void add(Tooling tooling) throws POExistException {

		List<Tooling> toolings = this.toolingDao.find("name", tooling.getName());
		if (toolings != null && toolings.size() > 0) {
			throw new POExistException("工装名有重复:" + tooling.getName());
		}
		this.toolingDao.save(tooling);
	}

	@Override
	public void delete(Tooling tooling) throws NoSuchPOException {

		Tooling toolingtmp = this.toolingDao.get(tooling.getId());
		if (toolingtmp == null) {
			throw new NoSuchPOException("要删除工装不存在,id = " + tooling.getId());
		}
		this.toolingDao.delete(toolingtmp);
	}

	@Override
	public void delete(Serializable id) throws NoSuchPOException {

		Tooling toolingtmp = this.toolingDao.get(id);
		if (toolingtmp == null) {
			throw new NoSuchPOException("要删除的工装不存在,id = " + id);
		}
		this.toolingDao.delete(toolingtmp);
	}

	/**
	 * 必须保证传入的tooling不是持久化状态的
	 */
	@Override
	public void modify(Tooling tooling) throws POExistException, NoSuchPOException {

		Tooling toolingtmp = this.toolingDao.get(tooling.getId());
		if (toolingtmp == null) {
			throw new NoSuchPOException("修改的工装不存在，id = " + tooling.getId());
		}

		List<Tooling> toolingtmps = this.toolingDao.find("name", tooling.getName());

		if (toolingtmps == null || toolingtmps.size() == 0) {
			this.toolingDao.merge(tooling);
			return;
		} else if (toolingtmps.get(0).getId() == tooling.getId()) {
			this.toolingDao.merge(tooling);
			return;
		} else {
			throw new POExistException("修改的工装名称有重复，name = " + tooling.getName());
		}

	}
	
	/**
	 * 必须保证传入的tooling是持久化状态的
	 */
	@Override
	public void update(Tooling tooling) {
		
		this.toolingDao.update(tooling);

	}

	@Override
	public Tooling find(Serializable id) throws NoSuchPOException {

		Tooling toolingtmp = this.toolingDao.get(id);// 使用load时必须保证数据存在，不然会报错
		if (toolingtmp == null) {
			throw new NoSuchPOException("寻找的工装不存在，id = " + id);
		}
		return toolingtmp;
	}

	@Override
	public List<Tooling> find(String paramname, Object param) throws NoSuchPOException {

		List<Tooling> toolings = this.toolingDao.find(paramname, param);
		if (toolings == null || toolings.size() == 0) {
			throw new NoSuchPOException("寻找的工装不存在，" + paramname + " = " + param);
		}
		return toolings;
	}

	@Override
	public List<Tooling> findLike(String paramname, String param) throws NoSuchPOException {

		List<Tooling> toolings = this.toolingDao.findLike(paramname, param);
		if (toolings == null || toolings.size() == 0) {
			throw new NoSuchPOException("寻找的工装不存在，" + paramname + " = " + param);
		}
		return toolings;
	}

	@Override
	public List<Tooling> findAll() throws NoSuchPOException {
		List<Tooling> toolings = this.toolingDao.findAll();
		if (toolings == null || toolings.size() == 0) {
			throw new NoSuchPOException("系统中现在没有任何工装");
		}
		return toolings;
	}
	
	public ToolingDao getToolingDao() {
		return toolingDao;
	}

	public void setToolingDao(ToolingDao toolingDao) {
		this.toolingDao = toolingDao;
	}

}
