package com.cgi.nikoniko.utils;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.dao.IVerticaleCrudRepository;
import com.cgi.nikoniko.dao.base.IBaseAssociatedCrudRepository;
import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;
import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;

public abstract class UtilsFunctions {
	
	// need to call class constant
	
	public final static String DOT = ".";
	public final static String PATH = "/";
	public final static String BASE_USER = "user";
	public final static String VERTICALE = "verticale";
	public final static String BASE_URL = PATH + BASE_USER;

	public final static String SHOW_PATH = "show";
	public final static String MENU_PATH = "menu";

	public final static String SHOW_NIKONIKO = "showNikoNikos";
	public final static String SHOW_GRAPH = "showGraph";
	public final static String SHOW_TEAM = "showTeam";
	public final static String SHOW_ROLE = "showRole";
	public final static String SHOW_LINK = "link";
	public final static String SHOW_VERTICAL = "showVerticale";

	public final static String ADD_TEAM = "addTeams";
	public final static String ADD_ROLE = "addRoles";
	public final static String ADD_VERTICAL = "addVerticale";

	public final static String REDIRECT = "redirect:";

	public final static LocalDate TODAY_DATE = new LocalDate();

	public final static double TIME = 0.999999;
	
	
/////////////////// UTILS FOR THE VOTE /////////////////////////////////
	
	
	/**
	 * FUNCTION FOR UPDATE THE PREVIOUS NIKONIKO VOTE BY USER
	 * @param idUser
	 * @param mood
	 * @param comment
	 * @return
	 */
	public static String updateLastNikoNiko(Long idUser, Integer mood, String comment, INikoNikoCrudRepository nikoCrud, IUserCrudRepository userCrud){
		
		if (userCrud.getLastLastNikoNikoUser(idUser) == null) {
			return REDIRECT + PATH + MENU_PATH;
		}

		if (mood == null) {
			return REDIRECT + PATH + MENU_PATH;
		}

		else {

			NikoNiko lastNiko = nikoCrud.findOne(userCrud.getLastLastNikoNikoUser(idUser));

			lastNiko.setMood(mood);
			lastNiko.setComment(comment);
			nikoCrud.save(lastNiko);

			return REDIRECT + PATH + MENU_PATH;

		}
	}
	
	/**
	 * FUNCTION THAT CHECK NIKONIKO DATE FOR UPDATE OR NEW
	 * @param idUser
	 * @return
	 */
	public static Boolean checkDateNikoNiko(Long idUser, IUserCrudRepository userCrud, INikoNikoCrudRepository nikoCrud){

		Boolean updateNiko = true;

		Long idMaxNiko = userCrud.getLastNikoNikoUser(idUser);

		if (idMaxNiko == null) {

			updateNiko = false;
		}

		else {

			NikoNiko lastNiko = nikoCrud.findOne(idMaxNiko);

			Date entryDate = lastNiko.getEntryDate();
			LocalDate dateEntry = new LocalDate(entryDate);

			if (entryDate == null || (TODAY_DATE.isAfter(dateEntry))) {
				updateNiko = false;
			}
		}

		return updateNiko;
	}

	/**
	 * GET LAST NIKONIKO ENTER BY USER
	 * @param idUser
	 * @return
	 */
	public static int getUserLastMood(Long idUser, IUserCrudRepository userCrud, INikoNikoCrudRepository nikoCrud){

		int mood = 0;

		Long idMax = userCrud.getLastNikoNikoUser(idUser);

		if (idMax == null) {
			return mood;
		}

		else {
			mood = nikoCrud.findOne(idMax).getMood();
			return mood;
		}
	}

	/**
	 * GET LAST-1 NIKONIKO USER AND CHECK IF THE MOOD IS NULL OR NOT
	 * RETURN FALSE CAN'T UPDATE, TRUE UPDATE SECOND LAST
	 * @param idUser
	 * @param userCrud2 
	 * @return
	 */
	public static Boolean getLastLastNikoNikoMood(Long idUser, IUserCrudRepository userCrud, INikoNikoCrudRepository nikoCrud){
		
		Long idMax = userCrud.getLastLastNikoNikoUser(idUser);

		if (idMax == null) {
			return false;
		}

		else {

			NikoNiko lastLastNiko = nikoCrud.findOne(userCrud.getLastLastNikoNikoUser(idUser));
			NikoNiko lastNiko = nikoCrud.findOne(userCrud.getLastNikoNikoUser(idUser));

			LocalDate dateEntryLast = new LocalDate(lastNiko.getEntryDate());
			LocalDate dateEntryLastLast = new LocalDate(lastLastNiko.getEntryDate());

			int day = Days.daysBetween(new LocalDate(dateEntryLastLast), new LocalDate(dateEntryLast)).getDays() ;


			if (lastLastNiko.getMood() != 0 || day < 1) {
				return false;
			}

			else {
				return true;
			}
		}
	}
	
	/**
	 * RETURN USER FROM AUTHENTIFICATION
	 * @return
	 */
	public static User getUserInformations(IUserCrudRepository userCrud){

		String login = "";
		User user = new User();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		login = auth.getName();
		user = userCrud.findByLogin(login);
		//user= userCrud.getUser(login);

		return user;
	}

	
/////////////////// UTILS TO SEARCH /////////////////////////////////
	
	
	/**
	 * FIND A SPECIFIC USER
	 * @param name
	 * @return
	 */
	public static ArrayList<User> searchUser(String name, IUserCrudRepository userCrud){

		ArrayList<User> userList = new ArrayList<User>();
		userList = userCrud.getUsers(name);

		return userList;

	}
	
	/**
	 * FIND A SPECIFIC TEAM
	 * @param name
	 * @return
	 */
	public static ArrayList<Team> searchTeam(String name, ITeamCrudRepository teamCrud){

		ArrayList<Team> teamList = new ArrayList<Team>();
		teamList = teamCrud.getTeams(name);

		return teamList;

	}
	

}
