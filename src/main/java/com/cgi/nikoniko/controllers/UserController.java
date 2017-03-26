package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.User;
import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.association.UserHasRole;
import com.cgi.nikoniko.models.association.UserHasTeam;
import com.cgi.nikoniko.models.association.base.AssociationItemId;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(UserController.BASE_URL)
public class UserController extends ViewBaseController<User> {

	public final static String DOT = ".";
	public final static String PATH = "/";
	public final static String BASE_USER = "user";
	public final static String BASE_URL = PATH + BASE_USER;

	public final static String SHOW_PATH = "show";
	public final static String MENU_PATH = "menu";

	public final static String SHOW_NIKONIKO = "showNikoNikos";
	public final static String SHOW_TEAM = "showTeam";
	public final static String SHOW_ROLE = "showRole";
	public final static String SHOW_LINK = "link";
	public final static String ADD_TEAM = "addTeams";
	public final static String ADD_ROLE = "addRoles";
	public final static String REDIRECT = "redirect:";

	@Autowired
	INikoNikoCrudRepository nikonikoCrud;

	@Autowired
	IUserHasTeamCrudRepository userTeamCrud;

	@Autowired
	IUserHasRoleCrudRepository userRoleCrud;

	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	ITeamCrudRepository teamCrud;

	@Autowired
	IRoleCrudRepository roleCrud;

	public UserController() {
		super(User.class,BASE_URL);
	}

	protected UserController(Class<User> clazz, String baseURL) {
		super(clazz, baseURL);


	}

	/**
	 *
	 * ASSOCIATION USER --> NIKONIKO
	 *
	 */

