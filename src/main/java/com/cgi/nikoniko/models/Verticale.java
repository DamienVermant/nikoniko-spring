package com.cgi.nikoniko.models;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

@Entity
@Table(name = "verticale")
public class Verticale extends DatabaseItem{

	@Transient
	public static final String TABLE = "verticale";

	@Transient
	public static final String[] FIELDS = { "id", "agency", "name" };

	@Column(name = "verticale_agency", nullable = false)
	private String agency;

	@Column(name = "verticale_name", nullable = false)
	private String name;

	@OneToMany
	private Set<Team> teams;

	@OneToMany
	private Set<User> users;

	/**
	 *
	 * @return agency
	 */
	public String getAgency() {
		return agency;
	}

	/**
	 *
	 * @param agency
	 */
	public void setAgency(String agency) {
		this.agency = agency;
	}

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
	 * @return team
	 */
	public Set<Team> getTeams() {
		return teams;
	}

	/**
	 *
	 * @param team
	 */
	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}

	/**
	 *
	 * @return users
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 *
	 * @param users
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Verticale(){
		super(Verticale.TABLE, Verticale.FIELDS);
	}

	public Verticale(String agency, String name){
		this();
		this.agency = agency;
		this.name = name;
	}
}
