package com.cgi.nikoniko.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.AssociationItem;

@Entity
@Table(name = "user_has_role")
public class UserHasRole extends AssociationItem {

	@Transient
	public static final String TABLE = "user_has_role";

	@Transient
	public static final String[] FIELDS = {"idLeft", "idRight"};

	@Transient
	@ManyToOne
	private User user;

	@Transient
	@ManyToOne
	private RoleCGI role;

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the role
	 */
	public RoleCGI getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(RoleCGI role) {
		this.role = role;
	}

	public UserHasRole() {
		super(UserHasRole.TABLE,UserHasRole.FIELDS);
	}

	public UserHasRole (User user, RoleCGI role) {
		super(UserHasRole.TABLE,UserHasRole.FIELDS, user, role);
		this.user = user;
		this.user.getRoles().add(this);
		this.role = role;
		this.role.getUsers().add(this);
	}
}
