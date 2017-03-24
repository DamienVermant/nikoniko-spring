package com.cgi.nikoniko.models.tables;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.cgi.nikoniko.models.association.UserHasRole;
import com.cgi.nikoniko.models.association.UserHasTeam;
import com.cgi.nikoniko.models.tables.security.SecurityUser;

@Entity
@Table(name = User.TABLE)
public class User extends SecurityUser {

    public static final char SEX_MALE = 'M';
    public static final char SEX_FEMALE = 'F';
    public static final char SEX_UNDEFINNED = 'U';

	@Transient
	public static final String TABLE = "user";

	@Transient
	public static final String[] FIELDS = { "id", "lastname", "firstname", "sex", "registration_cgi",
											"login", "password", "enable", "verticale_id"};

	@Column(name = "lastname", nullable = false)
	private String lastname;

	@Column(name = "firstname", nullable = false)
	private String firstname;

	@Column(name = "sex", nullable = true)
	private char sex;

	@Column(name = "registration_number", nullable = false, unique = true)
	private String registration_cgi;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private Set<NikoNiko> nikoNikos;

	@Transient
	@OneToMany
	private Set<UserHasRole> roles;

	@Transient
	@OneToMany
	private Set<UserHasTeam> userHasTeams;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="verticale_id")
	private Verticale verticale;


	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname
	 *            the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname
	 *            the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 *
	 * @return the sex
	 */
	public char getSex() {
		return sex;
	}

	/**
	 *
	 * @param sex : the sex to set
	 */
	public void setSex(char sex) {
	    switch (sex) {
	    case User.SEX_MALE:
	    case User.SEX_FEMALE:
	    case User.SEX_UNDEFINNED:
	        this.sex = sex;
	        break;
        default:
            throw new InvalidParameterException();
	    }
	}

	/**
	 * @return the registration_cgi
	 */
	public String getRegistration_cgi() {
		return registration_cgi;
	}

	/**
	 * @param registration_cgi
	 *            the registration_cgi to set
	 */
	public void setRegistration_cgi(String registration_cgi) {
		this.registration_cgi = registration_cgi;
	}

	/**
	 * @return the nikoNikos
	 */
	public Set<NikoNiko> getNikoNikos() {
		return nikoNikos;
	}

	/**
	 * @param nikoNikos the nikoNikos to set
	 */
	public void setNikoNikos(ArrayList<NikoNiko> nikoNikos) {
		this.nikoNikos = (Set<NikoNiko>)nikoNikos;
	}

	/**
	 * @return the roles
	 */
	public Set<UserHasRole> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(ArrayList<UserHasRole> roles) {
		this.roles = (Set<UserHasRole>)roles;
	}

	/**
	 * @return the userHasTeams
	 */
	public Set<UserHasTeam> getUserHasTeams() {
		return userHasTeams;
	}

	/**
	 * @param userHasTeams : the userHasTeams to set
	 */
	public void setUserHasTeams(ArrayList<UserHasTeam> teamsHasUsers) {
		this.userHasTeams = (Set<UserHasTeam>)teamsHasUsers;
	}

	/**
	 * @return the verticale
	 */
	public Verticale getVerticale() {
		return verticale;
	}

	/**
	 * @param verticale : the verticale to set
	 */
	public void setVerticale(Verticale verticale) {
		this.verticale = verticale;
//		//TODO : mettre cette partie dans le getter peut donner un meilleur resultat
//		if (!this.verticale.getUsers().contains(this)) {
//			//TODO rajouter ce user a la verticale qui a ete set
//			this.verticale.getUsers().add(this);
//		}
	}

	public User() {
		super(User.TABLE, User.FIELDS);
	}

	public User (Verticale  verticale, String registration_cgi, String login, String password) {
		super(User.TABLE, User.FIELDS, login, password);
		this.registration_cgi = registration_cgi;
		this.verticale = verticale;
		this.verticale.getUsers().add(this);
	}

	public User(Verticale  verticale, String registration_cgi, String login, String password,
				String lastname, String firstname) {
		this(verticale, registration_cgi, login, password);
		this.lastname = lastname;
		this.firstname = firstname;
		this.sex = 'U';
	}

	public User(Verticale  verticale, String registration_cgi, String login, String password,
			String lastname, String firstname, char sex) {
		this(verticale, registration_cgi, login, password, lastname, firstname);
		this.sex = sex;
	}

}
