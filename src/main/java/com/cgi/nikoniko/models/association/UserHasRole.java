package com.cgi.nikoniko.models.association;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.cgi.nikoniko.models.association.base.AssociationItem;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.User;

@Entity
@Table(name = UserHasRole.TABLE)
public class UserHasRole extends AssociationItem {

	@Transient //naming convention : LeftTableName_has_RightTableName
	public static final String TABLE = "user_has_role";

	@Transient //FIELDS filling convention : {idLeft,idRight, Other attributes...}
	public static final String[] FIELDS = {"idLeft", "idRight"};

	@Transient
	@ManyToOne
	private User user;

	@Transient
	@ManyToOne
	private RoleCGI role;

	/**
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 *
	 * @return the role
	 */
	public RoleCGI getRole() {
		return role;
	}

	public UserHasRole() {
		super(UserHasRole.TABLE,UserHasRole.FIELDS);
	}

	public UserHasRole (User user, RoleCGI role) {
		super(UserHasRole.TABLE,UserHasRole.FIELDS, user, role);
		this.user = user;
		this.role = role;
	}

}
