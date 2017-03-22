package com.cgi.nikoniko.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.AssociationItem;

@Entity
@Table(name = "user_has_team")
public class UserHasTeam extends AssociationItem {

	@Transient //nom de table toujours tableDeGauche_has_tableDeDroite
	public static final String TABLE = "user_has_team";//ou creer TABLE_LEFT et TABLE_RIGHT

	@Transient //convention {idLeft,idRight, Other attributes...}
	public static final String[] FIELDS = {/*"idLeft", "idRight",*/ "arrivalDate", "leavingDate"};
	//TODO : fonction generation des noms des idLeft et IdRight pour le viewer

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

	public UserHasTeam () {
		super(UserHasTeam.TABLE,UserHasTeam.FIELDS);
	}

	public UserHasTeam (User user, Team team) {
		super(UserHasTeam.TABLE,UserHasTeam.FIELDS, user, team);
		this.user = user;
		this.user.getTeamsHasUsers().add(this);
		this.team = team;
		this.team.getTeamHasUsers().add(this);
		this.arrivalDate = new Date();
	}

	public UserHasTeam (User user, Team team, Date arrivaleDate) {
		this(user,team);
		this.arrivalDate = arrivaleDate;
	}

	public UserHasTeam (User user, Team team, Date arrivaleDate, Date leavingDate) {
		this(user,team,arrivaleDate);
		this.leavingDate = leavingDate;
	}

}
