package com.cgi.nikoniko.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

@Entity
@Table(name = "nikoniko")
public class NikoNiko extends DatabaseItem {
	
	// Nom de la table
	@Transient
	public static final String TABLE = "nikoniko";

	// Champs dans la table nikoniko
	@Transient
	public static final String[] FIELDS = { "id", "log_date", "mood", "comment", "id_user"};
	
	// Définition des attributs
	
	@Column(name = "mood", nullable = false)
	private int mood;
	
	@Column(name = "log_date", nullable = true)
	private Date log_date;
	
	@Column(name = "comment", nullable = true)
	private String comment;
	
	// Association avec change_dates
	@OneToMany
	private Set<ChangeDates> changeDates;
	
	// Association avec User
	@ManyToOne
	private User user;
	
	// Définition des getters et setters
	
	public NikoNiko(String table, String[] fields) {
		super(table, fields);
	}
	
	public int getMood() {
		return mood;
	}

	public void setMood(int mood) {
		this.mood = mood;
	}

	public Date getLog_date() {
		return log_date;
	}

	public void setLog_date(Date log_date) {
		this.log_date = log_date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<ChangeDates> getChangeDates() {
		return changeDates;
	}

	public void setChangeDates(Set<ChangeDates> changeDates) {
		this.changeDates = changeDates;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// Définition des constructeurs

	public NikoNiko(int mood, Date log_date, String comment, User user) {
		super(NikoNiko.TABLE, NikoNiko.FIELDS);
		this.mood = mood;
		this.log_date = log_date;
		this.comment = comment;
		this.user= user;
	}
	
	
	
}
