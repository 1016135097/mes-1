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
		this.pdocDao.delete(pdoc);
	}

	@Override
	public void delete(Serializable id) throws NoSuchPOException {
		Pdoc pdoctmp = this.pdocDao.find(id);
		if (pdoctmp == null) {
			throw new NoSuchPOException("要删除的工艺文件不存在,id = " + id);
		}
		this.pdocDao.delete(pdoctmp);
	}

	@Override
	public void modify(Pdoc pdoc) throws NoSuchPOException, POExistException {
		Pdoc pdoctmp = this.pdocDao.find(pdoc.getId());
		if (pdoctmp == null) {
			throw new NoSuchPOException("修改的工艺不存在，id = " + pdoc.getId());
		}
		System.out.println("------------------------");
		System.out.println(pdoc.getTitle());
		List<Pdoc> pdoctmps = this.pdocDao.find("title", pdoc.getTitle());
		System.out.println("------------------------");
		System.out.println(pdoctmps.get(0).getTitle());
		if ((pdoctmps != null && pdoctmps.get(0).getId() == pdoc.getId()) || pdoctmps == null || pdoctmps.size() == 0) {
			// 找到的pdoctmps和要存入的pdoc的ID相同，或者找不到pdoctmps，就可以修改
			this.pdocDao.modify(pdoc);
			return;
		} else {
			throw new POExistException("修改的工艺标题有重复，title = " + pdoc.getTitle());
		}

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
