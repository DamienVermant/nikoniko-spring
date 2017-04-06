package com.cgi.nikoniko.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cgi.nikoniko.controllers.PathClass.PathFinder;
import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.dao.IVerticaleCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;

@Controller
@RequestMapping(GraphController.BASE_URL)
public class GraphController extends ViewBaseController<User>{

	public final static String BASE_GRAPH = "graph";
	public final static String BASE_URL = PathFinder.PATH + BASE_GRAPH;


	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	IUserHasRoleCrudRepository userRoleCrud;

	@Autowired
	IRoleCrudRepository roleCrud;

	@Autowired
	INikoNikoCrudRepository nikonikoCrud;

	@Autowired
	IVerticaleCrudRepository verticaleCrud;

	@Autowired
	ITeamCrudRepository teamCrud;

	@Autowired
	IUserHasTeamCrudRepository userTeamCrud;

	public GraphController() {
		super(User.class,BASE_URL);
	}

	public GraphController(Class<User> clazz, String baseURL) {
		super(clazz, baseURL);
	}

	/**
	 * Récupère toutes les teams d'un utilisateur
	 * @param idValue
	 * @return
	 */
	public ArrayList<Team> findAllTeamsForUser(Long idValue) {

		List<Long> ids = new ArrayList<Long>();
		ArrayList<Team> teamList = new ArrayList<Team>();

		List<BigInteger> idsBig = userTeamCrud.findAssociatedTeam(idValue);

		if (!idsBig.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());
			}
			teamList = (ArrayList<Team>) teamCrud.findAll(ids);
		}

		return teamList;
	}

	/**
	 * recupère le dernier nikoniko d'un utilisateur
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
			mood = nikonikoCrud.findOne(idMax).getMood();
			return mood;
		}

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

/////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
*
* GRAPH GESTION
*
*
*/

/////////////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
*
* GRAPH JUST ONE DAY
*
*
*/

