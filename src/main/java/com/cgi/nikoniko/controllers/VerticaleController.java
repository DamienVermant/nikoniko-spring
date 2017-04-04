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
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;
import com.cgi.nikoniko.models.tables.Verticale;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(VerticaleController.BASE_URL)
public class VerticaleController  extends ViewBaseController<Verticale> {

	public final static String DOT = ".";
	public final static String PATH = "/";
	public final static String BASE_URL = "/verticale";
	public final static String MENU_PATH = "menu";

	public final static String BASE_VERTICALE = "verticale";
	public final static String SHOW_GRAPH = "showGraph";
	public final static String SHOW_USER = "showUser";
	public final static String SHOW_TEAM = "showTeam";
	public final static String SHOW_NIKO = "showNiko";

	public static final String SHOW_PATH = "show";

	public final static String ADD_USER = "addUsers";
	public final static String ADD_TEAM = "addTeams";

	public final static Long DEFAULT_ID_VERTICAL = (long) 1;

	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	ITeamCrudRepository teamCrud;

	@Autowired
	IVerticaleCrudRepository verticaleCrud;

	@Autowired
	IUserHasTeamCrudRepository userTeamCrud;

	@Autowired
	INikoNikoCrudRepository nikoCrud;

	public VerticaleController() {
		super(Verticale.class,BASE_URL);
	}

