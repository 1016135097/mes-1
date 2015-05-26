package com.pelloz.po;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BOM {

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne(cascade = { CascadeType.ALL })
	private Pdoc pdoc;

	// 对应的tooling
	@ManyToOne(cascade = { CascadeType.ALL })
	private Tooling tooling;

	// 这个tooling的需求个数
	private int amount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tooling getTooling() {
		return tooling;
	}

	public void setTooling(Tooling tooling) {
		this.tooling = tooling;
	}

	public Pdoc getPdoc() {
		return pdoc;
	}

	public void setPdoc(Pdoc pdoc) {
		this.pdoc = pdoc;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
