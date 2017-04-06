package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.PathClass.PathFinder;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.User;

@Controller
public class MenuController  {

	public final static LocalDate TODAY_DATE = new LocalDate();
	public final static String BASE_URL = PathFinder.PATH + PathFinder.MENU_PATH;

	// TODO : CHANGE TYPE OF TIME
	public final static double TIME = 0.9999999999;

	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	IUserHasRoleCrudRepository userRoleCrud;

	@Autowired
	IRoleCrudRepository roleCrud;

	@Autowired
	INikoNikoCrudRepository nikoCrud;

	/**
	 * SHOW MENU
	 * @param model
	 * @param login
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = PathFinder.PATH + PathFinder.MENU_PATH, method = RequestMethod.GET)
	public String index(Model model, String login) {

		model.addAttribute("page","MENU");

		model.addAttribute("lastNiko",this.getLastLastNikoNikoMood(this.getUserInformations().getId()));
		model.addAttribute("status", this.checkDateNikoNiko(this.getUserInformations().getId()));
		model.addAttribute("mood", this.getUserLastMood(this.getUserInformations().getId()));

		model.addAttribute("roles",this.getConnectUserRole());
		model.addAttribute("auth",this.getUserInformations().getFirstname());
		model.addAttribute("go_own_nikoniko", PathFinder.PATH + "user" + PathFinder.PATH + this.getUserInformations().getId() + PathFinder.PATH + "link");
		model.addAttribute("add_nikoniko", PathFinder.PATH + "user" + PathFinder.PATH + this.getUserInformations().getId() + PathFinder.PATH + "add");
		model.addAttribute("pie_chart", PathFinder.PATH + "graph" + PathFinder.PATH + PathFinder.SHOW_GRAPH);

		model.addAttribute("go_users", PathFinder.GO_USERS);
		model.addAttribute("go_nikonikos", PathFinder.GO_NIKOS);
		model.addAttribute("go_teams", PathFinder.GO_TEAMS);
		model.addAttribute("go_roles", PathFinder.GO_ROLES);
		model.addAttribute("go_functions", PathFinder.GO_FUNCTIONS);
		model.addAttribute("go_verticales", PathFinder.GO_VERTICALE);
		model.addAttribute("go_graphes", PathFinder.GO_GRAPHE);
		model.addAttribute("calendar", PathFinder.GO_CALENDAR);

		model.addAttribute("go_user_has_team", PathFinder.GO_USERTEAM);
		model.addAttribute("go_user_has_role", PathFinder.GO_USERROLE);
		model.addAttribute("go_role_has_function", PathFinder.GO_ROLEFUNC);

		model.addAttribute("add_last", PathFinder.PATH + "user" + PathFinder.PATH + this.getUserInformations().getId() + PathFinder.PATH + "addLast");

		// TEST FOR SECURED REDIRECTION

		model.addAttribute("id",this.getUserInformations().getId());


		return PathFinder.MENU_PATH + PathFinder.PATH + "mainMenu";
	}

	/**
	 * RETURN THE HIGHEST ROLE FROM A USER
	 * @return
	 */
	public String getConnectUserRole(){

		String login = "";
		String role = "";
		User user = new User();
		ArrayList<RoleCGI> roleList = new ArrayList<RoleCGI>();
		ArrayList<String> roleNames = new ArrayList<String>();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		login = auth.getName();
		user = userCrud.findByLogin(login);
		roleList = this.setRolesForUserGet(user.getId());
		roleNames = this.convertObjectToString(roleList);

		if (roleNames.contains("ROLE_ADMIN")) {
			role = "admin";
		}
		else if (roleNames.contains("ROLE_VP")) {
			role = "vp";
		}
		else if (roleNames.contains("ROLE_CHEF_PROJET")) {
			role = "chefProjet";
		}
		else if (roleNames.contains("ROLE_GESTIONNAIRE")) {
			role = "gestionTeam";
		}
		else {
			role = "employee";
		}

		return role;
	}

	/**
	 * RETURN USER FROM AUTHENTIFICATION
	 * @return
	 */
	public User getUserInformations(){

		String login = "";
		User user = new User();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		login = auth.getName();
		user = userCrud.findByLogin(login);

		return user;
	}

	/**
	 * HAVE ALL ROLES ASSOCIATED TO A USER
	 * @param idUser
	 * @return
	 */
	public ArrayList<RoleCGI> setRolesForUserGet(Long idUser) {

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

	/**
	 * GET ROLE NAME FROM ROLE OBJECT
	 * @param roleList
	 * @return
	 */
	public ArrayList<String> convertObjectToString(ArrayList<RoleCGI> roleList){

		ArrayList<String> roleNames = new ArrayList<String>();
		for (int i = 0; i <roleList.size(); i++) {
			roleNames.add(roleList.get(i).getName());
		}
		return roleNames;
	}

	/**
	 * FUNCTION THAT CHECK NIKONIKO DATE FOR UPDATE OR NEW
	 * @param idUser
	 * @return
	 */
	public Boolean checkDateNikoNiko(Long idUser){

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
	public int getUserLastMood(Long idUser){

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
	 * @return
	 */
	public Boolean getLastLastNikoNikoMood(Long idUser){

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


}