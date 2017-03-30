package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.cgi.nikoniko.models.association.UserHasTeam;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;
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
	public final static String VERTICALE = "verticale";
	public final static String ADD_USER = "addUsers";

	public final static String REDIRECT = "redirect:";

	@Autowired
	IUserHasTeamCrudRepository userTeamCrud;

	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	ITeamCrudRepository teamCrud;

	@Autowired
	INikoNikoCrudRepository nikoCrud;

	public TeamController() {
		super(Team.class, BASE_URL);
	}

	/**
	 *
	 * SHOW ALL USERS OF A TEAM WITH A GIVEN ID
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = ROUTE_SHOW, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long id) {

		Team teamBuffer = new Team();
		teamBuffer = teamCrud.findOne(id);

		model.addAttribute("page","TEAM : " + teamBuffer.getName());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("show_users", DOT + PATH + SHOW_USER);
		if (teamBuffer.getVerticale()!= null) {
			Long idverticale = teamBuffer.getVerticale().getId();
			model.addAttribute("show_verticale", PATH + VERTICALE + PATH + idverticale + PATH + SHOW_PATH);
		}
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

		model.addAttribute("items", DumpFields.listFielder((ArrayList<User>) userCrud.findAll()));
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
	@RequestMapping(path = "{idTeam}" + PATH + ADD_USER, method = RequestMethod.POST)
	public <T> String addUsersPost(Model model, @PathVariable Long idTeam, Long idUser) {
		return setUsersForTeamPost(idTeam, idUser);
	}

	/** DOES IT REALLY NEED A RESTRICTION??
	 * ADMIN,
	 * VP,
	 * GESTIONNAIRE
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

	/** DOES IT REALLY NEED A RESTRICTION??
	 *
	 *
	 * ADMIN,
	 * VP,
	 * GESTIONNAIRE
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
	 * ADMIN + Gestionnaire
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
	 * ADMIN
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

		List<Long> ids = new ArrayList<Long>();

		model.addAttribute("sortedFields",NikoNiko.FIELDS);
		model.addAttribute("items",DumpFields.listFielder(nikos));

		return "nikoniko/testFindNikopage";
	}



}
