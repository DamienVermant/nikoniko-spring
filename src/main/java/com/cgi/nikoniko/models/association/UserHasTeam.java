package com.cgi.nikoniko.models.association;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.cgi.nikoniko.models.association.base.AssociationItem;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;

@Entity
@Table(name = UserHasTeam.TABLE)
public class UserHasTeam extends AssociationItem {

	@Transient //naming convention : LeftTableName_has_RightTableName
	public static final String TABLE = "user_has_team";

	@Transient //FIELDS filling convention : {idLeft,idRight, Other attributes...}
	public static final String[] FIELDS = {"idLeft", "idRight", "arrivalDate", "leavingDate"};

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
	 *
	 * @return the arrivalDate
	 */
	public Date getArrivalDate() {
		return arrivalDate;
	}

	/**
	 *
	 * @param arrivalDate the arrivalDate to set
	 */
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	/**
	 *
	 * @return the leavingDate
	 */
	public Date getLeavingDate() {
		return leavingDate;
	}

	/**
	 *
	 * @param leavingDate the leavingDate to set
	 */
	public void setLeavingDate(Date leavingDate) {
		this.leavingDate = leavingDate;
	}

	/**
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 *
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
		//this.user.getUserHasTeams().add(this);
		this.team = team;
		//this.team.getUserHasTeams().add(this);
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
