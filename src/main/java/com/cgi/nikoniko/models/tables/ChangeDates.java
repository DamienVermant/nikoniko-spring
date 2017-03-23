package com.cgi.nikoniko.models.tables;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;

@Entity
@Table(name = ChangeDates.TABLE)
public class ChangeDates extends DatabaseItem {

		@Transient
		public static final String TABLE = "change_dates";

		@Transient
		public static final String[] FIELDS = { "id", "changeDate", "nikoniko_id"};

		@Column(name = "change_date", nullable = false)
		private Date changeDate;

		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="nikoniko_id")
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

		public ChangeDates(Date changeDate, NikoNiko nikoniko) {
			this();
			this.changeDate = changeDate;
			this.nikoniko = nikoniko;
			this.nikoniko.getChangeDates().add(this);
		}

		public ChangeDates(NikoNiko nikoniko) {
			this(new Date(), nikoniko);
		}

}
