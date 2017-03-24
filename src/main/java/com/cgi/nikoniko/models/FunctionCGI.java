package com.cgi.nikoniko.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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

	@Column
	private String name;

//	@ManyToMany
//	@JoinTable(
//		      name="func_role",
//		      joinColumns=@JoinColumn(name="func_id", referencedColumnName="id"),
//		      inverseJoinColumns=@JoinColumn(name="role_id", referencedColumnName="id"))
//	private Set<RoleCGI> roles;

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

	public void setRoles(Set<RoleHasFunction> roles) {
		this.roles = roles;
	}

	public FunctionCGI(){
		super(FunctionCGI.TABLE, FunctionCGI.FIELDS );
	}

	public FunctionCGI(String name){
		this();
		this.name = name;
	}
}