	/**
	 *
	 * SHOW ALL VERTICALE WITH A GIVEN ID
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = ROUTE_SHOW, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long id) {

		Verticale verticaleBuffer = new Verticale();
		verticaleBuffer = verticaleCrud.findOne(id);

		model.addAttribute("page","Verticale : " + verticaleBuffer.getName());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("show_users", DOT + PATH + SHOW_USER);
		model.addAttribute("show_team", DOT + PATH + SHOW_TEAM);
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);

	return BASE_VERTICALE + PATH + SHOW_PATH;
}


	/**
	 *
	 * @param model
	 * @param verticaleId
	 * @return
	 */

	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}"+ PATH + SHOW_USER, method = RequestMethod.GET)
	public String getUsersForVerticaleGET(Model model, @PathVariable Long verticaleId) {

		Verticale verticale = super.getItem(verticaleId);
		Set<User> user =  verticale.getUsers();
		List<User> listOfUser = new ArrayList<User>(user);

		model.addAttribute("idVerticale", verticaleId);
		model.addAttribute("page", verticale.getName());
		model.addAttribute("type","user");
		model.addAttribute("sortedFields", User.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfUser));
		model.addAttribute("add", DOT + PATH + ADD_USER);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		return BASE_VERTICALE + PATH + SHOW_USER;
	}

	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}"+ PATH + SHOW_USER, method = RequestMethod.POST)
	public String getUsersForVerticalePOST(Model model, @PathVariable Long verticaleId, Long idUser) {
		return deleteUserFromVertical(idUser, verticaleId);
	}

	/**
	 *
	 * @param model
	 * @param verticaleId
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}" + PATH + SHOW_TEAM, method= RequestMethod.GET)
	public String getTeamsForVerticale(Model model, @PathVariable Long verticaleId) {
		Verticale verticale = super.getItem(verticaleId);
		Set<Team> team =  verticale.getTeams();
		List<Team> listOfTeam = new ArrayList<Team>(team);

		model.addAttribute("idVerticale", verticaleId);
		model.addAttribute("page", verticale.getName());
		model.addAttribute("type","team");
		model.addAttribute("sortedFields", Team.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfTeam));
		model.addAttribute("add", DOT + PATH + ADD_TEAM);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		return BASE_VERTICALE + PATH + SHOW_TEAM;
	}

	/**
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@Override
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(path = ROUTE_DELETE, method = RequestMethod.POST)
	public String deleteItemPost(Model model,@PathVariable Long id) {
		Verticale verticale = super.getItem(id);

		//Set to null foreign key verticale_id in user table before delete the verticale object
		if (verticale.getUsers()!= null) {
			for (User user : verticale.getUsers()) {
				if (user.getVerticale().getId()==id) {
					user.setVerticale(null);
					userCrud.save(user);
				}
			}
		}
		//Set to null foreign key verticale_id in Team table before delete the verticale object
		if (verticale.getTeams()!= null) {
			for (Team team : verticale.getTeams()) {
				if (team.getVerticale().getId()==id) {
					team.setVerticale(null);
					teamCrud.save(team);
				}
			}
		}

		super.deleteItem(id);
		return deleteRedirect;
	}

	/**
	 * SHOW USERS TO ADD ON VERTICALE
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idVerticale}" + PATH + ADD_USER, method = RequestMethod.GET)
	public <T> String addVerticalForUserGET(Model model, @PathVariable Long idVerticale) {

	Object verticaleBuffer = new Object();
	verticaleBuffer = verticaleCrud.findOne(idVerticale);
	model.addAttribute("items", DumpFields.listFielder((ArrayList<User>) userCrud.findAll()));
	model.addAttribute("sortedFields",User.FIELDS);
	model.addAttribute("page", ((Verticale) verticaleBuffer).getName());
	model.addAttribute("go_show", SHOW_ACTION);
	model.addAttribute("go_create", CREATE_ACTION);
	model.addAttribute("go_delete", DELETE_ACTION);
	model.addAttribute("back", DOT + PATH + SHOW_USER);
	model.addAttribute("add", ADD_USER);

	return BASE_VERTICALE + PATH + ADD_USER;
	}

	/**
	 * ADD ONE USER TO VERTICALE
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idVertical}" + PATH + ADD_USER, method = RequestMethod.POST)
	public <T> String addVerticalForUserPOST(Model model, Long idUser, @PathVariable Long idVertical) {
		return setUserforVertical(idUser, idVertical);
	}

	private String setUserforVertical(Long idUser, Long idVertical) {

		String redirect = REDIRECT + PATH + BASE_VERTICALE + PATH + idVertical + PATH + SHOW_USER;

		User userBuffer = new User();
		Verticale verticaleBuffer = new Verticale();

		userBuffer = userCrud.findOne(idUser);
		verticaleBuffer = verticaleCrud.findOne(idVertical);

		userBuffer.setVerticale(verticaleBuffer);
		userCrud.save(userBuffer);


		return redirect;
	}

	public String deleteUserFromVertical(Long idUser, Long idVerticale){

		String redirect = REDIRECT + PATH + BASE_VERTICALE + PATH + idVerticale + PATH + SHOW_USER;

		User userBuffer = userCrud.findOne(idUser);
		Verticale verticaleBuffer = verticaleCrud.findOne(DEFAULT_ID_VERTICAL);

		userBuffer.setVerticale(verticaleBuffer);

		userCrud.save(userBuffer);

		return redirect;

	}

	/**
	 * SHOW USERS TO ADD ON VERTICALE
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idVerticale}" + PATH + ADD_TEAM, method = RequestMethod.GET)
	public <T> String addVerticalForTeamGET(Model model, @PathVariable Long idVerticale) {

	Object verticaleBuffer = new Object();
	verticaleBuffer = verticaleCrud.findOne(idVerticale);
	model.addAttribute("items", DumpFields.listFielder((ArrayList<Team>) teamCrud.findAll()));
	model.addAttribute("sortedFields",Team.FIELDS);
	model.addAttribute("page", ((Verticale) verticaleBuffer).getName());
	model.addAttribute("go_show", SHOW_ACTION);
	model.addAttribute("go_create", CREATE_ACTION);
	model.addAttribute("go_delete", DELETE_ACTION);
	model.addAttribute("back", DOT + PATH + SHOW_TEAM);
	model.addAttribute("add", ADD_TEAM);

	return BASE_VERTICALE + PATH + ADD_TEAM;
	}

	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}"+ PATH + SHOW_TEAM, method = RequestMethod.POST)
	public String getTeamsForVerticalePOST(Model model, @PathVariable Long verticaleId, Long idTeam) {
		return deleteTeamFromVertical(idTeam, verticaleId);
	}

	/**
	 * ADD ONE USER TO VERTICALE
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idVertical}" + PATH + ADD_TEAM ,method = RequestMethod.POST)
	public <T> String addVerticalForTeamPOST(Model model, Long idTeam, @PathVariable Long idVertical) {
		return setTeamforVertical(idTeam, idVertical);
	}

	private String setTeamforVertical(Long idTeam, Long idVertical) {

		String redirect = REDIRECT + PATH + BASE_VERTICALE + PATH + idVertical + PATH + SHOW_TEAM;

		Team teamBuffer = new Team();
		Verticale verticaleBuffer = new Verticale();

		teamBuffer = teamCrud.findOne(idTeam);
		verticaleBuffer = verticaleCrud.findOne(idVertical);

		teamBuffer.setVerticale(verticaleBuffer);
		teamCrud.save(teamBuffer);


		return redirect;
	}

	public String deleteTeamFromVertical(Long idTeam, Long idVerticale){

		String redirect = REDIRECT + PATH + BASE_VERTICALE + PATH + idVerticale + PATH + SHOW_TEAM;

		Team teamBuffer = teamCrud.findOne(idTeam);
		Verticale verticaleBuffer = verticaleCrud.findOne(DEFAULT_ID_VERTICAL);

		teamBuffer.setVerticale(verticaleBuffer);

		teamCrud.save(teamBuffer);

		return redirect;

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

//	/**se trouve a l adresse verticale/idTeam/mesnikonikos
//	 *
//	 * @param model
//	 * @param idTeam
//	 * @return
//	 */
//	@RequestMapping(path = "{idVert}/mesnikonikos", method = RequestMethod.GET)
//	public String controlleurBidon(Model model, @PathVariable Long idVert) {
//
//		ArrayList<NikoNiko> nikos = findNikoNikosOfAVerticale(idVert);
//
//		List<Long> ids = new ArrayList<Long>();
//
//		model.addAttribute("sortedFields",NikoNiko.FIELDS);
//		model.addAttribute("items",DumpFields.listFielder(nikos));
//
//		return "nikoniko/testFindNikopage";
//	}

	/**
	 *
	 * @param model	:
	 * @param idTeam: Id of the verticale
	 * @param month	: Month number
	 * @param year	: Year number
	 * @param action: Used to select the month to show from the current one (previous or next)
	 * @return 		: Calendar view of all nikonikos of a team shown per day for a given month
	 */
	@RequestMapping(path = "nikonikovert/{idVert}/month", method = RequestMethod.GET)
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
			firstWeekUncomplete = 1;
			model.addAttribute("nbJoursSemaineAIgnorer",firstDayOfCurrentMonth-1);
		}

		if (maxDayOfCurrentMonth.getDayOfWeek()!=7) {
			lastWeekUncomplete = 1;
			model.addAttribute("nbJoursSemaineAAjouter",7-maxDayOfCurrentMonth.getDayOfWeek());
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

		return "nikoniko/verticaleCalendarView";
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

}
