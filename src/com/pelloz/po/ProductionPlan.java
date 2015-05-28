package com.pelloz.po;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class ProductionPlan {

	@Id
	@GeneratedValue
	private int id;
	private String title;// 计划简介
	private Date enddate;// 截止时间
	private boolean isOnPlan;// 是否已经提交计划
	private boolean isOnProducing;// 是否正在生产

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	private UserInfo userinfo;// 计划员
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	private Pdoc pdoc;// 工艺文件

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Pdoc getPdoc() {
		return pdoc;
	}

	public void setPdoc(Pdoc pdoc) {
		this.pdoc = pdoc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public boolean isOnPlan() {
		return isOnPlan;
	}

	public void setOnPlan(boolean isOnPlan) {
		this.isOnPlan = isOnPlan;
	}

	public boolean isOnProducing() {
		return isOnProducing;
	}

	public void setOnProducing(boolean isOnProducing) {
		this.isOnProducing = isOnProducing;
	}

	public UserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}

}
