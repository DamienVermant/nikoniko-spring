package com.cgi.nikoniko.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

@Entity
@Table(name = "change_dates")
public class ChangeDates extends DatabaseItem {

		@Transient
		public static final String TABLE = "change_date";

		@Transient
		public static final String[] FIELDS = { "id", "changeDate", "nikoniko_id"};

		@Column(name = "change_date", nullable = true)
		private Date changeDate;

		@ManyToOne
		private NikoNiko nikoniko;

		/**
		 *
		 * @return changeDate
		 */
		public Date getChangeDate() {
			return changeDate;
		}

		/**
		 *
		 * @param changeDate
		 */
		public void setChangeDate(Date changeDate) {
			this.changeDate = changeDate;
		}

		/**
		 *
		 * @return nikoniko
		 */
		public NikoNiko getNikoniko() {
			return nikoniko;
		}

		/**
		 *
		 * @param nikoniko
		 */
		public void setNikoniko(NikoNiko nikoniko) {
			this.nikoniko = nikoniko;
		}

		public ChangeDates(){
			super(ChangeDates.TABLE, ChangeDates.FIELDS);
		}

		public ChangeDates(NikoNiko nikoniko) {
			this();
			this.changeDate = new Date();
			this.nikoniko = nikoniko;
			this.nikoniko.getChangeDates().add(this);
		}

		public ChangeDates(Date changeDate, NikoNiko nikoniko) {
			this(nikoniko);
			this.changeDate = changeDate;
//			this.nikoniko = nikoniko;
//			this.nikoniko.getChangeDates().add(this);
		}
}
