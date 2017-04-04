package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.dao.IVerticaleCrudRepository;
import com.cgi.nikoniko.models.association.UserHasTeam;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;
import com.cgi.nikoniko.models.tables.Verticale;
import com.cgi.nikoniko.models.association.base.AssociationItemId;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(TeamController.BASE_URL)
public class TeamController extends ViewBaseController<Team> {

	public final static String DOT = ".";
	public final static String PATH = "/";
	public final static String BASE_TEAM = "team";
	public final static String BASE_URL = PATH + BASE_TEAM;

	public static final String SHOW_PATH = "show";
	public static final String MENU_PATH = "menu";

	public final static String SHOW_GRAPH = "showGraph";
	public final static String SHOW_USER = "showUser";
	public final static String SHOW_NIKO = "showNiko";
	public final static String SHOW_VERTICAL = "showVerticale";

	public final static String ADD_VERTICAL = "addVerticale";
	public final static String ADD_USER = "addUsers";

	public final static String VERTICALE = "verticale";

	public final static String REDIRECT = "redirect:";

	@Autowired
	IUserHasTeamCrudRepository userTeamCrud;

	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	ITeamCrudRepository teamCrud;

	@Autowired
	INikoNikoCrudRepository nikoCrud;

	@Autowired
	IVerticaleCrudRepository verticaleCrud;

	public TeamController() {
		super(Team.class, BASE_URL);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////

		/**
		 *
		 * ASSOCIATION TEAM --> USER
		 *
		 */

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * SHOW ALL USERS OF A TEAM WITH A GIVEN ID
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = ROUTE_SHOW, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long id) {

		Long idverticale = null;

		Team teamBuffer = new Team();
		teamBuffer = teamCrud.findOne(id);

		if (teamBuffer.getVerticale() == null) {
			idverticale = 1L;

			teamBuffer.setVerticale(verticaleCrud.findOne(idverticale));
			teamCrud.save(teamBuffer);
			}

		model.addAttribute("page","TEAM : " + teamBuffer.getName());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("show_users", DOT + PATH + SHOW_USER);
		model.addAttribute("show_verticale", DOT + PATH + SHOW_VERTICAL);
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);

