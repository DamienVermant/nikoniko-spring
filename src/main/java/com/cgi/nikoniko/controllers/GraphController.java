package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.dao.IVerticaleCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;

@Controller
@RequestMapping(GraphController.BASE_URL)
public class GraphController extends ViewBaseController<User>{

	public final static String MENU_PATH = "menu";

	public final static String SHOW_GRAPH = "showGraph";
	public final static String SHOW_GRAPH_MONTH = "showGraphMonth";
	public final static String SHOW_GRAPH_WEEK = "showGraphWeek";
	public final static String SHOW_GRAPH_DATE = "showDate";
	public final static String SHOW_GRAPH_ALL = "showGraphAll";
	public final static String SHOW_GRAPH_VERTICALE = "showGraphVerticale";
	public final static String SHOW_GRAPH_TEAM = "showGraphTeam";

	public final static String BASE_GRAPH = "graph";
	public final static String BASE_URL = PATH + BASE_GRAPH;


	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	INikoNikoCrudRepository nikonikoCrud;

	@Autowired
	IVerticaleCrudRepository verticaleCrud;

	@Autowired
	ITeamCrudRepository teamCrud;

	@Autowired
	IUserHasTeamCrudRepository userTeamCrud;

	public GraphController(Class<User> clazz, String baseURL) {
		super(clazz, baseURL);
		// TODO Auto-generated constructor stub
	}

