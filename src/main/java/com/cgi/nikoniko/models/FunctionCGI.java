package com.cgi.nikoniko.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

@Entity
@Table(name = "function_cgi")
public class FunctionCGI extends DatabaseItem{

	@Transient
	public static final String TABLE = "function_cgi";
	@Transient
	public static final String[] FIELDS = { "id", "name" };

	@Column(name = "function_name", nullable = false)
	private String name;

	@Transient
	@OneToMany
	private Set<RoleHasFunction> roles;

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
	 * @return role
	 */
	public Set<RoleHasFunction> getRoles() {
		return roles;
	}

	/**
	 *
	 * @param role
	 */
	public void setRoles(Set<RoleHasFunction> role) {
		this.roles = role;
	}

	public FunctionCGI(){
		super(FunctionCGI.TABLE, FunctionCGI.FIELDS );
	}

	public FunctionCGI(String name){
		this();
		this.name = name;
	}
}
