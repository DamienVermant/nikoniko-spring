package com.cgi.nikoniko.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.cgi.nikoniko.models.modelbase.AssociationItem;

@Entity
@Table(name = "role_has_function")
public class RoleHasFunction extends AssociationItem {

	@Transient
	public static final String TABLE = "role_has_function";

	@Transient
	public static final String[] FIELDS = {"idLeft", "idRight"};

	@Transient
	@ManyToOne(cascade = CascadeType.ALL)
	private RoleCGI role;

	@Transient
	@ManyToOne(cascade = CascadeType.ALL)
	private FunctionCGI function;

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