	public GraphController() {
		super(User.class,BASE_URL);
	}


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

/////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
*
* GRAPH GESTION
*
*
*/

/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * ALL NIKONIKO GRAPH FOR AN USER
	 * @param model
	 * @param idUser
	 * @return
	 */

	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_GRAPH, method = RequestMethod.GET)
	public String showPie(Model model, @PathVariable Long idUser) {

		User user = super.getItem(idUser);
		Set<NikoNiko> niko =  user.getNikoNikos();
		List<NikoNiko> listOfNiko = new ArrayList<NikoNiko>(niko);
		List<NikoNiko> nikotoday = getNikoToday(listOfNiko);

		int good = 0;
		int medium = 0;
		int bad = 0;

		for (int i = 0; i < nikotoday.size(); i++) {
			if (nikotoday.get(i).getMood() == 3) {
				good++;
			}else if(nikotoday.get(i).getMood() == 2){
				medium++;
			}else{
				bad++;
			}
		}

		model.addAttribute("title", "Mes votes !" );
		model.addAttribute("mood", this.getUserLastMood(userCrud.findByLogin(super.checkSession().getName()).getId()));
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("back", PATH + MENU_PATH);
		return "graphs" + PATH + "pie";
	}

	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_GRAPH_MONTH, method = RequestMethod.GET)
	public String showPieMonth(Model model, @PathVariable Long idUser) {

		User user = super.getItem(idUser);
		Set<NikoNiko> niko =  user.getNikoNikos();
		List<NikoNiko> listOfNiko = new ArrayList<NikoNiko>(niko);
		List<NikoNiko> nikomonth = getNikoMonth(listOfNiko);

		int good = 0;
		int medium = 0;
		int bad = 0;

		for (int i = 0; i < nikomonth.size(); i++) {
			if (nikomonth.get(i).getMood() == 3) {
				good++;
			}else if(nikomonth.get(i).getMood() == 2){
				medium++;
			}else{
				bad++;
			}
		}

		model.addAttribute("title", "Mes votes !" );
		model.addAttribute("mood", this.getUserLastMood(userCrud.findByLogin(super.checkSession().getName()).getId()));
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("back", PATH + MENU_PATH);
		return "graphs" + PATH + "pie";
	}

	/**
	 * ALL NIKONIKOS GRAPH
	 * @param model
	 * @param idUser
	 * @return
	 */

	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_GRAPH_ALL, method = RequestMethod.GET)
	public String showAllPie(Model model, @PathVariable Long idUser) {

		List<NikoNiko> listOfNiko = (List<NikoNiko>) nikonikoCrud.findAll();

		int good = 0;
		int medium = 0;
		int bad = 0;

		for (int i = 0; i < listOfNiko.size(); i++) {
			if (listOfNiko.get(i).getMood() == 3) {
				good++;
			}else if(listOfNiko.get(i).getMood() == 2){
				medium++;
			}else{
				bad++;
			}
		}

		model.addAttribute("title", "Tous les votes");
		model.addAttribute("mood", this.getUserLastMood(userCrud.findByLogin(super.checkSession().getName()).getId()));
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("back", PATH + MENU_PATH);
		return "graphs" + PATH + "pie";
	}

	/**
	 * NikoNiko associated from Verticale
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = "{userId}" + PATH + SHOW_GRAPH_VERTICALE, method = RequestMethod.GET)
	public String getNikoFromVerticale(Model model, @PathVariable Long userId){
		User user = super.getItem(userId);
		Long verticaleId = user.getVerticale().getId();
		List<BigInteger> listId = verticaleCrud.getNikoNikoFromVerticale(verticaleId);
		List<Long> listNikoId = new ArrayList<Long>();
		List<NikoNiko> listNiko = new ArrayList<NikoNiko>();
		int nbMood = 0;

		if (!listId.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			nbMood =1;
			for (BigInteger id : listId) {
				listNikoId.add(id.longValue());
			}
			listNiko =  (List<NikoNiko>) nikonikoCrud.findAll(listNikoId);
		}


		int good = 0;
		int medium = 0;
		int bad = 0;

		for (int i = 0; i < listNiko.size(); i++) {
			if (listNiko.get(i).getMood() == 3) {
				good++;
			}else if(listNiko.get(i).getMood() == 2){
				medium++;
			}else{
				bad++;
			}
		}

		model.addAttribute("title", verticaleCrud.findOne(verticaleId).getName());
		model.addAttribute("mood", nbMood);
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("back", PATH + MENU_PATH);
		return "graphs" + PATH + "pie";
	}

	@RequestMapping(path = "{userId}" + PATH + SHOW_GRAPH_TEAM + PATH + "{nbTable}", method = RequestMethod.GET)
	public String getNikoFromTeam(Model model, @PathVariable Long userId, @PathVariable int nbTable){

		String LAST_WORD = null;

		ArrayList<Team> teamList = new ArrayList<Team>();
		ArrayList<String> teamName = new ArrayList<String>();

		teamList = findAllTeamsForUser(userId);

		for (int i = 0; i < teamList.size(); i++) {
			teamName.add(teamList.get(i).getName());
		}

		Long teamId = teamList.get(nbTable).getId();

		List<BigInteger> listId = teamCrud.getNikoNikoFromTeam(teamId);
		List<Long> listNikoId = new ArrayList<Long>();
		List<NikoNiko> listNiko = new ArrayList<NikoNiko>();
		int nbMood = 0;

		if (!listId.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			nbMood = 1;
			for (BigInteger id : listId) {
				listNikoId.add(id.longValue());
			}
			listNiko =  (List<NikoNiko>) nikonikoCrud.findAll(listNikoId);
		}

		int good = 0;
		int medium = 0;
		int bad = 0;

		for (int i = 0; i < listNiko.size(); i++) {
			if (listNiko.get(i).getMood() == 3) {
				good++;
			}else if(listNiko.get(i).getMood() == 2){
				medium++;
			}else{
				bad++;
			}
		}

		model.addAttribute("title", teamCrud.findOne(teamId).getName());
		model.addAttribute("nameteam", teamName);
		model.addAttribute("mood", nbMood);
		model.addAttribute("good", good);
		model.addAttribute("medium", medium);
		model.addAttribute("bad", bad);
		model.addAttribute("back", PATH + MENU_PATH);
		LAST_WORD = "pieTeam";

		return "graphs" + PATH + LAST_WORD;
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
	 * Get nikoniko's list from this week
	 * @param listOfNiko
	 * @return
	 */
	public List<NikoNiko> getNikoWeek(List<NikoNiko> listOfNiko){

		LocalDate nikodate = new LocalDate();
		LocalDate date= new LocalDate();
		LocalDate interval1 = date.withDayOfWeek(1);
		LocalDate interval2 = date.withDayOfWeek(7);

		List<NikoNiko> nikoWeek = new ArrayList<NikoNiko>();

		for (int i = 0; i < listOfNiko.size(); i++) {
			Date firstniko = listOfNiko.get(i).getEntryDate();
			nikodate = new LocalDate(firstniko);

			if (nikodate.isAfter(interval1) && nikodate.isBefore(interval2)
					|| nikodate.isEqual(interval1)
					|| nikodate.isEqual(interval2)) {
				nikoWeek.add(listOfNiko.get(i));
			}
		}

		return nikoWeek;
	}

	/**
	 * Get nikoniko's list from this month
	 * @param listOfNiko
	 * @return
	 */
	public List<NikoNiko> getNikoMonth(List<NikoNiko> listOfNiko){

		int[] monthDays= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

		LocalDate nikodate = new LocalDate();
		LocalDate date= new LocalDate();

		int year = date.getYear();
		int j;
		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
			monthDays[1] = 29;
		};
		LocalDate interval1 = date.withDayOfMonth(1);
		LocalDate interval2 = date.withDayOfMonth(monthDays[nikodate.getMonthOfYear()-1]);

		List<NikoNiko> nikoMonth = new ArrayList<NikoNiko>();

		for (int i = 0; i < listOfNiko.size(); i++) {
			Date firstniko = listOfNiko.get(i).getEntryDate();
			nikodate = new LocalDate(firstniko);

			if (nikodate.isAfter(interval1) && nikodate.isBefore(interval2)
					|| nikodate.isEqual(interval1)
					|| nikodate.isEqual(interval2)) {
				nikoMonth.add(listOfNiko.get(i));
			}
		}

		return nikoMonth;
	}

	/**
	 * Get nikoniko's list from one week of the year
	 * @param listOfNiko
	 * @param year
	 * @param week
	 * @return
	 */
	public List<NikoNiko> getNikoWeekChoose(List<NikoNiko> listOfNiko,int year, int week){

		LocalDate nikodate = new LocalDate();
		LocalDate date= new LocalDate().withYear(year).withWeekOfWeekyear(week);
		LocalDate interval1 = date.withDayOfWeek(1);
		LocalDate interval2 = date.withDayOfWeek(7);


	List<NikoNiko> nikoWeek = new ArrayList<NikoNiko>();

	for (int i = 0; i < listOfNiko.size(); i++) {
		Date firstniko = listOfNiko.get(i).getEntryDate();
		nikodate = new LocalDate(firstniko);

		if (nikodate.isAfter(interval1) && nikodate.isBefore(interval2)
				|| nikodate.isEqual(interval1)
				|| nikodate.isEqual(interval2)) {
			nikoWeek.add(listOfNiko.get(i));
		}
	}

	return nikoWeek;
	}

	/**
	* Get nikoniko's list from one month of the year
	* @param listOfNiko
	* @param yearc
	* @param month
	* @return
	*/
	public List<NikoNiko> getNikoMonthChoose(List<NikoNiko> listOfNiko,int yearc, int month){

	int[] monthDays= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	LocalDate nikodate = new LocalDate();
	LocalDate date= new LocalDate().withYear(yearc).withMonthOfYear(month);

	int year = date.getYear();
	int j;
	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
		monthDays[1] = 29;
	};
	LocalDate interval1 = date.withDayOfMonth(1);
	LocalDate interval2 = date.withDayOfMonth(monthDays[nikodate.getMonthOfYear()-1]);


	List<NikoNiko> nikoMonth = new ArrayList<NikoNiko>();

	for (int i = 0; i < listOfNiko.size(); i++) {
		Date firstniko = listOfNiko.get(i).getEntryDate();
		nikodate = new LocalDate(firstniko);

	if (nikodate.isAfter(interval1) && nikodate.isBefore(interval2)
			|| nikodate.isEqual(interval1)
			|| nikodate.isEqual(interval2)) {
		nikoMonth.add(listOfNiko.get(i));
		}
	}

	return nikoMonth;
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

}