		return BASE_TEAM + PATH + SHOW_PATH;
	}

	/**
	 * RELATIONS (TEAM HAS USER)
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = "{idTeam}" + PATH + SHOW_USER, method = RequestMethod.GET)
	public <T> String showLinksGet(Model model, @PathVariable Long idTeam) {

		Team teamBuffer = new Team();
		teamBuffer = teamCrud.findOne(idTeam);

		//model.addAttribute("items", DumpFields.listFielder(this.setUsersForTeamGet(id)));
		model.addAttribute("items",this.UserInTeam(idTeam));
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("page", ((Team) teamBuffer).getName());
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", "./show");
		model.addAttribute("add", "addUsers");

		return BASE_TEAM + PATH + SHOW_USER;
	}

	/**
	 *
	 * SHOW POST THAT UPDATE USER RELATION WITH TEAM WHEN A USER QUIT A TEAM
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = "{idTeam}" + PATH + SHOW_USER, method = RequestMethod.POST)
	public String showItemPost(Model model,@PathVariable Long idTeam, Long idUser) {
		return quitTeam(idUser, idTeam);
	}

	/**
	 *
	 * ADD USER FOR CURRENT TEAM
	 * @param model
	 * @param idTeam
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = "{idTeam}" + PATH + ADD_USER, method = RequestMethod.GET)
	public <T> String addUsersGet(Model model, @PathVariable Long idTeam) {

		Object teamBuffer = new Object();
		teamBuffer = teamCrud.findOne(idTeam);
		
		ArrayList<User> userList = new ArrayList<User>();

		model.addAttribute("items", DumpFields.listFielder(userList));
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("page", ((Team) teamBuffer).getName());
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", "./showUser");
		model.addAttribute("add", ADD_USER);

		return BASE_TEAM + PATH + ADD_USER;
	}

	/**
	 *
	 * ADD USER FOR CURRENT TEAM
	 * @param model
	 * @param idTeam
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = "{idTeam}" + PATH + ADD_USER, params = "idUser", method = RequestMethod.POST)
	public <T> String addUsersPost(Model model, @PathVariable Long idTeam, @RequestParam Long idUser) {
		return setUsersForTeamPost(idTeam, idUser);
	}

	/**
	 * SHOW USERS TO ADD ON VERTICALE (SEARCH)
	 * @param model
	 * @param name
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idTeam}" + PATH + ADD_USER, params = "name", method = RequestMethod.POST)
	public String addVerticalForTeamPOST(Model model,@RequestParam String name , @PathVariable Long idTeam){
		
		Team teamBuffer = teamCrud.findOne(idTeam);
		
		model.addAttribute("model", "team");
		model.addAttribute("page", teamBuffer.getName());
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("items",DumpFields.listFielder(this.searchUser(name)));
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", DOT + PATH + SHOW_USER);
		
		return BASE_TEAM + PATH + ADD_USER;

	}
	
	/**
	 * FIND A SPECIFIC USER
	 * @param name
	 * @return
	 */
	public ArrayList<User> searchUser(String name){

		ArrayList<User> userList = new ArrayList<User>();
		userList = userCrud.getUsers(name);

		return userList;

	}
	
	/**
	 *	RETURN LIST OF ALL USERS IN A TEAM WITH A GIVEN ID
	 * @param teamId
	 * @return userList (list of user associated to a team)
	 */
	public ArrayList<User> setUsersForTeamGet(Long idValue) {

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
	 * FUNCTION THAT SET NEW USER IN TEAM (JUST AFFECT A USER ALREADY CREATE)
	 * @param idTeam
	 * @param idUser
	 * @return
	 */
	public String setUsersForTeamPost(Long idTeam,Long idUser){

		String redirect = REDIRECT + PATH + BASE_TEAM + PATH + idTeam + PATH + SHOW_USER;

		Team team = new Team();
		team = teamCrud.findOne(idTeam);

		User user = new User();
		user = userCrud.findOne(idUser);

		UserHasTeam userHasTeamBuffer = new UserHasTeam(user, team, new Date());

		userTeamCrud.save(userHasTeamBuffer);

		return redirect;

	}

	/**
	 * UPDATE USER_HAS_TEAM (leaving_date) WHEN A TEAM DELETE AN USER FROM HIS OWN
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	public String quitTeam(Long idUser, Long idTeam){

		String redirect = REDIRECT + PATH + BASE_TEAM + PATH + idTeam + PATH + SHOW_USER;

		Date date = new Date();

		UserHasTeam userHasTeamBuffer = userTeamCrud.findOne(new AssociationItemId(idUser, idTeam));
		userHasTeamBuffer.setLeavingDate(date);

		userTeamCrud.save(userHasTeamBuffer);

		return redirect;
	}

	/**
	 * FUNCTION RETURNING ALL TEAM RELATED WITH ONE USER WITH leaving_date = null
	 * @param idUser
	 * @return
	 */
	public ArrayList<Map<String, Object>> UserInTeam(Long idTeam){

		ArrayList<Long> ids = new ArrayList<Long>();
		ArrayList<User> userList = new ArrayList<User>();
		ArrayList<UserHasTeam> userHasTeamList = new ArrayList<UserHasTeam>();
		ArrayList<UserHasTeam> userHasTeamListClean = new ArrayList<UserHasTeam>();

		userList = setUsersForTeamGet(idTeam);

		for (int i = 0; i < userList.size(); i++) {
			userHasTeamList.add(userTeamCrud.findAssociatedUserTeamALL(userList.get(i).getId(), idTeam));

			if(userHasTeamList.get(i).getLeavingDate() == null){

				userHasTeamListClean.add(userHasTeamList.get(i));
				ids.add(userHasTeamList.get(i).getIdLeft());

				}
		}

		return DumpFields.listFielder((List<User>) userCrud.findAll(ids));
	}


	// ///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * ASSOCIATION TEAM --> VERTICAL
	 *
	 */

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * RELATION USER HAS TEAM
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	// @Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = "{idTeam}" + PATH + SHOW_VERTICAL, method = RequestMethod.GET)
	public String showVerticalForUserGET(Model model, @PathVariable Long idTeam) {

		Team teamBuffer = new Team();
		teamBuffer = teamCrud.findOne(idTeam);

		model.addAttribute("page", teamBuffer.getName());
		model.addAttribute("sortedFields", Verticale.FIELDS);
		model.addAttribute("items", this.getVerticalForUser(idTeam));
		model.addAttribute("show_verticale", DOT + PATH + SHOW_VERTICAL);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addVerticale");

		return BASE_TEAM + PATH + SHOW_VERTICAL;

	}

	/**
	 * SHOW VERTICAL TO ON TEAM
	 * @param idTeam
	 * @return
	 */
	public ArrayList<Verticale> getVerticalForUser(Long idTeam) {
		ArrayList<Verticale> verticaleList = new ArrayList<Verticale>();
		Long idVerticale = teamCrud.getTeamVertical(idTeam);
		verticaleList.add(verticaleCrud.findOne(idVerticale));
		return verticaleList;
	}

	/**
	 * SHOW VERTICAL TO ADD TEAM
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_GESTIONNAIRE" })
	@RequestMapping(path = "{idTeam}" + PATH + ADD_VERTICAL, method = RequestMethod.GET)
	public <T> String addVerticalForUserGET(Model model,
			@PathVariable Long idTeam) {

		Object teamBuffer = new Object();
		teamBuffer = teamCrud.findOne(idTeam);
		model.addAttribute("items", DumpFields
				.listFielder((ArrayList<Verticale>) verticaleCrud.findAll()));
		model.addAttribute("sortedFields", Verticale.FIELDS);
		model.addAttribute("page", ((Team) teamBuffer).getName());
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", DOT + PATH + SHOW_VERTICAL);
		model.addAttribute("add", ADD_VERTICAL);

		return BASE_TEAM + PATH + ADD_VERTICAL;
	}

	/**
	 * ADD ONE VERTICALE TO TEAM
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_GESTIONNAIRE" })
	@RequestMapping(path = "{idTeam}" + PATH + ADD_VERTICAL, method = RequestMethod.POST)
	public <T> String addVerticalForUserPOST(Model model,
			@PathVariable Long idTeam, Long idVertical) {
		return setVerticalForTeam(idTeam, idVertical);
	}

	/**
	 * ADD ONE VERTICAL TO A TEAM
	 * @param idTeam
	 * @param idVertical
	 * @return
	 */
	private String setVerticalForTeam(Long idTeam, Long idVertical) {

		String redirect = REDIRECT + PATH + BASE_TEAM + PATH + idTeam + PATH
				+ SHOW_VERTICAL;

		Team teamBuffer = new Team();
		Verticale verticaleBuffer = new Verticale();

		teamBuffer = teamCrud.findOne(idTeam);
		verticaleBuffer = verticaleCrud.findOne(idVertical);

		teamBuffer.setVerticale(verticaleBuffer);
		teamCrud.save(teamBuffer);

		return redirect;
	}


	/**
	 * PARTIE DE ERWAN
	 */


	/**
	 * SELECTION NIKONIKO PAR RAPPORT A UN ENSEMBLE (TEAM, VERTICALE, ETC...)
	 */

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

	/**se trouve a l adresse team/idTeam/mesnikonikos
	 *
	 * @param model
	 * @param idTeam
	 * @return
	 */
	@RequestMapping(path = "{idTeam}/mesnikonikos", method = RequestMethod.GET)
	public String controlleurBidon(Model model, @PathVariable Long idTeam) {

		ArrayList<NikoNiko> nikos = findNikoNikosOfATeam(idTeam);

		//##################################################################
		//Creation calendrier
		//##################################################################

		LocalDate dateLocale = LocalDate.now();
		dateLocale = dateLocale.withMonthOfYear(4);//line to modify month to show previous nikos

		LocalDate maxDayOfCurrentMonth = dateLocale.dayOfMonth().withMaximumValue();
		int firstDayOfCurrentMonth = dateLocale.withDayOfMonth(1).getDayOfWeek();
		int lastDayOfCurrentMonth = maxDayOfCurrentMonth.getDayOfMonth();

		String[] jourSemaine = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"};
		List<Integer> nbWeeks = new ArrayList<Integer>();
		Boolean uncompleteWeek = true;
		int firstWeekUncomplete = 0;
		int lastWeekUncomplete = 0;
		int numberOfWeekInMonth = 1;
		nbWeeks.add(numberOfWeekInMonth);

		ArrayList<Map<String,Object>> days = new ArrayList<Map<String,Object>>();

		if (firstDayOfCurrentMonth!=1) {
			firstWeekUncomplete = 1;
			model.addAttribute("nbJoursSemaineAIgnorer",firstDayOfCurrentMonth-1);
		}

		if (maxDayOfCurrentMonth.getDayOfWeek()!=7) {
			lastWeekUncomplete = 1;
			model.addAttribute("nbJoursSemaineAAjouter",7-maxDayOfCurrentMonth.getDayOfWeek());
		}

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


		model.addAttribute("days",days);

		model.addAttribute("nbweeks",nbWeeks);
		model.addAttribute("numberOfWeekInMonth",numberOfWeekInMonth);
		model.addAttribute("jourSemaine",jourSemaine);

		model.addAttribute("firstWeekUncomplete",firstWeekUncomplete);
		model.addAttribute("lastWeekUncomplete",lastWeekUncomplete);


		//##################################################################
		//Fin creation calendrier
		//##################################################################


		model.addAttribute("nikos",DumpFields.listFielder(
				(ArrayList<NikoNiko>)nikoCrud.findAllByMood(2)));


//		model.addAttribute("teamName", teamCrud.findByName("Trololololo").getSerial());
//		model.addAttribute("teamName", teamCrud.findBySerial("264523kl").getName());
//		model.addAttribute("userregistrated",userCrud.findByRegistrationcgi("NOUVEAUUSER").getLogin());
//		model.addAttribute("users",DumpFields.listFielder((ArrayList<User>)userCrud.findAllByFirstname("Tony")));
//		model.addAttribute("verticales",DumpFields.listFielder((ArrayList<Verticale>)verticaleCrud.findAllByName("test")));

//		model.addAttribute("sortedFields",NikoNiko.FIELDS);
//		model.addAttribute("items",DumpFields.listFielder(nikos));

		return "nikoniko/testFindNikopage";
	}

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

	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = {PATH, ROUTE_LIST}, method = RequestMethod.POST)
	public String showUsers(Model model,String name){

		model.addAttribute("model", "team");
		model.addAttribute("page",this.baseName + " " + LIST_ACTION.toUpperCase());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items",DumpFields.listFielder(teamCrud.getTeams(name)));
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		
		return listView;
	}

	/**
	 * FIND A SPECIFIC USER
	 * @param name
	 * @return
	 */
	public ArrayList<Team> searchTeam(String name){

		ArrayList<Team> teamList = new ArrayList<Team>();
		teamList = teamCrud.getTeams(name);

		return teamList;

	}

}
