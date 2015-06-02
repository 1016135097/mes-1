package com.pelloz.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.pelloz.dao.PdocDao;
import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
import com.pelloz.po.BOM;
import com.pelloz.po.Pdoc;
import com.pelloz.service.PdocService;

/**
 * 工艺文件CRUD服务类
 * 
 * @author zp
 *
 */
@Component
public class PdocServiceImpl implements PdocService {

	@Resource
	private PdocDao pdocDao;

	@Override
	public void add(Pdoc pdoc) throws POExistException {

		List<Pdoc> pdocs = this.pdocDao.find("title", pdoc.getTitle());
		if (pdocs != null && pdocs.size() > 0) {
			throw new POExistException("工艺文件名有重复:" + pdoc.getTitle());
		}
		this.pdocDao.save(pdoc);
	}

	@Override
	public void delete(Pdoc pdoc) throws NoSuchPOException {

		Pdoc pdoctmp = this.pdocDao.get(pdoc.getId());
		if (pdoctmp == null) {
			throw new NoSuchPOException("要删除工艺文件不存在,id = " + pdoc.getId());
		}
		this.pdocDao.delete(pdoctmp);
	}

	@Override
	public void delete(Serializable id) throws NoSuchPOException {

		Pdoc pdoctmp = this.pdocDao.get(id);
		if (pdoctmp == null) {
			throw new NoSuchPOException("要删除的工艺文件不存在,id = " + id);
		}
		this.pdocDao.delete(pdoctmp);
	}
	

	@Override
	public void delete(BOM bom) {

		this.pdocDao.delete(bom);
		
	}

	/**
	 * 必须保证传入的pdoc是非持久化状态
	 */
	@Override
	public void modify(Pdoc pdoc) throws POExistException, NoSuchPOException {

		// 必须保证传入的pdoc不是持久化状态的
		Pdoc pdoctmp = this.pdocDao.get(pdoc.getId());
		if (pdoctmp == null) {
			throw new NoSuchPOException("修改的工艺不存在，id = " + pdoc.getId());
		}

		List<Pdoc> pdoctmps = this.pdocDao.find("title", pdoc.getTitle());

		if (pdoctmps.size() == 0) {
			this.pdocDao.merge(pdoc);
			return;
		} else if (pdoctmps.get(0).getId() == pdoc.getId()) {
			this.pdocDao.merge(pdoc);
			return;
		} else{
			throw new POExistException("修改的工艺标题有重复，title = " + pdoc.getTitle());
		}
		
	}
	
	/**
	 * 必须保证传入的pdoc是持久化状态
	 */
	@Override
	public void update(Pdoc pdoc) {

		this.pdocDao.update(pdoc);
		
	}

	@Override
	public Pdoc find(Serializable id) throws NoSuchPOException {

		Pdoc pdoctmp = this.pdocDao.get(id);// 使用load时必须保证数据存在，不然会报错
		if (pdoctmp == null) {
			throw new NoSuchPOException("寻找的工艺不存在，id = " + id);
		}
		return pdoctmp;
	}

	@Override
	public List<Pdoc> find(String paramname, Object param) throws NoSuchPOException {

		List<Pdoc> pdocs = this.pdocDao.find(paramname, param);
		if (pdocs == null || pdocs.size() == 0) {
			throw new NoSuchPOException("find " + paramname + " = " + param);
		}
		return pdocs;
	}

	@Override
	public List<Pdoc> findLike(String paramname, String param) throws NoSuchPOException {

		List<Pdoc> pdocs = this.pdocDao.findLike(paramname, param);
		if (pdocs == null || pdocs.size() == 0) {
			throw new NoSuchPOException("find " + paramname + " = " + param);
		}
		return pdocs;
	}

	public PdocDao getPdocDao() {
		return pdocDao;
	}

	public void setPdocDao(PdocDao pdocDao) {
		this.pdocDao = pdocDao;
	}

}
