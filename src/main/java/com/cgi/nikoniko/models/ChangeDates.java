package com.cgi.nikoniko.models;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

@Entity
@Table(name = "change_dates")
public class ChangeDates extends DatabaseItem {
	
		// Nom de la table
		@Transient
		public static final String TABLE = "change_date";

		// Champs dans la table nikoniko
		@Transient
		public static final String[] FIELDS = { "id", "change_date", "id_nikoniko"};
		
		// Définitions des atrributs
		
		@Column(name = "changeDate", nullable = true)
		private Date changeDate;

		@ManyToOne
		private NikoNiko nikoniko;

		// Génération des getters et des setters 
		
		public Date getChangeDate() {
			return changeDate;
		}

		public void setChangeDate(Date changeDate) {
			this.changeDate = changeDate;
		}

		public NikoNiko getNikoniko() {
			return nikoniko;
		}

		public void setNikoniko(NikoNiko nikoniko) {
			this.nikoniko = nikoniko;
		}
		
		// Génération des constructeurs

		public ChangeDates(Date changeDate,
				NikoNiko nikoniko) {
			super(ChangeDates.TABLE, ChangeDates.FIELDS);
			this.changeDate = changeDate;
			this.nikoniko = nikoniko;
		}
}
