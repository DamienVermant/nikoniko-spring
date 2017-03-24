package com.cgi.nikoniko.models.association;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.cgi.nikoniko.models.association.base.AssociationItem;
import com.cgi.nikoniko.models.tables.FunctionCGI;
import com.cgi.nikoniko.models.tables.RoleCGI;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.cgi.nikoniko.models.association.base.AssociationItem;

@Entity
@Table(name = RoleHasFunction.TABLE)
public class RoleHasFunction extends AssociationItem {

	@Transient //naming convention : LeftTableName_has_RightTableName
	public static final String TABLE = "role_has_function";

	@Transient //FIELDS filling convention : {idLeft,idRight, Other attributes...}
	public static final String[] FIELDS = {"idLeft", "idRight"};

	@Transient
	@ManyToOne(cascade = CascadeType.ALL)
	private RoleCGI role;

	@Transient
	@ManyToOne(cascade = CascadeType.ALL)
	private FunctionCGI function;

	/**
	 *
	 * @return the role
	 */
	public RoleCGI getRole() {
		return role;
	}

	/**
	 *
	 * @return the function
	 */
	public FunctionCGI getFunction() {
		return function;
	}

	public RoleHasFunction() {
		super(RoleHasFunction.TABLE,RoleHasFunction.FIELDS);
	}

	public RoleHasFunction (RoleCGI role, FunctionCGI function) {
		super(RoleHasFunction.TABLE,RoleHasFunction.FIELDS, role, function);
		this.function = function;
		//this.function.getRoles().add(this);
		this.role = role;
		this.role.getFunctionCGI().add(this);
	}

}
