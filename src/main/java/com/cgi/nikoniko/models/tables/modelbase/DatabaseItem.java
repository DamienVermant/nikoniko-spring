package com.cgi.nikoniko.models.tables.modelbase;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class DatabaseItem implements Serializable {

	@Transient
	public String table;

	@Transient
	public String[] fields;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 *
	 * @param id : the id to set
	 */
	public void setId(Long id) {
		if (id>0){
			this.id = id;
		}else {
			this.id = null;
		}
	}

	public DatabaseItem(String table, String[] fields) {
		this.table = table;
		this.fields = fields;
	}

	public DatabaseItem() {
	}

}
