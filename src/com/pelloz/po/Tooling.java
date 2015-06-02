package com.pelloz.po;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Tooling {

	@Id
	@GeneratedValue
	private int id;

	@Column(unique = true)
	private String name;//工具名称
	
	private int amount;//工具总量

	/*@OneToMany(mappedBy = "tooling", cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	private Set<BOM> boms = new HashSet<BOM>();*/

	@OneToMany(mappedBy = "tooling", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private Set<OrderForm> orderforms = new HashSet<OrderForm>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Set<OrderForm> getOrderforms() {
		return orderforms;
	}

	public void setOrderforms(Set<OrderForm> orderforms) {
		this.orderforms = orderforms;
	}

/*	public Set<BOM> getBoms() {
		return boms;
	}

	public void setBoms(Set<BOM> boms) {
		this.boms = boms;
	}*/

}