	/**
	 * SHOW USER ACTIONS FOR A SPECIFIC PROFILE
	 *
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_PATH, method = RequestMethod.GET)
	public String showItem(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		//BOUCLE VERIF SI UN ROLE SPECIFIQUE EST ASSOCIE A UN USER
		//SERVIRA A DETERMINER QUEL ENVIRONNEMENT ON AFFICHE
		//ATTENTION : LE ROLE EST DETERMINE PAR RAPPORT AU USER QU'ON AFFICHE
		//			  ET PAS LE DETENTEUR DE LA SESSION
		for (RoleCGI roleName : this.setRolesForUserGet(idUser)) {
			String varTest = roleName.getName();
			if (varTest.equals("ROLE_ADMIN")) {
				model.addAttribute("myRole", roleName.getName());
				model.addAttribute("page",  "USER : " + userBuffer.getRegistration_cgi());
				model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
				model.addAttribute("item",DumpFields.fielder(super.getItem(idUser)));
				model.addAttribute("show_nikonikos", DOT + PATH + SHOW_NIKONIKO);
				model.addAttribute("show_teams", DOT + PATH + SHOW_TEAM);
				model.addAttribute("show_roles", DOT + PATH + SHOW_ROLE);
				model.addAttribute("go_delete", DELETE_ACTION);
				model.addAttribute("go_update", UPDATE_ACTION);

				return BASE_USER + PATH + SHOW_PATH;//LATER THIS ROUTE WILL LEAD TO A SPECIFIC FTL FILE
			} else {
				model.addAttribute("myRole", null);
			}
		}

		model.addAttribute("page",  "USER : " + userBuffer.getRegistration_cgi());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(idUser)));
		model.addAttribute("show_nikonikos", DOT + PATH + SHOW_NIKONIKO);
//		model.addAttribute("show_nikonikos", DOT + PATH + SHOW_LINK);
		model.addAttribute("show_teams", DOT + PATH + SHOW_TEAM);
		model.addAttribute("show_roles", DOT + PATH + SHOW_ROLE);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);

		return BASE_USER + PATH + SHOW_PATH;//LATER THIS ROUTE WILL LEAD TO A DEFAULT VIEW FOR NON USERS
	}

	/**
	 * LINK USER -> NIKONIKO (SELECT ALL NIKONIKOS FOR A USER)
	 * @param model
	 * @param userId
	 * @return
	 */
	@Secured("ROLE_ADMIN")//IMPORTANT : indique quels seront les profils autorisés à utiliser la fonction
	@RequestMapping("{userId}/showNikoNikos")
	public String getNikoNikosForUser(Model model, @PathVariable Long userId) {
		User user = super.getItem(userId);
		Set<NikoNiko> niko =  user.getNikoNikos();
		List<NikoNiko> listOfNiko = new ArrayList<NikoNiko>(niko);
		model.addAttribute("page", user.getFirstname() + " nikonikos");
		model.addAttribute("sortedFields", NikoNiko.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfNiko));
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addNikoNiko");
		return "user/showNikoNikos";
	}

	/**
	 *
	 * Page de creation d'un nikoniko pour un user
	 */
	@RequestMapping(path = "{userId}/addNikoNiko", method = RequestMethod.GET)
	public String createItemGet(Model model, @PathVariable Long userId) {
		User user = super.getItem(userId);
		NikoNiko niko = new NikoNiko();

		model.addAttribute("page",user.getFirstname() + " " + CREATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",NikoNiko.FIELDS);
		model.addAttribute("item",DumpFields.createContentsEmpty(niko.getClass()));
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("create_item", CREATE_ACTION);
		return "nikoniko/addNikoNiko";
	}

	// TODO : ADD A NIKONIKO FOR A USER

	@RequestMapping(path = "{idUser}/add", method = RequestMethod.POST)
	public String createItemPost(Model model, @PathVariable Long idUser, Integer mood, String comment) {
		return this.addNikoNiko(idUser, mood, comment);
	}


	// TODO : FONCTION TO ADD A NIKONIKO (FOR POST ACTION)

	public String addNikoNiko(Long idUser, int mood, String comment){

		Date date = new Date();
		User user = new User();

		user = userCrud.findOne(idUser);

		NikoNiko niko = new NikoNiko(user,mood,date,comment);
		nikonikoCrud.save(niko);

		return REDIRECT + PATH + MENU_PATH;
	}

	//////////////////////////////////////////////////////////////////


	/**
	 *
	 * Creation d'un nikoniko
	 */
	@RequestMapping(path = "{userId}/create", method = RequestMethod.POST)
	public String createItemPost(Model model, NikoNiko niko, @PathVariable Long userId) {

		try {
			User user = super.getItem(userId);
			niko.setUser(user);
			nikonikoCrud.save(niko);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return "redirect:/user/" + userId + "/showNikoNikos";
//		return "redirect:/user/" + userId + "/link";
	}

	/**
	 *
	 * ASSOCIATION USER --> TEAM
	 *
	 */

	/**
	 * RELATION USER HAS TEAM
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "{idUser}" + PATH + SHOW_TEAM, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("page",userBuffer.getRegistration_cgi());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items",this.UserInTeam(idUser));
		model.addAttribute("show_teams", DOT + PATH + SHOW_TEAM);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addTeams");

		return BASE_USER + PATH + SHOW_TEAM;
	}

	/**
	 * SHOW POST THAT UPDATE USER RELATION WITH TEAM WHEN A USER QUIT A TEAM
	 */
	@RequestMapping(path = "{idUser}" + PATH + SHOW_TEAM, method = RequestMethod.POST)
	public String showItemPost(Model model,@PathVariable Long idUser, Long idTeam) {
		return quitTeam(idUser, idTeam);
	}

	/**
	 * ADD USER FOR CURRENT TEAM
	 * @param model
	 * @param idUser
	 * @return
	 */
	@RequestMapping(path = "{idUser}" + PATH + ADD_TEAM, method = RequestMethod.GET)
	public <T> String addUsersGet(Model model, @PathVariable Long idUser) {

		Object userBuffer = new Object();
		userBuffer = userCrud.findOne(idUser);
		model.addAttribute("items", DumpFields.listFielder((ArrayList<Team>) teamCrud.findAll()));
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("page", ((User) userBuffer).getRegistration_cgi());
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", DOT + PATH + SHOW_TEAM);
		model.addAttribute("add", ADD_TEAM);

		return BASE_USER + PATH + ADD_TEAM;
	}

	/**
	 * ADD USER FOR CURRENT TEAM
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@RequestMapping(path = "{idUser}" + PATH + ADD_TEAM, method = RequestMethod.POST)
	public <T> String addUsersPost(Model model, @PathVariable Long idUser, Long idTeam) {
		return setUsersForTeamPost(idTeam, idUser);
	}

	/**
	 *
	 * @param teamId
	 * @return userList (list of user associated to a team)
	 */
	public ArrayList<Team> setTeamsForUserGet(Long idValue) {

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
	 * CREATE A FUNCTION THAT SET NEW TEAM FOR A USER (JUST AFFECT A TEAM ALREADY CREATE)
	 * @param idTeam
	 * @param idUser
	 * @return
	 */
	public String setUsersForTeamPost(Long idTeam,Long idUser){

		String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_TEAM;

		Team team = new Team();
		team = teamCrud.findOne(idTeam);

		User user = new User();
		user = userCrud.findOne(idUser);

		UserHasTeam userHasTeamBuffer = new UserHasTeam(user, team, new Date());

		userTeamCrud.save(userHasTeamBuffer);

		return redirect;

	}

	/**
	 * UPDATE USER_HAS_TEAM (leaving_date) WHEN A USER QUIT A TEAM
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	public String quitTeam(Long idUser, Long idTeam){

		String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_TEAM;

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
	public ArrayList<Map<String, Object>> UserInTeam(Long idUser){

		ArrayList<Long> ids = new ArrayList<Long>();
		ArrayList<Team> teamList = new ArrayList<Team>();
		ArrayList<UserHasTeam> userHasTeamList = new ArrayList<UserHasTeam>();
		ArrayList<UserHasTeam> userHasTeamListClean = new ArrayList<UserHasTeam>();

		teamList = setTeamsForUserGet(idUser);

		for (int i = 0; i < teamList.size(); i++) {
			userHasTeamList.add(userTeamCrud.findAssociatedUserTeamALL(idUser, teamList.get(i).getId()));

			if(userHasTeamList.get(i).getLeavingDate() == null){

				userHasTeamListClean.add(userHasTeamList.get(i));
				ids.add(userHasTeamList.get(i).getIdRight());

				}
		}
		return DumpFields.listFielder((List<Team>) teamCrud.findAll(ids));
	}

	/**
	 *
	 * ASSOCIATION USER --> ROLE
	 *
	 */

	/**
	 *
	 * @param model
	 * @param idUser
	 * @return
	 */
	@RequestMapping(path = "{idUser}" + PATH + SHOW_ROLE, method = RequestMethod.GET)
	public String showItemGetRole(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("page",userBuffer.getRegistration_cgi());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(this.setRolesForUserGet(idUser)));
		//model.addAttribute("items",DumpFields.listFielder((List<RoleCGI>) roleCrud.findAll()));
		model.addAttribute("show_roles", DOT + PATH + SHOW_ROLE);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addRoles");

		return BASE_USER + PATH + SHOW_ROLE;
	}

	/***
	 *
	 * @param model
	 * @param idUser
	 * @param idRole
	 * @return
	 */
	@RequestMapping(path = "{idUser}" + PATH + SHOW_ROLE, method = RequestMethod.POST)
	public String showItemDeleteRole(Model model,@PathVariable Long idUser, Long idRole) {

		String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_ROLE;
		UserHasRole userHasRole = new UserHasRole(userCrud.findOne(idUser), roleCrud.findOne(idRole));
		userRoleCrud.delete(userHasRole);
		return redirect;
	}

	/**
	 *
	 * @param model
	 * @param idUser
	 * @param idRole
	 * @return
	 */
	@RequestMapping(path = "{idUser}" + PATH + ADD_ROLE, method = RequestMethod.POST)
	public String showItemPostRole(Model model,@PathVariable Long idUser, Long idRole) {

		String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_ROLE;
		UserHasRole userHasRole = new UserHasRole(userCrud.findOne(idUser), roleCrud.findOne(idRole));
		userRoleCrud.save(userHasRole);

		return redirect;
	}


	public ArrayList<RoleCGI> setRolesForUserGet(Long idUser) {

		List<Long> ids = new ArrayList<Long>();
		ArrayList<RoleCGI> roleList = new ArrayList<RoleCGI>();

		List<BigInteger> idsBig = userRoleCrud.findAssociatedRole(idUser);

		if (!idsBig.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());
			}
			roleList = (ArrayList<RoleCGI>) roleCrud.findAll(ids);
		}
		return roleList;
	}


	@RequestMapping(path = "{idUser}" + PATH + ADD_ROLE, method = RequestMethod.GET)
	public <T> String addUsersGetRole(Model model, @PathVariable Long idUser) {

		Object userBuffer = new Object();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("items", DumpFields.listFielder((ArrayList<RoleCGI>) roleCrud.findAll()));
		model.addAttribute("sortedFields",RoleCGI.FIELDS);
		model.addAttribute("page", ((User) userBuffer).getRegistration_cgi());
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", DOT + PATH + SHOW_ROLE);
		model.addAttribute("add", ADD_ROLE);

		return BASE_USER + PATH + ADD_ROLE;
	}


}
