package com.cgi.nikoniko.models;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

@Entity
@Table(name = "role")
public class RoleCGI extends DatabaseItem{

	@Transient
	public static final String TABLE = "role";

	@Transient
	public static final String[] FIELDS = { "id", "name" };

	@Column(name = "role_name", nullable = false)
	private String name;

	@OneToMany
	private Set<UserHasRole> users;

	@ManyToMany
	private Set<RoleHasFunction> functionCGI;

	/**
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return users
	 */
	public Set<UserHasRole> getUsers() {
		return users;
	}

	/**
	 *
	 * @param users
	 */
	public void setUsers(Set<UserHasRole> users) {
		this.users = users;
	}

	/**
	 *
	 * @return functionCGI
	 */
	public Set<RoleHasFunction> getFunctionCGI() {
		return functionCGI;
	}

	/**
	 *
	 * @param functionCGI
	 */
	public void setFunctionCGI(Set<RoleHasFunction> functionCGI) {
		this.functionCGI = functionCGI;
	}

	public RoleCGI(){
		super(RoleCGI.TABLE, RoleCGI.FIELDS);
	}

	public RoleCGI(String name){
		this();
		this.name = name;
	}

}
