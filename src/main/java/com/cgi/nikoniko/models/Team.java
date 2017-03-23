package com.cgi.nikoniko.models;


import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

@Entity
@Table(name = "team")
public class Team extends DatabaseItem{

	@Transient
	public static final String DEFAULT_COLORS = "red,orange,green";

	@Transient
	public static final int DEFAULT_NUMBER = 3;

	@Transient
	public static final int DEFAULT_VISIBILITY = 0;

	@Transient
	public static final Boolean DEFAULT_PRIVACY = false;

	@Transient
	public static final String TABLE = "team";
	@Transient
	public static final String[] FIELDS = { "id", "name", "serial", "start_date", "end_date",
		"niko_sticker_color", "niko_sticker_number", "niko_visibility", "niko_privacy" };

	@Column(name = "team_name", nullable = true)
	private String name;

	@Column(name = "team_serial", nullable = false)
	private String serial;

	@Column(name = "team_start_date", nullable = false)
	private Date start_date;

	@Column(name = "team_end_date", nullable = true)
	private Date end_date;

	@Column(name = "team_sticker_color", nullable = false)
	private String niko_sticker_color;

	@Column(name = "team_sticker_number", nullable = false)
	private int niko_sticker_number;

	@Column(name = "team_visibility", nullable = false)
	private int niko_visibility;

	@Column(name = "team_privacy", nullable = false)
	private Boolean niko_privacy;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="VERTICALE_ID")
	private Verticale verticale;

	@OneToMany
	private Set<UserHasTeam> teamHasUsers;

	// ADD TO TEST RELATIONS /////

	@Transient
	private ArrayList<User> users;

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the serial
	 */
	public String getSerial() {
		return serial;
	}

	/**
	 * @param serial
	 *            the serial to set
	 */
	public void setSerial(String serial) {
		this.serial = serial;
	}

	/**
	 *
	 * @return  start_date
	 */
	public Date getStart_date() {
		return start_date;
	}

	/**
	 *
	 * @param start_date
	 */
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	/**
	 *
	 * @return end_date
	 */
	public Date getEnd_date() {
		return end_date;
	}

	/**
	 *
	 * @param end_date
	 */
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	/**
	 *
	 * @return niko_sticker_color
	 */
	public String getNiko_sticker_color() {
		return niko_sticker_color;
	}

	/**
	 *
	 * @param niko_sticker_color
	 */
	public void setNiko_sticker_color(String niko_sticker_color) {
		this.niko_sticker_color = niko_sticker_color;
	}

	/**
	 *
	 * @return niko_sticker_number
	 */
	public int getNiko_sticker_number() {
		return niko_sticker_number;
	}

	/**
	 *
	 * @param niko_sticker_number
	 */
	public void setNiko_sticker_number(int niko_sticker_number) {
		this.niko_sticker_number = niko_sticker_number;
	}

	/**
	 *
	 * @return niko_visibility
	 */
	public int getNiko_visibility() {
		return niko_visibility;
	}

	/**
	 *
	 * @param niko_visibility
	 */
	public void setNiko_visibility(int niko_visibility) {
		this.niko_visibility = niko_visibility;
	}

	/**
	 *
	 * @return niko_privacy
	 */
	public Boolean getNiko_privacy() {
		return niko_privacy;
	}

	/**
	 *
	 * @param niko_privacy
	 */
	public void setNiko_privacy(Boolean niko_privacy) {
		this.niko_privacy = niko_privacy;
	}

	/**
	 * @return the verticale
	 */
	public Verticale getVerticale() {
		return verticale;
	}

	/**
	 * @param verticale
	 *            the verticale to set
	 */
	public void setVerticale(Verticale verticale) {
		this.verticale = verticale;
//		//TODO : mettre cette partie dans le getter peut donner un meilleur resultat
//		if (!this.verticale.getTeam().contains(this)) {
//			//TODO rajouter ce user a la verticale qui a ete set
//			this.verticale.getTeam().add(this);
//		}
	}

	/**
	 *
	 * @return teamHasUsers
	 */
	public Set<UserHasTeam> getTeamHasUsers() {
		return teamHasUsers;
	}

	/**
	 *
	 * @param teamHasUsers
	 */
	public void setTeamHasUsers(Set<UserHasTeam> teamHasUsers) {
		this.teamHasUsers = teamHasUsers;
	}

	public Team() {
		super(Team.TABLE, Team.FIELDS);
		this.niko_sticker_color = DEFAULT_COLORS;
		this.niko_sticker_number = DEFAULT_NUMBER;
		this.niko_visibility = DEFAULT_VISIBILITY;
		this.niko_privacy = DEFAULT_PRIVACY;
	}

	public Team (String serial,Date start_date, String name){
		this();
		this.serial=serial;
		this.start_date = start_date;
		this.name = name;
	}

	public Team (String serial){
		this(serial, new Date(), "");
	}

	public Team (String serial,Date start_date){
		this(serial, start_date, "");
	}

	public Team (String serial,Date start_date, Verticale verticale){
		this(serial, start_date, "");
		this.verticale = verticale;
		this.verticale.getTeams().add(this);
	}

	public Team (String serial,Date start_date, String name, Verticale verticale){
		this(serial, start_date, name);
		this.verticale = verticale;
		this.verticale.getTeams().add(this);
	}

	public Team(String serial, Date start_date, String name, String niko_sticker_color,
				int niko_sticker_number, int niko_visibility, Boolean niko_privacy) {
		this(serial,start_date, name);
		this.niko_sticker_color = niko_sticker_color;
		this.niko_sticker_number = niko_sticker_number;
		this.niko_visibility = niko_visibility;
		this.niko_privacy = niko_privacy;
	}

	public Team(String serial, Date start_date, String name, Verticale verticale, String niko_sticker_color,
			 	int niko_sticker_number, int niko_visibility, Boolean niko_privacy) {
		this(serial,start_date, name, verticale);
		this.niko_sticker_color = niko_sticker_color;
		this.niko_sticker_number = niko_sticker_number;
		this.niko_visibility = niko_visibility;
		this.niko_privacy = niko_privacy;
	}

}
