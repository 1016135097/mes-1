package com.pelloz.po;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class OrderForm {

	@Id
	@GeneratedValue
	private int id;
	private String title;// 订单简介
	private int amount;// 购买的数量
	private double price;// 单价
	@Temporal(TemporalType.DATE)
	private Date date;// 订单预计完成时间
	private boolean isComplete = false;// 是否已经完成

	@ManyToOne(cascade = { CascadeType.MERGE })
	private UserInfo userinfo;// 订单发起人

	@ManyToOne(cascade = { CascadeType.MERGE })
	private Tooling tooling;// 订单需要购买的物品

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 返回单价
	 * @return 单价
	 */
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Tooling getTooling() {
		return tooling;
	}

	public void setTooling(Tooling tooling) {
		this.tooling = tooling;
	}

	public UserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

}
