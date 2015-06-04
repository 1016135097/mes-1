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
public class UserInfo {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(unique=true)
	private String username;
	private String password;
	private String fullname;
	private String department;//TODO department和title后续可以单独建表，方便维护
	private String title;

	@OneToMany(mappedBy = "userinfo", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private Set<Pdoc> pdocs = new HashSet<Pdoc>();

	@OneToMany(mappedBy = "userinfo", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private Set<Plan> productionplan = new HashSet<Plan>();

	@OneToMany(mappedBy = "userinfo", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private Set<OrderForm> orderforms = new HashSet<OrderForm>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Pdoc> getPdocs() {
		return pdocs;
	}

	public void setPdocs(Set<Pdoc> pdocs) {
		this.pdocs = pdocs;
	}

	public Set<Plan> getProductionplan() {
		return productionplan;
	}

	public void setProductionplan(Set<Plan> productionplan) {
		this.productionplan = productionplan;
	}

	public Set<OrderForm> getOrderforms() {
		return orderforms;
	}

	public void setOrderforms(Set<OrderForm> orderforms) {
		this.orderforms = orderforms;
	}

}
