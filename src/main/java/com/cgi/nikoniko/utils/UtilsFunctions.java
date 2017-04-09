package com.cgi.nikoniko.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cgi.nikoniko.controllers.PathClass.PathFinder;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;

public abstract class UtilsFunctions {
	
	
public static LocalDate TODAY_DATE = new LocalDate();
	
	
/////////////////// UTILS FOR THE VOTE /////////////////////////////////
	
	
	/**
	 * FUNCTION FOR UPDATE THE PREVIOUS NIKONIKO VOTE BY USER
	 * @param idUser
	 * @param mood
	 * @param comment
	 * @return
	 */
	public static String updateLastNikoNiko(Long idUser, Integer mood, String comment, INikoNikoCrudRepository nikoCrud, IUserCrudRepository userCrud){
		
		if (UtilsFunctions.getLastLastNikoNikoMood(idUser, userCrud, nikoCrud) == false) {
			
			return PathFinder.REDIRECT + PathFinder.PATH + PathFinder.MENU_PATH;
		}

		if (mood == null) {
			
			return PathFinder.REDIRECT + PathFinder.PATH + PathFinder.MENU_PATH;
		}

		else {
			
			NikoNiko niko = UtilsFunctions.getLastLastNikoNiko(idUser, userCrud, nikoCrud);
			
			niko.setMood(mood);
			niko.setComment(comment);
			
			nikoCrud.save(niko);
			
			return PathFinder.REDIRECT + PathFinder.PATH + PathFinder.MENU_PATH;

		}
	}
	
	/**
	 * FUNCTION THAT CHECK NIKONIKO DATE FOR UPDATE OR NEW. TRUE : UPDATE NIKONIKO, FALSE : NEW NIKONIKO
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
	 * GET LAST MOOD ENTER BY USER
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
	 * GET SECOND TO LAST NIKONIKO USER AND CHECK IF THE MOOD IS NULL OR NOT.FALSE : CAN'T UPDATE, TRUE : UPDATE SECOND LAST
	 * @param idUser
	 * @param userCrud
	 * @return
	 */
	public static Boolean getLastLastNikoNikoMood(Long idUser, IUserCrudRepository userCrud, INikoNikoCrudRepository nikoCrud){
		
		LocalDate lastDay = TODAY_DATE.minusDays(1);

		NikoNiko niko = nikoCrud.getNikoDate(lastDay, idUser);
		
		if (niko == null) {
			
			return false;
		}
		
		else{
			
			if (niko.getMood() != 0) {
				
				return false;
			}
			
			else {
				
				return true;
			}
		}
	}
	
	/**
	 * GET SECOND TO LAST NIKONIKO IF EXIST AND MOOD != 0
	 * @param idUser
	 * @param userCrud
	 * @param nikoCrud
	 * @return
	 */
	public static NikoNiko getLastLastNikoNiko(Long idUser, IUserCrudRepository userCrud, INikoNikoCrudRepository nikoCrud){
		
		if (UtilsFunctions.getLastLastNikoNikoMood(idUser, userCrud, nikoCrud) == true) {
			
			LocalDate lastDay = TODAY_DATE.minusDays(1);
			
			NikoNiko niko = nikoCrud.getNikoDate(lastDay, idUser);
			
			return niko;	
		}
		
		return null;	
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
	
	
/////////////////// UTILS TO GET ROLES /////////////////////////////////

	
	/**
	 * HAVE ALL ROLES ASSOCIATED TO A USER
	 * @param idUser
	 * @return
	 */
	public static ArrayList<RoleCGI> setRolesForUserGet(Long idUser, IUserHasRoleCrudRepository userRoleCrud, IRoleCrudRepository roleCrud) {

		List<Long> ids = new ArrayList<Long>();
		ArrayList<RoleCGI> roleList = new ArrayList<RoleCGI>();

		List<BigInteger> idsBig = userRoleCrud.findAssociatedRole(idUser);

		if (!idsBig.isEmpty()) {
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());

			}
			roleList = (ArrayList<RoleCGI>) roleCrud.findAll(ids);
		}

		return roleList;
	}
	
	
/////////////////// UTILS TO GET USER'S INFORMATION /////////////////////////////////

	
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

		return user;
	}
}
