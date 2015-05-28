package com.pelloz.po;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Pdoc {

	@Id
	@GeneratedValue
	private int id;

	@Column(unique = true)
	private String title;// 工艺文件名称

	@Column(length = 500)
	private String content;// 工艺文件内容

	// 作者
	@ManyToOne(cascade = { CascadeType.MERGE })
	private UserInfo userinfo;

	// 工艺文件中对应的BOM表
	@OneToMany(mappedBy = "pdoc", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private Set<BOM> boms = new HashSet<BOM>();

	@OneToMany(mappedBy = "pdoc", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private Set<ProductionPlan> productionplan = new HashSet<ProductionPlan>();

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<ProductionPlan> getProductionplan() {
		return productionplan;
	}

	public void setProductionplan(Set<ProductionPlan> productionplan) {
		this.productionplan = productionplan;
	}

	public UserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}

	public Set<BOM> getBoms() {
		return boms;
	}

	public void setBoms(Set<BOM> boms) {
		this.boms = boms;
	}

}
