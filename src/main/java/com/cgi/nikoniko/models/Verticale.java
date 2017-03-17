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

	@OneToMany(targetEntity = Team.class)
	private Set<Team> team;
	@OneToMany(targetEntity = User.class)
	private Set<User> users;

	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Set<Team> getTeam() {
		return team;
	}
	public void setTeam(Set<Team> team) {
		this.team = team;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public Verticale(){
		super(Verticale.TABLE, Verticale.FIELDS);
	}

	public Verticale(String agency, String name){
		super(Verticale.TABLE, Verticale.FIELDS);
		this.agency = agency;
		this.name = name;
	}
}
