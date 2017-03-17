package com.cgi.nikoniko.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

public class FunctionCGI extends DatabaseItem{

	@Transient
	public static final String TABLE = "role";
	@Transient
	public static final String[] FIELDS = { "id", "name" };

	@Column(name = "functionCGI_name", nullable = false)
	private String name;

	@ManyToMany(targetEntity = RoleCGI.class)
	private Set<RoleCGI> role;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<RoleCGI> getRole() {
		return role;
	}

	public void setRole(Set<RoleCGI> role) {
		this.role = role;
	}

	public FunctionCGI(){
		super(FunctionCGI.TABLE, FunctionCGI.FIELDS );
	}

	public FunctionCGI(String name){
		super(FunctionCGI.TABLE, FunctionCGI.FIELDS );
		this.name = name;
	}
}
