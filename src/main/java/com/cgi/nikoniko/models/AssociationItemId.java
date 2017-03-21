package com.cgi.nikoniko.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class AssociationItemId implements Serializable {
	private Long idLeft;
	private Long idRight;

	/**
	 * @return the idLeft
	 */
	public Long getIdLeft() {
		return idLeft;
	}

	/**
	 * @return the idRight
	 */
	public Long getIdRight() {
		return idRight;
	}

	public AssociationItemId (Long idLeft, Long idRight){
		this.idLeft = idLeft;
		this.idRight = idRight;
	}

	public AssociationItemId (){

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idRight == null) ? 0 : idRight.hashCode());
		result = prime * result + Math.toIntExact(idLeft);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssociationItemId other = (AssociationItemId) obj;
		if (idRight == null) {
			if (other.idRight != null)
				return false;
		} else if (!idRight.equals(other.idRight))
			return false;
		if (idLeft != other.idLeft)
			return false;
		return true;
	}
}
