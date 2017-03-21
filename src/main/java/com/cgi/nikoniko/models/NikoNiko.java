package com.cgi.nikoniko.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

@Entity
@Table(name = "nikoniko")
public class NikoNiko extends DatabaseItem {

	@Transient
	public static final String TABLE = "nikoniko";

	@Transient
	public static final String[] FIELDS = { "id", "entry_date", "mood", "comment", "id_user"};

	@Column(name = "nikoniko_mood", nullable = false)
	private int mood;

	@Column(name = "nikoniko_entry_date", nullable = false)
	private Date entry_date;

	@Column(name = "nikoniko_comment", nullable = true)
	private String comment;

	@OneToMany(mappedBy = "nikoniko")
	private Set<ChangeDates> changeDates;

	@ManyToOne
	private User user;

	/**
	 *
	 * @return the mood
	 */
	public int getMood() {
		return mood;
	}

	/**
	 *
	 * @param mood
	 */
	public void setMood(int mood) {
		this.mood = NikoNikoSatisfaction.satisfactionRule(mood);
	}

	/**
	 * @return the entry_date
	 */
	public Date getEntry_date() {
		return entry_date;
	}

	/**
	 * @param entry_date the entry_date to set
	 */
	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}

	/**
	 *
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 *
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 *
	 * @return changeDates
	 */
	public Set<ChangeDates> getChangeDates() {
		return changeDates;
	}

	/**
	 *
	 * @param changeDates
	 */
	public void setChangeDates(Set<ChangeDates> changeDates) {
		this.changeDates = changeDates;
	}

	/**
	 *
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 *
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}


	public NikoNiko() {
		super(NikoNiko.TABLE, NikoNiko.FIELDS);
	}

	public NikoNiko(User user, int mood, Date entry_date) {
		this();
		this.user = user;
		this.user.getNikoNikos().add(this);
		this.setMood(mood);
		this.entry_date = entry_date;
	}

	public NikoNiko(User user, int mood) {
		this(user, mood, new Date());
	}

	public NikoNiko(User user, int mood, Date entry_date, String comment) {
		this(user, mood, entry_date);
		this.comment = comment;
	}

	private static class NikoNikoSatisfaction {

		public static final int[] satisfactionItems = { 1, 2, 3 };
		public static final int defaultSatisfactionError = 0;

		public static Boolean inSatisfactionItems(int satisfaction) {
			Boolean flag = false;
			for (int i = 0; i < satisfactionItems.length; i++) {
				if (satisfaction == satisfactionItems[i]) {
					flag = true;
					break;
				}
			}
			return flag;
		}

		public static int satisfactionRule(int satisfaction) {
			if (inSatisfactionItems(satisfaction)) {
				return satisfaction;
			} else {
				String error = "Error satisfaction not in ";
				for (int i = 0; i < satisfactionItems.length - 1; i++) {
					error += satisfactionItems[i] + ", ";
				}
				error += satisfactionItems[satisfactionItems.length - 1] + ".";
				System.err.println(error);
				return defaultSatisfactionError;
			}
		}

	}

}
