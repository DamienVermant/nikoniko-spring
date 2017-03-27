package com.cgi.nikoniko.models.tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;

@Entity
@Table(name = NikoNiko.TABLE)
public class NikoNiko extends DatabaseItem {

	@Transient
	public static final String TABLE = "nikoniko";

	@Transient
	public static final String[] FIELDS = { "id", "entry_date","change_date", "mood", "comment", "user_id"};

	@Column(name = "mood", nullable = false)
	private int mood;

	@Column(name = "entry_date", nullable = false)
	private Date entry_date;
	
	@Column(name = "change_date", nullable = true)
	private Date change_date;

	@Column(name = "nikoniko_comment", nullable = true)
	private String comment;

	@OneToMany(mappedBy = "nikoniko", cascade = CascadeType.REMOVE)
	private Set<ChangeDates> changeDates;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
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
	public void setMood(int mood) {//TODO : find a way to import sticker number from team (here default is 3)
		this.mood = NikoNikoSatisfaction.satisfactionRule(mood,3);
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
	public void setChangeDates(ArrayList<ChangeDates> changeDates) {
		this.changeDates = (Set<ChangeDates>)changeDates;
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
        if (!user.getNikoNikos().contains(this)) { // warning this may cause performance issues if you have a large data set since this operation is O(n)
            user.getNikoNikos().add(this);
            }
	}

	public NikoNiko() {
		super(NikoNiko.TABLE, NikoNiko.FIELDS);
	}

	public Date getChange_date() {
		return change_date;
	}

	public void setChange_date(Date change_date) {
		this.change_date = change_date;
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
	
	public NikoNiko(User user, int mood, Date entry_date, String comment, Date change_date) {
		this(user, mood, entry_date);
		this.comment = comment;
		this.change_date = change_date;
	}
	

	//Class to check if given satisfaction is valid with our configuration
	private static class NikoNikoSatisfaction {

		public static final int defaultSatisfactionError = 0;

		public static Boolean inSatisfactionItems(int satisfaction, int maxSatisfaction) {
			Boolean flag = false;
			for (int i = 0; i < maxSatisfaction+1; i++) {
				if (satisfaction == i) {
					flag = true;
					break;
				}
			}
			return flag;
		}

		public static int satisfactionRule(int satisfaction, int maxSatisfaction) {
			if (inSatisfactionItems(satisfaction, maxSatisfaction)) {
				return satisfaction;
			} else {
				String error = "Error satisfaction not in ";
				for (int i = 1; i < maxSatisfaction; i++) {
					error += i + ", ";
				}
				error += maxSatisfaction + ".";
				System.err.println(error);
				return defaultSatisfactionError;
			}
		}

	}

}
