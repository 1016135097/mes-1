package com.pelloz.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.pelloz.dao.PdocDao;
import com.pelloz.exception.NoSuchPOException;
import com.pelloz.exception.POExistException;
import com.pelloz.po.Pdoc;
import com.pelloz.service.PdocService;

/**
 * 工艺文件CRUD服务类
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
		if (pdocs.size() > 0) {
			throw new POExistException("工艺文件名有重复:" + pdoc.getTitle());
		}
		this.pdocDao.add(pdoc);
	}

	@Override
	public void delete(Pdoc pdoc) throws NoSuchPOException {
		Pdoc pdoctmp = this.pdocDao.find(pdoc.getId());
		if (pdoctmp == null) {
			throw new NoSuchPOException("要删除工艺文件不存在,id = " + pdoc.getId());
		}
		this.pdocDao.add(pdoc);
	}
	
	@Override
	public void delete(Serializable id) throws NoSuchPOException {
		Pdoc pdoctmp = this.pdocDao.find(id);
		if (pdoctmp == null) {
			throw new NoSuchPOException("要删除的工艺文件不存在,id = " + id);
		}
		this.pdocDao.add(pdoctmp);
	}

	@Override
	public void modify(Pdoc pdoc) throws NoSuchPOException {
		Pdoc pdoctmp = this.pdocDao.find(pdoc.getId());
		if (pdoctmp == null) {
			throw new NoSuchPOException("修改的工艺不存在，id = " + pdoc.getId());
		}
		this.pdocDao.modify(pdoc);

	}

	@Override
	public Pdoc find(Serializable id) throws NoSuchPOException {
		Pdoc pdoctmp = this.pdocDao.find(id);
		if (pdoctmp == null) {
			throw new NoSuchPOException("寻找的工艺不存在，id = " + id);
		}
		return pdoctmp;
	}


	@Override
	public List<Pdoc> find(String param, String paramname)
			throws NoSuchPOException {
		List<Pdoc> pdocs = this.pdocDao.find(param, paramname);
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