/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * ALL NIKONIKO GRAPH FOR AN USER FROM TODAY
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = PathFinder.PATH + PathFinder.SHOW_GRAPH, method = RequestMethod.GET)
	public String showPie(Model model) {

		Long idUser = this.getUserInformations().getId();
		User user = super.getItem(idUser);
		user.getRoles();
		Set<NikoNiko> niko =  user.getNikoNikos();
		List<NikoNiko> listOfNiko = new ArrayList<NikoNiko>(niko);
		List<NikoNiko> nikotoday = getNikoToday(listOfNiko);

		String role = testRole(idUser);

		int good = 0;
		int medium = 0;
		int bad = 0;

		///////////////////////////////////////////////////////////////////////////////
		//	Sélectionne dans une liste de satisfaction le niveau de satisfaction et	 //
		//	l'enregistre dans une variable qu'on transmet à la page					 //
		///////////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < nikotoday.size(); i++) {
			if (nikotoday.get(i).getMood() == 3) {
				good++;
			}else if(nikotoday.get(i).getMood() == 2){
				medium++;
			}else if(nikotoday.get(i).getMood() == 1){
				bad++;
			}
		}

		model.addAttribute("title", "Mes votes d'aujourd'hui" );
		model.addAttribute("role", role);
		model.addAttribute("mood", this.getUserLastMood(userCrud.findByLogin(super.checkSession().getName()).getId()));
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);
		return "graphs" + PathFinder.PATH + "pie";
	}

	/**
	 * ALL NIKONIKO GRAPH FOR AN USER FROM A PRECISE DATE
	 * @param model
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = PathFinder.SHOW_GRAPH + PathFinder.PATH + "{year}" +
					PathFinder.PATH + "{month}" + PathFinder.PATH + "{day}", method = RequestMethod.GET)
	public String showPieWithDate(Model model, @PathVariable int year,
								@PathVariable int month, @PathVariable int day) {

		Long idUser = this.getUserInformations().getId();
		User user = super.getItem(idUser);
		Set<NikoNiko> niko =  user.getNikoNikos();
		List<NikoNiko> listOfNiko = new ArrayList<NikoNiko>(niko);
		List<NikoNiko> nikotoday = getNikoPreciseDate(listOfNiko, year, month, day);

		String role = testRole(idUser);

		int good = 0;
		int medium = 0;
		int bad = 0;

		///////////////////////////////////////////////////////////////////////////////
		//	Sélectionne dans une liste de satisfaction le niveau de satisfaction et	 //
		//	l'enregistre dans une variable qu'on transmet à la page					 //
		///////////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < nikotoday.size(); i++) {
			if (nikotoday.get(i).getMood() == 3) {
				good++;
			}else if(nikotoday.get(i).getMood() == 2){
				medium++;
			}else{
				bad++;
			}
		}

		model.addAttribute("title", "Mes votes de la journée du : " + day + " " + getMonthLetter(month) + " " + year );
		model.addAttribute("role", role);
		model.addAttribute("mood", this.getUserLastMood(userCrud.findByLogin(super.checkSession().getName()).getId()));
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);
		return "graphs" + PathFinder.PATH + "piedate";
	}

	/**
	 * ALL NIKONIKOS GRAPH FROM TODAY
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = PathFinder.SHOW_GRAPH_ALL, method = RequestMethod.GET)
	public String showAllPie(Model model) {

		Long idUser = this.getUserInformations().getId();

		List<NikoNiko> listOfNikoall = (List<NikoNiko>) nikonikoCrud.findAll();
		List<NikoNiko> listOfNiko = getNikoToday(listOfNikoall);
		String role = testRole(idUser);

		int nbMood = 0;

		if (!listOfNiko.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			nbMood = 1;
		}

		int good = 0;
		int medium = 0;
		int bad = 0;

		///////////////////////////////////////////////////////////////////////////////
		//	Sélectionne dans une liste de satisfaction le niveau de satisfaction et	 //
		//	l'enregistre dans une variable qu'on transmet à la page					 //
		///////////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < listOfNiko.size(); i++) {
			if (listOfNiko.get(i).getMood() == 3) {
				good++;
			}else if(listOfNiko.get(i).getMood() == 2){
				medium++;
			}else if(listOfNiko.get(i).getMood() == 1){
				bad++;
			}
		}

		model.addAttribute("title", "Tous les votes d'aujourd'hui");
		model.addAttribute("role", role);
		model.addAttribute("mood", nbMood);
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);
		return "graphs" + PathFinder.PATH + "pie";
	}

	/**
	 * ALL NIKONIKOS GRAPH FROM A PRECISE DATE
	 * @param model
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = PathFinder.SHOW_GRAPH_ALL + PathFinder.PATH + "{year}" + PathFinder.PATH + "{month}" + PathFinder.PATH + "{day}", method = RequestMethod.GET)
	public String showAllPieWithDate(Model model, @PathVariable int year,
			@PathVariable int month, @PathVariable int day) {

		Long idUser = this.getUserInformations().getId();

		List<NikoNiko> listOfNikoall = (List<NikoNiko>) nikonikoCrud.findAll();
		List<NikoNiko> listOfNiko = getNikoPreciseDate(listOfNikoall, year, month, day);
		String role = testRole(idUser);

		int nbMood = 0;

		if (!listOfNiko.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			nbMood = 1;
		}

		int good = 0;
		int medium = 0;
		int bad = 0;

		///////////////////////////////////////////////////////////////////////////////
		//	Sélectionne dans une liste de satisfaction le niveau de satisfaction et	 //
		//	l'enregistre dans une variable qu'on transmet à la page					 //
		///////////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < listOfNiko.size(); i++) {
			if (listOfNiko.get(i).getMood() == 3) {
				good++;
			}else if(listOfNiko.get(i).getMood() == 2){
				medium++;
			}else if(listOfNiko.get(i).getMood() == 1){
				bad++;
			}
		}

		model.addAttribute("title", "Tous les votes de la journée du : " + day + " " + getMonthLetter(month) + " " + year);
		model.addAttribute("role", role);
		model.addAttribute("mood", nbMood);
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);
		return "graphs" + PathFinder.PATH + "piedate";
	}

	/**
	 * NikoNiko associated from Verticale from today
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = PathFinder.SHOW_GRAPH_VERTICALE, method = RequestMethod.GET)
	public String getNikoFromVerticale(Model model){

		Long userId = this.getUserInformations().getId();
		int nbMood = 0;
		User user = super.getItem(userId);
		Long verticaleId = user.getVerticale().getId();

		List<NikoNiko> listOfNikoall = findNikoNikosOfAVerticaleList(verticaleId);
		List<NikoNiko> listNiko = getNikoToday(listOfNikoall);

		String role = testRole(userId);

		int good = 0;
		int medium = 0;
		int bad = 0;

		///////////////////////////////////////////////////////////////////////////////
		//	Sélectionne dans une liste de satisfaction le niveau de satisfaction et	 //
		//	l'enregistre dans une variable qu'on transmet à la page					 //
		///////////////////////////////////////////////////////////////////////////////
		if(listNiko.size() != 0){
			nbMood = 1;
			for (int i = 0; i < listNiko.size(); i++) {
				if (listNiko.get(i).getMood() == 3) {
					good++;
				}else if(listNiko.get(i).getMood() == 2){
					medium++;
				}else if(listNiko.get(i).getMood() == 1){
					bad++;
				}
			}
		}

		model.addAttribute("title", "Tous les votes d'aujourd'hui pour la verticale : " + verticaleCrud.findOne(verticaleId).getName());
		model.addAttribute("role", role);
		model.addAttribute("mood", nbMood);
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);
		return "graphs" + PathFinder.PATH + "pie";
	}

	/**
	 * NikoNiko associated from Verticale from a precise date
	 * @param model
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	@RequestMapping(path = PathFinder.SHOW_GRAPH_VERTICALE + PathFinder.PATH + "{year}" + PathFinder.PATH + "{month}" + PathFinder.PATH + "{day}", method = RequestMethod.GET)
	public String getNikoFromVerticaleWithDate(Model model, @PathVariable int year,
			@PathVariable int month, @PathVariable int day){

		Long userId = this.getUserInformations().getId();
		int nbMood = 0;
		User user = super.getItem(userId);
		Long verticaleId = user.getVerticale().getId();

		List<NikoNiko> listOfNikoall = findNikoNikosOfAVerticaleList(verticaleId);
		List<NikoNiko> listNiko = getNikoPreciseDate(listOfNikoall, year, month, day);

		String role = testRole(userId);

		int good = 0;
		int medium = 0;
		int bad = 0;

		///////////////////////////////////////////////////////////////////////////////
		//	Sélectionne dans une liste de satisfaction le niveau de satisfaction et	 //
		//	l'enregistre dans une variable qu'on transmet à la page					 //
		///////////////////////////////////////////////////////////////////////////////
		if(listNiko.size() != 0){
			nbMood = 2;
			for (int i = 0; i < listNiko.size(); i++) {
				if (listNiko.get(i).getMood() == 3) {
					good++;
				}else if(listNiko.get(i).getMood() == 2){
					medium++;
				}else if(listNiko.get(i).getMood() == 1){
					bad++;
				}
			}
		}

		model.addAttribute("title", "Tous les votes du : " + day + " " + getMonthLetter(month) + " " + year + ", pour la verticale : " + verticaleCrud.findOne(verticaleId).getName());
		model.addAttribute("role", role);
		model.addAttribute("mood", nbMood);
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);
		return "graphs" + PathFinder.PATH + "piedate";
	}

	/**
	 * SHOW NikoNiko FROM team (TODAY)
	 * @param model
	 * @param nbTable
	 * @return
	 */
	@RequestMapping(path = PathFinder.SHOW_GRAPH_TEAM + PathFinder.PATH + "{nbTable}", method = RequestMethod.GET)
	public String getNikoFromTeam(Model model, @PathVariable int nbTable){

		ArrayList<Team> teamList = new ArrayList<Team>();
		ArrayList<String> teamName = new ArrayList<String>();

		Long userId = this.getUserInformations().getId();
		teamList = findAllTeamsForUser(userId);
		String role = testRole(userId);

		for (int i = 0; i < teamList.size(); i++) {
			teamName.add(teamList.get(i).getName());
		}

		Long teamId = teamList.get(nbTable).getId();

		List<BigInteger> listId = teamCrud.getNikoNikoFromTeam(teamId);
		List<Long> listNikoId = new ArrayList<Long>();
		List<NikoNiko> listNikoall = new ArrayList<NikoNiko>();
		int nbMood = 0;

		if (!listId.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			nbMood = 1;
			for (BigInteger id : listId) {
				listNikoId.add(id.longValue());
			}
			listNikoall =  (List<NikoNiko>) nikonikoCrud.findAll(listNikoId);
		}

		List<NikoNiko> listNiko = getNikoToday(listNikoall);

		int good = 0;
		int medium = 0;
		int bad = 0;

		///////////////////////////////////////////////////////////////////////////////
		//	Sélectionne dans une liste de satisfaction le niveau de satisfaction et	 //
		//	l'enregistre dans une variable qu'on transmet à la page					 //
		///////////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < listNiko.size(); i++) {
			if (listNiko.get(i).getMood() == 3) {
				good++;
			}else if(listNiko.get(i).getMood() == 2){
				medium++;
			}else if(listNiko.get(i).getMood() == 1){
				bad++;
			}
		}

		model.addAttribute("title", "Tous les votes d'aujourd'hui pour l'equipe : " + teamCrud.findOne(teamId).getName());
		model.addAttribute("role", role);
		model.addAttribute("nameteam", teamName);
		model.addAttribute("name", teamName);
		model.addAttribute("mood", nbMood);
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);

		return "graphs" + PathFinder.PATH + "pieTeam";
	}

	/**
	 * SHOW NikoNiko FROM team (precise date)
	 * @param model
	 * @param nbTable
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	@RequestMapping(path = PathFinder.SHOW_GRAPH_TEAM + PathFinder.PATH + "{nbTable}" + PathFinder.PATH + "{year}" + PathFinder.PATH + "{month}" + PathFinder.PATH + "{day}", method = RequestMethod.GET)
	public String getNikoFromTeamWithDate(Model model, @PathVariable int nbTable, @PathVariable int year,
			@PathVariable int month, @PathVariable int day){

		ArrayList<Team> teamList = new ArrayList<Team>();
		ArrayList<String> teamName = new ArrayList<String>();

		Long userId = this.getUserInformations().getId();
		teamList = findAllTeamsForUser(userId);
		String role = testRole(userId);

		for (int i = 0; i < teamList.size(); i++) {
			teamName.add(teamList.get(i).getName());
		}

		Long teamId = teamList.get(nbTable).getId();

		List<BigInteger> listId = teamCrud.getNikoNikoFromTeam(teamId);
		List<Long> listNikoId = new ArrayList<Long>();
		List<NikoNiko> listNikoall = new ArrayList<NikoNiko>();
		int nbMood = 0;

		if (!listId.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			nbMood = 1;
			for (BigInteger id : listId) {
				listNikoId.add(id.longValue());
			}
			listNikoall =  (List<NikoNiko>) nikonikoCrud.findAll(listNikoId);
		}

		List<NikoNiko> listNiko = getNikoPreciseDate(listNikoall, year, month, day);

		int good = 0;
		int medium = 0;
		int bad = 0;

		///////////////////////////////////////////////////////////////////////////////
		//	Sélectionne dans une liste de satisfaction le niveau de satisfaction et	 //
		//	l'enregistre dans une variable qu'on transmet à la page					 //
		///////////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < listNiko.size(); i++) {
			if (listNiko.get(i).getMood() == 3) {
				good++;
			}else if(listNiko.get(i).getMood() == 2){
				medium++;
			}else if(listNiko.get(i).getMood() == 1){
				bad++;
			}
		}

		model.addAttribute("title", "Tous les votes du : " + day + " " + getMonthLetter(month) + " " + year + " pour l'equipe : " + teamCrud.findOne(teamId).getName());
		model.addAttribute("role", role);
		model.addAttribute("nameteam", teamName);
		model.addAttribute("mood", nbMood);
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);

		return "graphs" + PathFinder.PATH + "pieTeamdate";
	}

	/**
	 * Get nikoniko's list from today
	 * @param listOfNiko
	 * @return
	 */
	public List<NikoNiko> getNikoToday(List<NikoNiko> listOfNiko){

		LocalDate nikodate = new LocalDate();
		LocalDate date;
		List<NikoNiko> nikotoday = new ArrayList<NikoNiko>();

		for (int i = 0; i < listOfNiko.size(); i++) {
			Date firstniko = listOfNiko.get(i).getEntryDate();
			nikodate = new LocalDate(firstniko);
			date = new LocalDate();
			if (nikodate.isEqual(date)) {
				nikotoday.add(listOfNiko.get(i));
			}
		}

		return nikotoday;
	}

	/**
	* Get nikoniko's list from one day of the year
	* @param listOfNiko
	* @param year
	* @param month
	* @param day
	* @return
	*/
	public List<NikoNiko> getNikoPreciseDate(List<NikoNiko> listOfNiko, int year, int month, int day){

	LocalDate nikodate = new LocalDate();
	LocalDate date = new LocalDate().withYear(year).withMonthOfYear(month).withDayOfMonth(day);
	List<NikoNiko> niko = new ArrayList<NikoNiko>();

	for (int i = 0; i < listOfNiko.size(); i++) {
		Date firstniko = listOfNiko.get(i).getEntryDate();
		nikodate = new LocalDate(firstniko);
		if (nikodate.isEqual(date)) {
			niko.add(listOfNiko.get(i));
		}
	}

	return niko;
	}

	public String getMonthLetter (int month){

		String monthNames[] = {"Janvier","Fevrier","Mars","Avril","Mai","Juin","Juillet","Aout","Septembre","Octobre","Novembre","Decembre"};
		String monthName = monthNames[month-1];

		return monthName;
	}
	/**
	 * Recupère les nikonikos par rapport à une verticale
	 * @param idVert
	 * @return
	 */
	public List<NikoNiko> findNikoNikosOfAVerticaleList(Long idVert){

		List<NikoNiko> vertNikonikos = new ArrayList<NikoNiko>();

		List<Team> vertTeams = new ArrayList<Team>();

		int nbMood = 0;

		if (!verticaleCrud.findOne(idVert).getTeams().isEmpty()) {
			vertTeams.addAll(verticaleCrud.findOne(idVert).getTeams());

			for (Team team : vertTeams) {
				vertNikonikos.addAll(findNikoNikosOfATeam(team.getId()));
			}
		}
		return vertNikonikos;
	}

	/**
	 * Recupère les roles d'un utilisateur
	 * @param idUser
	 * @return
	 */
	public String testRole(Long idUser){

		String role;

		List<Long> ids = new ArrayList<Long>();
		ArrayList<RoleCGI> roleList = new ArrayList<RoleCGI>();

		List<BigInteger> idsBig = userRoleCrud.findAssociatedRole(idUser);

		if (!idsBig.isEmpty()) {
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());

			}
			roleList = (ArrayList<RoleCGI>) roleCrud.findAll(ids);
		}

		ArrayList<String> roleNames = new ArrayList<String>();
		for (int i = 0; i <roleList.size(); i++) {
			roleNames.add(roleList.get(i).getName());
		}

		if (roleNames.contains("ROLE_ADMIN")) {
			role = "admin";
		}
		else if (roleNames.contains("ROLE_VP")) {
			role = "vp";
		}
		else {
			role = "employee";
		}
		return role;
	}

	/**
	 * ERWAN CHANGES
	 */

	/**
	 *
	 * @param model	:
	 * @param month	: Month number
	 * @param year	: Year number
	 * @param action: Used to select the month to show from the current one (previous or next)
	 * @return 		: Calendar view of all nikonikos of a team shown per day for a given month
	 * @throws IOException
	 */
	@RequestMapping(path = "nikoniko"+ PathFinder.PATH + "month", method = RequestMethod.GET)
	public String nikoNikoCalendar(Model model,
			@RequestParam(defaultValue = "null") String month,
			@RequestParam(defaultValue = "null") String year,
			@RequestParam(defaultValue = "") String action,
			HttpServletResponse response) throws IOException {

		//TODO : Check if the calendar for this idUser can be see by the user of the current session
		Long idUser = userCrud.findByLogin(super.checkSession().getName()).getId();

		try {
			userCrud.findOne(idUser).getLogin();
		} catch (Exception e) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),("This user doesn't exist!").toUpperCase());
			return "";
		}

		//##################################################################
		//Initialisation
		//##################################################################
		LocalDate dateLocale = LocalDate.now();

		String[] moisAnnee = {	"Janvier","Fevrier","Mars","Avril","Mai","Juin",
								"Juillet","Aout","Septembre","Octobre","Novembre","Décembre"};
		String[] jourSemaine = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"};

		int firstWeekUncomplete = 0;
		int lastWeekUncomplete = 0;
		int numberOfWeekInMonth = 1;
		int currentMonth = dateLocale.getMonthOfYear();
		int currentYear = dateLocale.getYear();
		int monthToUse = currentMonth;
		int yearToUse = currentYear;

		Boolean uncompleteWeek = true;
		Boolean monthIsAccepted = true;
		Boolean yearIsAccepted = true;

		List<Integer> nbWeeks = new ArrayList<Integer>();
		nbWeeks.add(numberOfWeekInMonth);

		ArrayList<Map<String,Object>> days = new ArrayList<Map<String,Object>>();
		ArrayList<NikoNiko> nikos = new ArrayList<NikoNiko>();

		nikos.addAll(userCrud.findOne(idUser).getNikoNikos());

		//###################################################################
		//#Check if given requestPAram values of month and year are integers#
		//###################################################################

		try {
			Integer.parseInt(month);
		} catch (Exception e) {
			monthIsAccepted = false;
		}

		try {
			Integer.parseInt(year);
		} catch (Exception e) {
			yearIsAccepted = false;
		}

		//##################################################################################
		//#Switch to the selected month and year (or default value if incorrect input data)#
		//##################################################################################

		if (action.equals("previous")) {
			if (monthIsAccepted) {
				monthToUse = Integer.parseInt(month) - 1;

				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}

				if (monthToUse == 0) {//January=>December
					monthToUse = 12;
					if (yearIsAccepted) {
						yearToUse = Integer.parseInt(year) - 1;
					} else {
						yearToUse = currentYear - 1;
					}
				}
			} else {
				monthToUse = currentMonth;
				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}
			}
		}else if (action.equals("next")) {
			if (monthIsAccepted) {
				monthToUse = Integer.parseInt(month) + 1;

				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}

				if (monthToUse == 13) {//December=>January
					monthToUse = 1;
					if (yearIsAccepted) {
						yearToUse = Integer.parseInt(year) + 1;
					} else {
						yearToUse = currentYear + 1;
					}
				}
			} else {
				//Prevoir un throw error 400
				monthToUse = currentMonth;
				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}
			}
		} else {
			monthToUse = currentMonth;
			yearToUse = currentYear;
		}

		dateLocale = dateLocale.withMonthOfYear(monthToUse).withYear(yearToUse);

		LocalDate maxDayOfCurrentMonth = dateLocale.dayOfMonth().withMaximumValue();
		int firstDayOfCurrentMonth = dateLocale.withDayOfMonth(1).getDayOfWeek();
		int lastDayOfCurrentMonth = maxDayOfCurrentMonth.getDayOfMonth();

		//###########################################################
		//#Select nikoniko's mood per day with the chosen month/year#
		//###########################################################

		for (int i = 1; i <= lastDayOfCurrentMonth; i++) {
			days.add(new HashMap<String, Object>());

			days.get(i-1).put(jourSemaine[dateLocale.withDayOfMonth(i).getDayOfWeek()-1], i);

			//fonction a importer
			List<NikoNiko> nikostemp = getNikoPreciseDate((List<NikoNiko>)nikos,dateLocale.getYear(),dateLocale.getMonthOfYear(),i);

			int nikoMood = 0;

			for (NikoNiko nikotemp : nikostemp) {
				nikoMood = nikotemp.getMood();
			}

			days.get(i-1).put("nikoOfDay", nikoMood);

			if (dateLocale.withDayOfMonth(i).getDayOfWeek()==1) {//if Monday
				numberOfWeekInMonth++;
				nbWeeks.add(numberOfWeekInMonth);
				days.get(i-1).put("endOfWeek", numberOfWeekInMonth);
			} else {
				days.get(i-1).put("endOfWeek", numberOfWeekInMonth);
			}

			if (uncompleteWeek) {
				days.get(i-1).put("uncompleteWeek", 1);
				if (dateLocale.withDayOfMonth(i).getDayOfWeek()==7) {
					uncompleteWeek = false;
				}
			} else {
				days.get(i-1).put("uncompleteWeek", 0);
			}

			if (dateLocale.withDayOfMonth(i).getDayOfWeek()== 1
				&& i >= (lastDayOfCurrentMonth-5)) {
				uncompleteWeek = true;
			}
		}

		//#########################################################
		//#Give attributes to the view for the selected month/year#
		//#########################################################

		if (firstDayOfCurrentMonth!=1) {
			if (firstDayOfCurrentMonth!=7&&firstDayOfCurrentMonth!=6) {
				firstWeekUncomplete = 1;
				model.addAttribute("nbJoursSemaineAIgnorer",firstDayOfCurrentMonth-1);
			}
		}

		if (maxDayOfCurrentMonth.getDayOfWeek()!=7) {
			if (maxDayOfCurrentMonth.getDayOfWeek()!=6&&maxDayOfCurrentMonth.getDayOfWeek()!=5) {
				lastWeekUncomplete = 1;
				model.addAttribute("nbJoursSemaineAAjouter",5-maxDayOfCurrentMonth.getDayOfWeek());
			}
		}

		//ArrayList of maps
		model.addAttribute("days",days);
		//Lists
		model.addAttribute("numberOfWeekInMonth",numberOfWeekInMonth);
		model.addAttribute("jourSemaine",jourSemaine);
		//Checks/booleans
		model.addAttribute("firstWeekUncomplete",firstWeekUncomplete);
		model.addAttribute("lastWeekUncomplete",lastWeekUncomplete);
		//Others
		model.addAttribute("yearToUse",yearToUse);
		model.addAttribute("monthToUse",monthToUse);
		model.addAttribute("monthName",moisAnnee[monthToUse-1]);
		model.addAttribute("nbweeks",nbWeeks);
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);

		return "nikoniko/userCalendarView";
	}
	/**
	 * END OF ERWAN CHANGES
	 */

	/**
	 *
	 * @param model	:
	 * @param idTeam: Id of the team
	 * @param month	: Month number
	 * @param year	: Year number
	 * @param action: Used to select the month to show from the current one (previous or next)
	 * @return 		: Calendar view of all nikonikos of a team shown per day for a given month
	 * @throws IOException
	 */
	@RequestMapping(path = "nikonikoteam" + PathFinder.PATH + "{idTeam}"+ PathFinder.PATH + "month", method = RequestMethod.GET)
	public String nikoNikoCalendarTeam(Model model, @PathVariable Long idTeam,
			@RequestParam(defaultValue = "null") String month,
			@RequestParam(defaultValue = "null") String year,
			@RequestParam(defaultValue = "") String action,
			HttpServletResponse response) throws IOException {

		//TODO : if ROLE_USER/_CHEF_PROJET, check if visibility is "on" for this team
		//		 if privacy not "on", don't show this view
		try {
			teamCrud.findOne(idTeam).getName();
		} catch (Exception e) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),("This team doesn't exist!").toUpperCase());
			return "";
		}

		//##################################################################
		//Initialisation
		//##################################################################
		LocalDate dateLocale = LocalDate.now();

		String[] moisAnnee = {	"Janvier","Fevrier","Mars","Avril","Mai","Juin",
								"Juillet","Aout","Septembre","Octobre","Novembre","Décembre"};
		String[] jourSemaine = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"};

		int firstWeekUncomplete = 0;
		int lastWeekUncomplete = 0;
		int numberOfWeekInMonth = 1;
		int currentMonth = dateLocale.getMonthOfYear();
		int currentYear = dateLocale.getYear();
		int monthToUse = currentMonth;
		int yearToUse = currentYear;

		Boolean uncompleteWeek = true;
		Boolean monthIsAccepted = true;
		Boolean yearIsAccepted = true;

		List<Integer> nbWeeks = new ArrayList<Integer>();
		nbWeeks.add(numberOfWeekInMonth);

		ArrayList<Map<String,Object>> days = new ArrayList<Map<String,Object>>();

		ArrayList<NikoNiko> nikos = findNikoNikosOfATeam(idTeam);

		//###################################################################
		//#Check if given requestPAram values of month and year are integers#
		//###################################################################

		try {
			Integer.parseInt(month);
		} catch (Exception e) {
			monthIsAccepted = false;
		}

		try {
			Integer.parseInt(year);
		} catch (Exception e) {
			yearIsAccepted = false;
		}

		//##################################################################################
		//#Switch to the selected month and year (or default value if incorrect input data)#
		//##################################################################################

		if (action.equals("previous")) {
			if (monthIsAccepted) {
				monthToUse = Integer.parseInt(month) - 1;

				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}

				if (monthToUse == 0) {//January=>December
					monthToUse = 12;
					if (yearIsAccepted) {
						yearToUse = Integer.parseInt(year) - 1;
					} else {
						yearToUse = currentYear - 1;
					}
				}
			} else {
				monthToUse = currentMonth;
				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}
			}
		}else if (action.equals("next")) {
			if (monthIsAccepted) {
				monthToUse = Integer.parseInt(month) + 1;

				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}

				if (monthToUse == 13) {//December=>January
					monthToUse = 1;
					if (yearIsAccepted) {
						yearToUse = Integer.parseInt(year) + 1;
					} else {
						yearToUse = currentYear + 1;
					}
				}
			} else {
				//Prevoir un throw error 400
				monthToUse = currentMonth;
				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}
			}
		} else {
			monthToUse = currentMonth;
			yearToUse = currentYear;
		}

		dateLocale = dateLocale.withMonthOfYear(monthToUse).withYear(yearToUse);

		LocalDate maxDayOfCurrentMonth = dateLocale.dayOfMonth().withMaximumValue();
		int firstDayOfCurrentMonth = dateLocale.withDayOfMonth(1).getDayOfWeek();
		int lastDayOfCurrentMonth = maxDayOfCurrentMonth.getDayOfMonth();

		//###########################################################
		//#Select nikoniko's mood per day with the chosen month/year#
		//###########################################################

		for (int i = 1; i <= lastDayOfCurrentMonth; i++) {
			days.add(new HashMap<String, Object>());

			days.get(i-1).put(jourSemaine[dateLocale.withDayOfMonth(i).getDayOfWeek()-1], i);

			//fonction a importer
			List<NikoNiko> nikostemp = getNikoPreciseDate((List<NikoNiko>)nikos,dateLocale.getYear(),dateLocale.getMonthOfYear(),i);

			int countNikosBad = 0;
			int countNikosNeut = 0;
			int countNikosGood = 0;

			for (NikoNiko nikotemp : nikostemp) {
				if (nikotemp.getMood()==1) {
					countNikosBad = countNikosBad+1;
				}
				if (nikotemp.getMood()==2) {
					countNikosNeut = countNikosNeut+1;
				}
				if (nikotemp.getMood()==3) {
					countNikosGood = countNikosGood+1;
				}
			}

			//Put niko stats here
			days.get(i-1).put("nikoBad", countNikosBad);
			days.get(i-1).put("nikoNeutral", countNikosNeut);
			days.get(i-1).put("nikoGood", countNikosGood);

			if (dateLocale.withDayOfMonth(i).getDayOfWeek()==1) {//if Monday
				numberOfWeekInMonth++;
				nbWeeks.add(numberOfWeekInMonth);
				days.get(i-1).put("endOfWeek", numberOfWeekInMonth);
			} else {
				days.get(i-1).put("endOfWeek", numberOfWeekInMonth);
			}

			if (uncompleteWeek) {
				days.get(i-1).put("uncompleteWeek", 1);
				if (dateLocale.withDayOfMonth(i).getDayOfWeek()==7) {
					uncompleteWeek = false;
				}
			} else {
				days.get(i-1).put("uncompleteWeek", 0);
			}

			if (dateLocale.withDayOfMonth(i).getDayOfWeek()== 1
				&& i >= (lastDayOfCurrentMonth-5)) {
				uncompleteWeek = true;
			}
		}

		//#########################################################
		//#Give attributes to the view for the selected month/year#
		//#########################################################

		if (firstDayOfCurrentMonth!=1) {
			if (firstDayOfCurrentMonth!=7&&firstDayOfCurrentMonth!=6) {
				firstWeekUncomplete = 1;
				model.addAttribute("nbJoursSemaineAIgnorer",firstDayOfCurrentMonth-1);
			}
		}

		if (maxDayOfCurrentMonth.getDayOfWeek()!=7) {
			if (maxDayOfCurrentMonth.getDayOfWeek()!=6&&maxDayOfCurrentMonth.getDayOfWeek()!=5) {
				lastWeekUncomplete = 1;
				model.addAttribute("nbJoursSemaineAAjouter",5-maxDayOfCurrentMonth.getDayOfWeek());
			}
		}

		//ArrayList of maps
		model.addAttribute("days",days);
		//Lists
		model.addAttribute("numberOfWeekInMonth",numberOfWeekInMonth);
		model.addAttribute("jourSemaine",jourSemaine);
		//Checks/booleans
		model.addAttribute("firstWeekUncomplete",firstWeekUncomplete);
		model.addAttribute("lastWeekUncomplete",lastWeekUncomplete);
		//Others
		model.addAttribute("yearToUse",yearToUse);
		model.addAttribute("monthToUse",monthToUse);
		model.addAttribute("monthName",moisAnnee[monthToUse-1]);
		model.addAttribute("nbweeks",nbWeeks);
		model.addAttribute("teamName",teamCrud.findOne(idTeam).getName());
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);

		return "nikoniko/teamCalendarView";
	}

	/**
	 * SELECTION NIKONIKO PAR RAPPORT A UN ENSEMBLE (TEAM, VERTICALE, ETC...)
	 */

	public ArrayList<NikoNiko> findNikoNikosOfAVerticale(Long idVert){
		ArrayList<NikoNiko> vertNikonikos = new ArrayList<NikoNiko>();

		ArrayList<Team> vertTeams = new ArrayList<Team>();

		if (!verticaleCrud.findOne(idVert).getTeams().isEmpty()) {
			vertTeams.addAll(verticaleCrud.findOne(idVert).getTeams());

			for (Team team : vertTeams) {
				vertNikonikos.addAll(findNikoNikosOfATeam(team.getId()));
			}
		}
		return vertNikonikos;
	}

	public ArrayList<NikoNiko> findNikoNikosOfATeam(Long idTeam){

		ArrayList<User> usersOfTeam = findUsersOfATeam(idTeam);

		ArrayList<NikoNiko> nikonikos = new ArrayList<NikoNiko>();
		//Partie a externaliser en fonction findAllNikoNikoForAUser(idUser) => probablement deja existante
		if (!usersOfTeam.isEmpty()) {
			for (User user : usersOfTeam) {
				if (!user.getNikoNikos().isEmpty()) {
					nikonikos.addAll(user.getNikoNikos());
				}
			}
		}
		//fin de partie a externaliser

		return nikonikos;
	}

	public ArrayList<User> findUsersOfATeam(Long idValue) {

		List<Long> ids = new ArrayList<Long>();
		ArrayList<User> userList = new ArrayList<User>();
		List<BigInteger> idsBig = userTeamCrud.findAssociatedUser(idValue);

		if (!idsBig.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());
			}
			userList = (ArrayList<User>) userCrud.findAll(ids);
		}
		return userList;
	}

	/**
	 *
	 * @param model	:
	 * @param idTeam: Id of the verticale
	 * @param month	: Month number
	 * @param year	: Year number
	 * @param action: Used to select the month to show from the current one (previous or next)
	 * @return 		: Calendar view of all nikonikos of a team shown per day for a given month
	 */
	@RequestMapping(path = "nikonikovert" + PathFinder.PATH + "{idVert}"+ PathFinder.PATH + "month", method = RequestMethod.GET)
	public String nikoNikoCalendar(Model model, @PathVariable Long idVert,
			@RequestParam(defaultValue = "null") String month,
			@RequestParam(defaultValue = "null") String year,
			@RequestParam(defaultValue = "") String action,
			HttpServletResponse response) throws IOException {

		//TODO : Check if one or more team in this verticale have their visibility set to "off"
		//		 if privacy "off", don't show these team in the view (do this in get niko for team)
		try {
			verticaleCrud.findOne(idVert).getUsers();
		} catch (Exception e) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),("This verticale doesn't exist!").toUpperCase());
			return "";
		}
		//##################################################################
		//Initialisation
		//##################################################################
		LocalDate dateLocale = LocalDate.now();

		String[] moisAnnee = {	"Janvier","Fevrier","Mars","Avril","Mai","Juin",
								"Juillet","Aout","Septembre","Octobre","Novembre","Décembre"};
		String[] jourSemaine = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"};

		int firstWeekUncomplete = 0;
		int lastWeekUncomplete = 0;
		int numberOfWeekInMonth = 1;
		int currentMonth = dateLocale.getMonthOfYear();
		int currentYear = dateLocale.getYear();
		int monthToUse = currentMonth;
		int yearToUse = currentYear;

		Boolean uncompleteWeek = true;
		Boolean monthIsAccepted = true;
		Boolean yearIsAccepted = true;

		List<Integer> nbWeeks = new ArrayList<Integer>();
		nbWeeks.add(numberOfWeekInMonth);

		ArrayList<Map<String,Object>> days = new ArrayList<Map<String,Object>>();

		ArrayList<NikoNiko> nikos = findNikoNikosOfAVerticale(idVert);

		//###################################################################
		//#Check if given requestPAram values of month and year are integers#
		//###################################################################

		try {
			Integer.parseInt(month);
		} catch (Exception e) {
			monthIsAccepted = false;
		}

		try {
			Integer.parseInt(year);
		} catch (Exception e) {
			yearIsAccepted = false;
		}

		//##################################################################################
		//#Switch to the selected month and year (or default value if incorrect input data)#
		//##################################################################################

		if (action.equals("previous")) {
			if (monthIsAccepted) {
				monthToUse = Integer.parseInt(month) - 1;

				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}

				if (monthToUse == 0) {//January=>December
					monthToUse = 12;
					if (yearIsAccepted) {
						yearToUse = Integer.parseInt(year) - 1;
					} else {
						yearToUse = currentYear - 1;
					}
				}
			} else {
				monthToUse = currentMonth;
				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}
			}
		}else if (action.equals("next")) {
			if (monthIsAccepted) {
				monthToUse = Integer.parseInt(month) + 1;

				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}

				if (monthToUse == 13) {//December=>January
					monthToUse = 1;
					if (yearIsAccepted) {
						yearToUse = Integer.parseInt(year) + 1;
					} else {
						yearToUse = currentYear + 1;
					}
				}
			} else {
				//Prevoir un throw error 400
				monthToUse = currentMonth;
				if (yearIsAccepted) {
					yearToUse = Integer.parseInt(year);
				} else {
					yearToUse = currentYear;
				}
			}
		} else {
			monthToUse = currentMonth;
			yearToUse = currentYear;
		}

		dateLocale = dateLocale.withMonthOfYear(monthToUse).withYear(yearToUse);

		LocalDate maxDayOfCurrentMonth = dateLocale.dayOfMonth().withMaximumValue();
		int firstDayOfCurrentMonth = dateLocale.withDayOfMonth(1).getDayOfWeek();
		int lastDayOfCurrentMonth = maxDayOfCurrentMonth.getDayOfMonth();

		//###########################################################
		//#Select nikoniko's mood per day with the chosen month/year#
		//###########################################################

		for (int i = 1; i <= lastDayOfCurrentMonth; i++) {
			days.add(new HashMap<String, Object>());

			days.get(i-1).put(jourSemaine[dateLocale.withDayOfMonth(i).getDayOfWeek()-1], i);

			//fonction a importer
			List<NikoNiko> nikostemp = getNikoPreciseDate((List<NikoNiko>)nikos,dateLocale.getYear(),dateLocale.getMonthOfYear(),i);

			int countNikosBad = 0;
			int countNikosNeut = 0;
			int countNikosGood = 0;

			for (NikoNiko nikotemp : nikostemp) {
				if (nikotemp.getMood()==1) {
					countNikosBad = countNikosBad+1;
				}
				if (nikotemp.getMood()==2) {
					countNikosNeut = countNikosNeut+1;
				}
				if (nikotemp.getMood()==3) {
					countNikosGood = countNikosGood+1;
				}
			}

			//Put niko stats here
			days.get(i-1).put("nikoBad", countNikosBad);
			days.get(i-1).put("nikoNeutral", countNikosNeut);
			days.get(i-1).put("nikoGood", countNikosGood);

			if (dateLocale.withDayOfMonth(i).getDayOfWeek()==1) {//if Monday
				numberOfWeekInMonth++;
				nbWeeks.add(numberOfWeekInMonth);
				days.get(i-1).put("endOfWeek", numberOfWeekInMonth);
			} else {
				days.get(i-1).put("endOfWeek", numberOfWeekInMonth);
			}

			if (uncompleteWeek) {
				days.get(i-1).put("uncompleteWeek", 1);
				if (dateLocale.withDayOfMonth(i).getDayOfWeek()==7) {
					uncompleteWeek = false;
				}
			} else {
				days.get(i-1).put("uncompleteWeek", 0);
			}

			if (dateLocale.withDayOfMonth(i).getDayOfWeek()== 1
				&& i >= (lastDayOfCurrentMonth-5)) {
				uncompleteWeek = true;
			}
		}

		//#########################################################
		//#Give attributes to the view for the selected month/year#
		//#########################################################

		if (firstDayOfCurrentMonth!=1) {
			if (firstDayOfCurrentMonth!=7&&firstDayOfCurrentMonth!=6) {
				firstWeekUncomplete = 1;
				model.addAttribute("nbJoursSemaineAIgnorer",firstDayOfCurrentMonth-1);
			}
		}

		if (maxDayOfCurrentMonth.getDayOfWeek()!=7) {
			if (maxDayOfCurrentMonth.getDayOfWeek()!=6&&maxDayOfCurrentMonth.getDayOfWeek()!=5) {
				lastWeekUncomplete = 1;
				model.addAttribute("nbJoursSemaineAAjouter",5-maxDayOfCurrentMonth.getDayOfWeek());
			}
		}

		//ArrayList of maps
		model.addAttribute("days",days);
		//Lists
		model.addAttribute("numberOfWeekInMonth",numberOfWeekInMonth);
		model.addAttribute("jourSemaine",jourSemaine);
		//Checks/booleans
		model.addAttribute("firstWeekUncomplete",firstWeekUncomplete);
		model.addAttribute("lastWeekUncomplete",lastWeekUncomplete);
		//Others
		model.addAttribute("yearToUse",yearToUse);
		model.addAttribute("monthToUse",monthToUse);
		model.addAttribute("monthName",moisAnnee[monthToUse-1]);
		model.addAttribute("nbweeks",nbWeeks);
		model.addAttribute("verticaleName",verticaleCrud.findOne(idVert).getName());
		model.addAttribute("back", PathFinder.PATH + PathFinder.MENU_PATH);

		return "nikoniko/verticaleCalendarView";
	}

}
