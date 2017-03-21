package com.cgi.nikoniko.models.modelbase;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.AssociationItemId;

@MappedSuperclass
@IdClass(AssociationItemId.class)
public abstract class AssociationItem implements Serializable{

	@Transient
	public String table;

	@Transient
	public String[] fields;

	@Id
	private Long idLeft;

	@Id
	private Long idRight;

	/**
	 * @param idLeft the idLeft to set
	 */
	public void setIdLeft(Long idLeft) {
		this.idLeft = idLeft;
	}

	/**
	 * @return the idLeft
	 */
	public Long getIdLeft() {
		return idLeft;
	}

	/**
	 * @param idRight the idRight to set
	 */
	public void setIdRight(Long idRight) {
		this.idRight = idRight;
	}

	/**
	 * @return the idRight
	 */
	public Long getIdRight() {
		return idRight;
	}

	public AssociationItem(String table, String[] fields, DatabaseItem itemLeft, DatabaseItem itemRight) {
		this.table = table;
		this.fields = fields;
		this.idLeft = itemLeft.getId();
		this.idRight = itemRight.getId();
	}

	public AssociationItem() {
	}

	public AssociationItem(String table, String[] fields) {
		this.table = table;
		this.fields = fields;
	}
}
