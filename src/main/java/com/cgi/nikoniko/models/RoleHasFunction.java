package com.cgi.nikoniko.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.AssociationItem;

@Entity
@Table(name = "role_has_function")
public class RoleHasFunction extends AssociationItem {

	@Transient
	public static final String TABLE = "role_has_function";

	@Transient
	public static final String[] FIELDS = {"idLeft", "idRight"};

	@Transient
	@ManyToOne
	private RoleCGI role;

	@Transient
	@ManyToOne
	private FunctionCGI function;

	public RoleHasFunction() {
		super(RoleHasFunction.TABLE,RoleHasFunction.FIELDS);
	}

	public RoleHasFunction (RoleCGI role, FunctionCGI function) {
		super(RoleHasFunction.TABLE,RoleHasFunction.FIELDS, role, function);
		this.function = function;
		this.function.getRoles().add(this);
		this.role = role;
		this.role.getFunctionCGI().add(this);
	}
}
