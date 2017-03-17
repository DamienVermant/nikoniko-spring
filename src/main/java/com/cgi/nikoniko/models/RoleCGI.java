package com.cgi.nikoniko.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

public class RoleCGI extends DatabaseItem{

	@Transient
	public static final String TABLE = "role";
	@Transient
	public static final String[] FIELDS = { "id", "name" };

	@Column(name = "role_name", nullable = false)
	private String name;

	@ManyToMany(targetEntity = User.class)
	private Set<User> users;

	@ManyToMany(targetEntity = FunctionCGI.class)
	private Set<FunctionCGI> functionCGI;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<FunctionCGI> getFunctionCGI() {
		return functionCGI;
	}

	public void setFunctionCGI(Set<FunctionCGI> functionCGI) {
		this.functionCGI = functionCGI;
	}


	public RoleCGI(){
		super(RoleCGI.TABLE, RoleCGI.FIELDS);
	}

	public RoleCGI(String name){
		super(RoleCGI.TABLE, RoleCGI.FIELDS);
		this.name = name;
	}
}
