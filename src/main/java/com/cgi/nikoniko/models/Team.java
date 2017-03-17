package com.cgi.nikoniko.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

public class Team extends DatabaseItem{

	@Transient
	public static final String TABLE = "team";
	@Transient
	public static final String[] FIELDS = { "id", "name", "serial", "start_date", "end_date",
		"niko_sticker_color", "niko_sticker_number", "niko_visibility", "niko_privacy" };

	@Column(name = "team_name", nullable = false)
	private String name;
	@Column(name = "team_serial", nullable = false)
	private String serial;
	@Column(name = "team_start_date", nullable = false)
	private Date start_date;
	@Column(name = "team_end_date", nullable = true)
	private Date end_date;
	@Column(name = "team_niko_sticker_color", nullable = false)
	private String niko_sticker_color;
	@Column(name = "team_niko_sticker_number", nullable = false)
	private int niko_sticker_number;
	@Column(name = "team_niko_visibility", nullable = false)
	private int niko_visibility;
	@Column(name = "team_niko_privacy", nullable = false)
	private Boolean niko_privacy;

	@ManyToOne(targetEntity = Verticale.class)
	private Verticale verticale;

	@OneToMany(targetEntity = TeamHasUser.class)
	private Set<TeamHasUser> teamHasUsers;

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

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getNiko_sticker_color() {
		return niko_sticker_color;
	}

	public void setNiko_sticker_color(String niko_sticker_color) {
		this.niko_sticker_color = niko_sticker_color;
	}

	public int getNiko_sticker_number() {
		return niko_sticker_number;
	}

	public void setNiko_sticker_number(int niko_sticker_number) {
		this.niko_sticker_number = niko_sticker_number;
	}

	public int getNiko_visibility() {
		return niko_visibility;
	}

	public void setNiko_visibility(int niko_visibility) {
		this.niko_visibility = niko_visibility;
	}

	public Boolean getNiko_privacy() {
		return niko_privacy;
	}

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
	public void setProjects(Verticale projects) {
		this.verticale = projects;
	}

	public Set<TeamHasUser> getTeamHasUsers() {
		return teamHasUsers;
	}

	public void setTeamHasUsers(Set<TeamHasUser> teamHasUsers) {
		this.teamHasUsers = teamHasUsers;
	}

	public Team(String name, String serial, Date start_date, Date end_date,
			String niko_sticker_color, int niko_sticker_number, int niko_visibility,
			Boolean niko_privacy, Verticale verticale) {
		super(Team.TABLE, Team.FIELDS);
		this.name = name;
		this.serial = serial;
		this.start_date = start_date;
		this.end_date = end_date;
		this.niko_sticker_color = niko_sticker_color;
		this.niko_sticker_number = niko_sticker_number;
		this.niko_visibility = niko_visibility;
		this.niko_privacy = niko_privacy;
		this.verticale = verticale;
	}

	public Team(String name, String serial, Date start_date,
			String niko_sticker_color, int niko_sticker_number, int niko_visibility,
			Boolean niko_privacy) {
		super(Team.TABLE, Team.FIELDS);
		this.name = name;
		this.serial = serial;
		this.start_date = start_date;
		this.niko_sticker_color = niko_sticker_color;
		this.niko_sticker_number = niko_sticker_number;
		this.niko_visibility = niko_visibility;
		this.niko_privacy = niko_privacy;
	}

	public Team() {
		super(Team.TABLE, Team.FIELDS);
	}
}
