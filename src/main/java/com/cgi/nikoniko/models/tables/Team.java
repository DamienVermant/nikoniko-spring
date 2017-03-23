package com.cgi.nikoniko.models.tables;

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

import com.cgi.nikoniko.models.association.UserHasTeam;
import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;

@Entity
@Table(name = Team.TABLE)
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
	public static final String[] FIELDS = { "id", "name", "serial", "start_date", "end_date","stickers_colors",
											"stickers_number", "visibility", "privacy", "verticale_id" };

	@Column(name = "name", nullable = true)
	private String name;

	@Column(name = "serial", nullable = false, unique = true)
	private String serial;

	@Column(name = "start_date", nullable = false)
	private Date start_date;

	@Column(name = "end_date", nullable = true)
	private Date end_date;

	@Column(name = "stickers_colors", nullable = false)
	private String stickers_colors;

	@Column(name = "stickers_number", nullable = false)
	private int stickers_number;

	@Column(name = "visibility", nullable = false)
	private int visibility;

	@Column(name = "privacy", nullable = false)
	private Boolean privacy;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="verticale_id")
	private Verticale verticale;

	@Transient
	@OneToMany
	private Set<UserHasTeam> userHasTeams;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name : the name to set
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
	 * @return stickers_color
	 */
	public String getStickers_colors() {
		return stickers_colors;
	}

	/**
	 *
	 * @param stickers_color
	 */
	public void setStickers_colors(String stickers_colors) {
		this.stickers_colors = stickers_colors;
	}

	/**
	 *
	 * @return stickers_number
	 */
	public int getStickers_number() {
		return stickers_number;
	}

	/**
	 *
	 * @param stickers_number
	 */
	public void setStickers_number(int stickers_number) {
		this.stickers_number = stickers_number;
	}

	/**
	 *
	 * @return visibility
	 */
	public int getVisibility() {
		return visibility;
	}

	/**
	 *
	 * @param visibility
	 */
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	/**
	 *
	 * @return privacy
	 */
	public Boolean getPrivacy() {
		return privacy;
	}

	/**
	 *
	 * @param privacy
	 */
	public void setPrivacy(Boolean privacy) {
		this.privacy = privacy;
	}

	/**
	 *
	 * @return the verticale
	 */
	public Verticale getVerticale() {
		return verticale;
	}

	/**
	 *
	 * @param verticale : the verticale to set
	 */
	public void setVerticale(Verticale verticale) {
		this.verticale = verticale;
	}

	/**
	 *
	 * @return userHasTeams
	 */
	public Set<UserHasTeam> getUserHasTeams() {
		return userHasTeams;
	}

	/**
	 *
	 * @param userHasTeams
	 */
	public void setUserHasTeams(ArrayList<UserHasTeam> userHasTeams) {
		this.userHasTeams = (Set<UserHasTeam>) userHasTeams;
	}

	public Team() {
		super(Team.TABLE, Team.FIELDS);
		this.stickers_colors = DEFAULT_COLORS;
		this.stickers_number = DEFAULT_NUMBER;
		this.visibility = DEFAULT_VISIBILITY;
		this.privacy = DEFAULT_PRIVACY;
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

	public Team(String serial, Date start_date, String name,
				String stickers_colors, int stickers_number, int visibility, Boolean privacy) {
		this(serial,start_date, name);
		this.stickers_colors = stickers_colors;
		this.stickers_number = stickers_number;
		this.visibility = visibility;
		this.privacy = privacy;
	}

	public Team(String serial, Date start_date, String name, Verticale verticale,
			 	String stickers_colors, int stickers_number, int visibility, Boolean privacy) {
		this(serial,start_date, name, verticale);
		this.stickers_colors = stickers_colors;
		this.stickers_number = stickers_number;
		this.visibility = visibility;
		this.privacy = privacy;
	}

}
