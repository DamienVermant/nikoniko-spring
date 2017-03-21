package com.cgi.nikoniko.models;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.security.SecurityUser;



@Entity
@Table(name = "user")
public class User extends SecurityUser {

    public static final char SEX_MALE = 'M';
    public static final char SEX_FEMALE = 'F';
    public static final char SEX_UNDEFINNED = 'U';

	@Transient
	public static final String TABLE = "user";

	@Transient
	public static final String[] FIELDS = { "id", "lastname", "firstname", "sex", "registration_cgi",
											"login", "password","verticale_id"};

	@Column(name = "user_lastname", nullable = false)
	private String lastname;

	@Column(name = "user_firstname", nullable = false)
	private String firstname;

	@Column(name = "user_sex", nullable = false)
	private char sex;

	@Column(name = "user_registration", nullable = false)
	private String registration_cgi;

	@OneToMany(mappedBy = "user")
	private Set<NikoNiko> nikoNikos;

	@ManyToMany
	@JoinTable(
		      name="user_role",
		      joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"),
		      inverseJoinColumns=@JoinColumn(name="role_id", referencedColumnName="id"))
	private Set<RoleCGI> roles;

	@OneToMany
	private Set<UserHasTeam> teamsHasUsers;

	@ManyToOne
	private Verticale verticale;

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @return the sex
	 */
	public char getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
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
	 * @return the roles
	 */
	public ArrayList<RoleCGI> getRoles() {
		return (ArrayList<RoleCGI>)roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(ArrayList<RoleCGI> roles) {
		this.roles = (Set<RoleCGI>)roles;
	}

	/**
	 * @return the teamsHasUsers
	 */
	public ArrayList<UserHasTeam> getTeamsHasUsers() {
		return (ArrayList<UserHasTeam>)teamsHasUsers;
	}

	/**
	 * @param teamsHasUsers the teamsHasUsers to set
	 */
	public void setTeamsHasUsers(ArrayList<UserHasTeam> teamsHasUsers) {
		this.teamsHasUsers = (Set<UserHasTeam>)teamsHasUsers;
	}

	/**
	 * @return the verticale
	 */
	public Verticale getVerticale() {
		return verticale;
	}

	/**
	 * @param verticale the verticale to set
	 */
	public void setVerticale(Verticale verticale) {
		this.verticale = verticale;
	}

	/**
	 * @param nikoNikos the nikoNikos to set
	 */
	public void setNikoNikos(Set<NikoNiko> nikoNikos) {
		this.nikoNikos = nikoNikos;
	}

	/**
	 * @return the nikoNikos
	 */
	public ArrayList<NikoNiko> getNikoNikos() {
		return (ArrayList<NikoNiko>)nikoNikos;
	}

	/**
	 * @param nikoNikos
	 *            the nikoNikos to set
	 */
	public void setNikoNikos(ArrayList<NikoNiko> nikoNikos) {
		this.nikoNikos = (Set<NikoNiko>)nikoNikos;
	}

	public User() {
		super(User.TABLE, User.FIELDS);
	}

	public User (Verticale  verticale, String registration_cgi, String login, String password) {
		super(User.TABLE, User.FIELDS, login, password);
		this.registration_cgi = registration_cgi;//TODO : implement verification of unicity of this attribute
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
