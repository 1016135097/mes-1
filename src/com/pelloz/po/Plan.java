package com.pelloz.po;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Plan {

	@Id
	@GeneratedValue
	private int id;
	private String title;// 计划简介
	private int num;//生产数量
	@Temporal(TemporalType.DATE)
	private Date endDate;// 截止时间
	private boolean isOnPlan = false;// 计划是否上线
	private boolean isOnProducting = false;// 是否正在生产

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

	public boolean isOnPlan() {
		return isOnPlan;
	}

	public void setOnPlan(boolean isOnPlan) {
		this.isOnPlan = isOnPlan;
	}

	public UserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isOnProducting() {
		return isOnProducting;
	}

	public void setOnProducting(boolean isOnProducting) {
		this.isOnProducting = isOnProducting;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
