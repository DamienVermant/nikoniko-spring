package com.cgi.nikoniko.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "team_has_user")
@IdClass(TeamHasUserId.class)
public class TeamHasUser implements Serializable {

	@Transient
	public static final String TABLE = "teamhasuser";

	@Transient
	public static final String[] FIELDS = { "id", "idUser", "idTeam", "arrivalDate", "leavingDate"};

	@Column(nullable = false, name = "arrival_date")
	private Date arrivalDate;

	@Column(nullable = true, name = "leaving_date")
	private Date leavingDate;

	@Transient
	@ManyToOne
	private User user;

	@Transient
	@ManyToOne
	private Team team;

	@Id
	private Long idUser;

	@Id
	private Long idTeam;


	/**
	 * @return the arrivalDate
	 */
	public Date getArrivalDate() {
		return arrivalDate;
	}

	/**
	 * @param arrivalDate the arrivalDate to set
	 */
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	/**
	 * @return the leavingDate
	 */
	public Date getLeavingDate() {
		return leavingDate;
	}

	/**
	 * @param leavingDate the leavingDate to set
	 */
	public void setLeavingDate(Date leavingDate) {
		this.leavingDate = leavingDate;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @return the idUser
	 */
	public Long getIdUser() {
		return idUser;
	}

	/**
	 * @return the idTeam
	 */
	public Long getIdTeam() {
		return idTeam;
	}

	//No empty constructor!!!
	public TeamHasUser (User user, Team team) {
		this.user = user;
		this.user.getTeamsHasUsers().add(this);
		this.idUser = this.user.getId();
		this.team = team;
		this.team.getTeamHasUsers().add(this);
		this.idTeam = this.team.getId();
		this.arrivalDate = new Date();
	}

	public TeamHasUser (User user, Team team, Date arrivaleDate) {
		this(user,team);
		this.arrivalDate = arrivaleDate;
	}

	public TeamHasUser (User user, Team team, Date arrivaleDate, Date leavingDate) {
		this(user,team,arrivaleDate);
		this.leavingDate = leavingDate;
	}

}
