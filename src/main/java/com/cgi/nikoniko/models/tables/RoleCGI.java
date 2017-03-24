package com.cgi.nikoniko.models.tables;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.association.RoleHasFunction;
import com.cgi.nikoniko.models.association.UserHasRole;
import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;

@Entity
@Table(name = RoleCGI.TABLE)
public class RoleCGI extends DatabaseItem {

	@Transient
	public static final String TABLE = "role";

	@Transient
	public static final String[] FIELDS = { "id", "name" };

	@Column(name = "name", nullable = false)
	private String name;

	@Transient
	@OneToMany
	private Set<UserHasRole> users;

	@Transient
	@OneToMany
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
	public void setUsers(ArrayList<UserHasRole> users) {
		this.users = (Set<UserHasRole>) users;
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
	public void setFunctionCGI(ArrayList<RoleHasFunction> functionCGI) {
		this.functionCGI = (Set<RoleHasFunction> ) functionCGI;
	}

	public RoleCGI(){
		super(RoleCGI.TABLE, RoleCGI.FIELDS);
	}

	public RoleCGI(String name){
		this();
		this.name = name;
	}

}
