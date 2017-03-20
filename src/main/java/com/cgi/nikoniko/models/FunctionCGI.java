package com.cgi.nikoniko.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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

	@ManyToMany
	private Set<RoleCGI> role;

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
	public Set<RoleCGI> getRole() {
		return role;
	}

	/**
	 *
	 * @param role
	 */
	public void setRole(Set<RoleCGI> role) {
		this.role = role;
	}

	public FunctionCGI(){
		super(FunctionCGI.TABLE, FunctionCGI.FIELDS );
	}

	public FunctionCGI(String name){
		this();
		this.name = name;
	}
}
