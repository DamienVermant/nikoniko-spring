package com.cgi.nikoniko.models.tables.security;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;
import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;

@MappedSuperclass
@Inheritance
public abstract class SecurityUser extends DatabaseItem {

	@Column(name = "login", nullable = false, unique = true)
	private String login;

	@Column(name = "password", nullable = false)
	private String password;

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login : the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password : the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public SecurityUser(String table, String[] fields, String login, String password) {
		super(table, fields);
		this.login = login;
		this.password = password;
	}

	public SecurityUser(String table, String[] fields) {
		super(table, fields);
	}
}
