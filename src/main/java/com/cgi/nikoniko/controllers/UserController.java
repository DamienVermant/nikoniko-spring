package com.cgi.nikoniko.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.cgi.nikoniko.models.tables.Verticale;
import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.dao.IVerticaleCrudRepository;
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
	public final static String VERTICALE = "verticale";
	public final static String BASE_URL = PATH + BASE_USER;

	public final static String SHOW_PATH = "show";
	public final static String MENU_PATH = "menu";

	public final static String SHOW_NIKONIKO = "showNikoNikos";
	public final static String SHOW_GRAPH = "showGraph";
	public final static String SHOW_GRAPH_ALL = "showGraphAll";
	public final static String SHOW_GRAPH_VERTICALE = "showGraphVerticale";
	public final static String SHOW_GRAPH_TEAM = "showGraphTeam";
	public final static String SHOW_TEAM = "showTeam";
	public final static String SHOW_ROLE = "showRole";
	public final static String SHOW_LINK = "link";
	public final static String SHOW_VERTICAL = "showVerticale";
	
	public final static String ADD_TEAM = "addTeams";
	public final static String ADD_ROLE = "addRoles";
	public final static String ADD_VERTICAL = "addVerticale";
	
	public final static String REDIRECT = "redirect:";

	public final static int TIME = 1;

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
	IVerticaleCrudRepository verticaleCrud;

	@Autowired
	INikoNikoCrudRepository nikoCrud;

	@Autowired
	IRoleCrudRepository roleCrud;

	public UserController() {
		super(User.class,BASE_URL);
	}

	protected UserController(Class<User> clazz, String baseURL) {
		super(clazz, baseURL);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * ASSOCIATION USER --> NIKONIKO
	 *
	 */
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**NAME : showUserActionsGET
	 *
	 * RETIRER VP DANS LES DROITS D'ACCES???
	 *
	 * SHOW USER ACTIONS FOR A SPECIFIC PROFILE
	 *
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Override
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_PATH, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long idUser) {

		Long idverticale = null;

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		// TODO : WHEN CREATE A USER ASIGN A VERTICAL
		
		// ADD A DEFAUT VERTICALE
		if (userBuffer.getVerticale() == null) {
			idverticale = 1L;
			
			userBuffer.setVerticale(verticaleCrud.findOne(3L));
		}
		else {
			 idverticale = userBuffer.getVerticale().getId();
		}

		model.addAttribute("page",  "USER : " + userBuffer.getRegistration_cgi());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(idUser)));
		model.addAttribute("show_nikonikos", DOT + PATH + SHOW_NIKONIKO);
		model.addAttribute("show_graphique", DOT + PATH + SHOW_GRAPH);
		model.addAttribute("show_verticale", PATH + VERTICALE + PATH + idverticale + PATH + SHOW_PATH);
		model.addAttribute("show_teams", DOT + PATH + SHOW_TEAM);
		model.addAttribute("show_roles", DOT + PATH + SHOW_ROLE);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);


		return BASE_USER + PATH + SHOW_PATH;
	}

	/**NAME : getNikoNikosForUser
	 *
	 * LINK USER -> NIKONIKO (SELECT ALL NIKONIKOS FOR A USER)
	 * @param model
	 * @param userId
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP","ROLE_USER"})
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

	/**NAME : newNikoNikoForUserGET
	 *
	 * Page de creation d'un nikoniko pour un user
	 * Only show the nikoniko's page of the user of the current session
	 * If someone try to hack url, he's redirected to an error page (or logout for now)
	 *
	 * @param model
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{userId}/add", method = RequestMethod.GET)
	public String newNikoNikoForUserGET(Model model,@PathVariable Long userId,
						HttpServletResponse response) throws IOException {

		User user = super.getItem(userId);
		NikoNiko niko = new NikoNiko();

		if (userCrud.findByLogin(super.checkSession().getName()).getId()!= userId) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),("Don't try to hack url!").toUpperCase());
		}

		model.addAttribute("mood" , this.getUserLastMood(userCrud.findByLogin(super.checkSession().getName()).getId()));
		model.addAttribute("page",user.getFirstname() + " " + CREATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",NikoNiko.FIELDS);
		model.addAttribute("item",DumpFields.createContentsEmpty(niko.getClass()));
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("create_item", CREATE_ACTION);
		return "nikoniko/addNikoNiko";
	}

	/**
	 *
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

	/**NAME : newNikoNikoForUserPOST
	 *
	 *
	 * @param model
	 * @param idUser
	 * @param mood
	 * @param comment
	 * @return
	 */

	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{idUser}/add", method = RequestMethod.POST)
	public String newNikoNikoForUserPOST(Model model, @PathVariable Long idUser, Integer mood, String comment) {
		return this.addNikoNikoInDB(idUser, mood, comment);
	}

	/**
	 * CHECK FOR NEW NIKONIKO OR UPDATE
	 */

	// TODO : CREATE A FUNCTION THAT CAN SET A NIKO AFTER J+1 IF USER DOES NOT SET HIS NIKONIKO
	// TODO : IF A USER FORGET TO POST HIS SATISFACTION, RECALL HIM AFTER (ONE DAY ?) TO VOTE FOR HIS PREVIOUS VOTE

	public Boolean checkDateNikoNiko(Long idUser){

		Boolean updateNiko = null;
		Date todayDate = new Date();

		Long idMaxNiko = userCrud.getLastNikoNikoUser(idUser);

		if (idMaxNiko == null) {
			updateNiko = false;
		}

		else {

			NikoNiko lastNiko = nikonikoCrud.findOne(idMaxNiko);
			Date entryDate = lastNiko.getEntry_date();

			java.util.Date eDate = new java.util.Date(entryDate.getTime());

			DateTime eDateClean = new DateTime(eDate,DateTimeZone.forID( "Europe/Paris" ));
			DateTime todayDateClean = new DateTime(todayDate,DateTimeZone.forID( "Europe/Paris" ));

			Days diff = Days.daysBetween(eDateClean, todayDateClean);

			if (diff.getDays() <= TIME) {

					updateNiko = true;
				}

				else {

					updateNiko = false;
				}
			}

		return updateNiko;
	}

	/**NAME : addNikoNikoInDB
	*
	* FUNCTION THAT SAVE THE NIKONIKO IN DB
	*
	* @param idUser, mood, comment
	* @return
	*/
	public String addNikoNikoInDB(Long idUser, Integer mood, String comment){

		Date date = new Date();
		User user = new User();

		user = userCrud.findOne(idUser);

		if (this.checkDateNikoNiko(idUser) == true) {

			if (mood ==  null) {

				return REDIRECT + PATH + MENU_PATH;
			}

			else {

				Long idMax = userCrud.getLastNikoNikoUser(idUser);
				NikoNiko nikoUpdate = nikonikoCrud.findOne(idMax);

				nikoUpdate.setChange_date(date);
				nikoUpdate.setComment(comment);
				nikoUpdate.setMood(mood);

				nikonikoCrud.save(nikoUpdate);

				return REDIRECT + PATH + MENU_PATH;
			}
		}

		else {

			if (mood ==  null) {

				return REDIRECT + PATH + MENU_PATH;
			}

			else {

				NikoNiko niko = new NikoNiko(user,mood,date,comment);
				nikonikoCrud.save(niko);
				return REDIRECT + PATH + MENU_PATH;
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * ASSOCIATION USER --> TEAM
	 *
	 */
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**NAME : showTeamsForUserGET
	 *
	 * RELATION USER HAS TEAM
	 *
	 * @param model
	 * @param id
	 * @return
	 */

	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_TEAM, method = RequestMethod.GET)
	public String showTeamsForUserGET(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("page",userBuffer.getRegistration_cgi());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items",this.getTeamsForUser(idUser));
		model.addAttribute("show_teams", DOT + PATH + SHOW_TEAM);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addTeams");

		return BASE_USER + PATH + SHOW_TEAM;
	}

	/**NAME : quiTeamPOST
	 *
	 * Delete the selected relation team-user and redirect to the userhasteams view (by using quitTeam())
	 * SHOW POST THAT UPDATE USER RELATION WITH TEAM WHEN A USER QUIT A TEAM
	 *
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */

	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_TEAM, method = RequestMethod.POST)
	public String quiTeamPOST(Model model,@PathVariable Long idUser, Long idTeam) {
		return quitTeam(idUser, idTeam);
	}

	/**NAME : addUserInTeamGET
	 *
	 * ADD USER FOR CURRENT TEAM
	 *
	 * @param model
	 * @param idUser
	 * @return
	 */

	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idUser}" + PATH + ADD_TEAM, method = RequestMethod.GET)
	public <T> String addUserInTeamGET(Model model, @PathVariable Long idUser) {

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

	/**NAME : addUserInTeamPOST
	 *
	 * ADD USER FOR CURRENT TEAM
	 *
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */

	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idUser}" + PATH + ADD_TEAM, method = RequestMethod.POST)
	public <T> String addUserInTeamPOST(Model model, @PathVariable Long idUser, Long idTeam) {
		return setUsersForTeam(idTeam, idUser);
	}

	/**NAME : findAllTeamsForUser
	 *
	 * Find all teams related to a user by checking relation table user_has_team
	 *
	 * @param idValue
	 * @return teamList (list of user associated to a team)
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

	/**NAME : setUsersForTeam
	 *
	 * Put an user in a new team by creating a new  association in user_has_team (or modify if exists)
	 *
	 * @param idTeam
	 * @param idUser
	 * @return
	 */
	public String setUsersForTeam(Long idTeam,Long idUser){

		userTeamCrud.save(new UserHasTeam(userCrud.findOne(idUser), teamCrud.findOne(idTeam), new Date()));

		String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_TEAM;

		return redirect;
	}

	/**NAME : quitTeam
	 *
	 * Set the leaving date in user_has_team table when a user leave a team
	 *
	 * @param idUser
	 * @param idTeam
	 * @return redirect (path redirection after action)
	 */
	public String quitTeam(Long idUser, Long idTeam){

		Date date = new Date();

		UserHasTeam userHasTeamBuffer = userTeamCrud.findOne(new AssociationItemId(idUser, idTeam));
		userHasTeamBuffer.setLeavingDate(date);

		userTeamCrud.save(userHasTeamBuffer);

		String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_TEAM;

		return redirect;
	}

	/** NAME : getTeamsForUser
	 *
	 * FUNCTION RETURNING ALL TEAM RELATED WITH ONE USER WITH leaving_date = null
	 *
	 * @param idUser
	 * @return
	 */
	public ArrayList<Map<String, Object>> getTeamsForUser(Long idUser){

		ArrayList<Long> ids = new ArrayList<Long>();
		ArrayList<Team> teamList = new ArrayList<Team>();
		ArrayList<UserHasTeam> userHasTeamList = new ArrayList<UserHasTeam>();
		ArrayList<UserHasTeam> userHasTeamListClean = new ArrayList<UserHasTeam>();

		teamList = findAllTeamsForUser(idUser);

		for (int i = 0; i < teamList.size(); i++) {
			userHasTeamList.add(userTeamCrud.findAssociatedUserTeamALL(idUser, teamList.get(i).getId()));

			if(userHasTeamList.get(i).getLeavingDate() == null){

				userHasTeamListClean.add(userHasTeamList.get(i));
				ids.add(userHasTeamList.get(i).getIdRight());

				}
		}
		return DumpFields.listFielder((List<Team>) teamCrud.findAll(ids));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * ASSOCIATION USER --> ROLE
	 *
	 */
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**NAME : showRolesForUserGET
	 *
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_ROLE, method = RequestMethod.GET)
	public String showRolesForUserGET(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("page",userBuffer.getRegistration_cgi());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(this.getAllRolesForUser(idUser)));
		model.addAttribute("show_roles", DOT + PATH + SHOW_ROLE);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addRoles");

		return BASE_USER + PATH + SHOW_ROLE;
	}

	/**NAME : revokeRoleToUserPOST // revokeRoleToOneUserPOST
	 *
	 *Revoke the selected role for the selected user
	 *
	 * @param model
	 * @param idUser
	 * @param idRole
	 * @return
	 */

	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_ROLE, method = RequestMethod.POST)
	public String revokeRoleToUserPOST(Model model,@PathVariable Long idUser, Long idRole) {

		String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_ROLE;
		UserHasRole userHasRole = new UserHasRole(userCrud.findOne(idUser), roleCrud.findOne(idRole));
		userRoleCrud.delete(userHasRole);
		return redirect;
	}

	/**NAME : addRoleToUserPOST
	 *
	 * Add the selected role to the selected user
	 * @param model
	 * @param idUser
	 * @param idRole
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + ADD_ROLE, method = RequestMethod.POST)
	public String addRoleToUserPOST(Model model,@PathVariable Long idUser, Long idRole) {

		String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_ROLE;
		UserHasRole userHasRole = new UserHasRole(userCrud.findOne(idUser), roleCrud.findOne(idRole));
		userRoleCrud.save(userHasRole);

		return redirect;
	}

	/**NAME : getAllRolesForUser
	 *
	 * Return all roles of the selected user
	 *
	 * @param idUser
	 * @return
	 */
	public ArrayList<RoleCGI> getAllRolesForUser(Long idUser) {

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

	/**NAME : addRoleforUserGET
	 *
	 * Show the page to add a role to an User
	 *
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + ADD_ROLE, method = RequestMethod.GET)
	public <T> String addRoleforUserGET(Model model, @PathVariable Long idUser) {

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
			listNiko =  (List<NikoNiko>) nikoCrud.findAll(listNikoId);
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
				listNiko =  (List<NikoNiko>) nikoCrud.findAll(listNikoId);
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
	
	
	
	
	// TODO : RELATION USER -> VERTICAL
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * ASSOCIATION USER --> VERTICAL
	 *
	 */

	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**NAME : showTeamsForUserGET
	 *
	 * RELATION USER HAS TEAM
	 *
	 * @param model
	 * @param id
	 * @return
	 */

	//@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_VERTICAL, method = RequestMethod.GET)
	public String showVerticalForUserGET(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("page",userBuffer.getRegistration_cgi());
		model.addAttribute("sortedFields",Verticale.FIELDS);
		model.addAttribute("items",this.getVerticalForUser(idUser));
		model.addAttribute("show_teams", DOT + PATH + SHOW_VERTICAL);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addTeams");

		return BASE_USER + PATH + SHOW_VERTICAL;
	
	}
	
	// TODO : ARRAYLIST CAN BE CONVERT TO A LONG
	public ArrayList<Verticale> getVerticalForUser(Long idUser){

		ArrayList<Verticale> verticaleList = new ArrayList<Verticale>();
		
		Long idVerticale = userCrud.getUserVertical(idUser);
		
		verticaleList.add(verticaleCrud.findOne(idVerticale));
		
		return verticaleList;
		
		
	}

///**NAME : quiTeamPOST
//*
//* Delete the selected relation team-user and redirect to the userhasteams view (by using quitTeam())
//* SHOW POST THAT UPDATE USER RELATION WITH TEAM WHEN A USER QUIT A TEAM
//*
//* @param model
//* @param idUser
//* @param idTeam
//* @return
//*/
//
//@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP"})
//@RequestMapping(path = "{idUser}" + PATH + SHOW_TEAM, method = RequestMethod.POST)
//public String quiTeamPOST(Model model,@PathVariable Long idUser, Long idTeam) {
//return quitTeam(idUser, idTeam);
//}
//
///**NAME : addUserInTeamGET
//*
//* ADD USER FOR CURRENT TEAM
//*
//* @param model
//* @param idUser
//* @return
//*/
//
//@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
//@RequestMapping(path = "{idUser}" + PATH + ADD_TEAM, method = RequestMethod.GET)
//public <T> String addUserInTeamGET(Model model, @PathVariable Long idUser) {
//
//Object userBuffer = new Object();
//userBuffer = userCrud.findOne(idUser);
//model.addAttribute("items", DumpFields.listFielder((ArrayList<Team>) teamCrud.findAll()));
//model.addAttribute("sortedFields",Team.FIELDS);
//model.addAttribute("page", ((User) userBuffer).getRegistration_cgi());
//model.addAttribute("go_show", SHOW_ACTION);
//model.addAttribute("go_create", CREATE_ACTION);
//model.addAttribute("go_delete", DELETE_ACTION);
//model.addAttribute("back", DOT + PATH + SHOW_TEAM);
//model.addAttribute("add", ADD_TEAM);
//
//return BASE_USER + PATH + ADD_TEAM;
//}
//
///**NAME : addUserInTeamPOST
//*
//* ADD USER FOR CURRENT TEAM
//*
//* @param model
//* @param idUser
//* @param idTeam
//* @return
//*/
//
//@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
//@RequestMapping(path = "{idUser}" + PATH + ADD_TEAM, method = RequestMethod.POST)
//public <T> String addUserInTeamPOST(Model model, @PathVariable Long idUser, Long idTeam) {
//return setUsersForTeam(idTeam, idUser);
//}
//
///**NAME : findAllTeamsForUser
//*
//* Find all teams related to a user by checking relation table user_has_team
//*
//* @param idValue
//* @return teamList (list of user associated to a team)
//*/
//public ArrayList<Team> findAllTeamsForUser(Long idValue) {
//
//List<Long> ids = new ArrayList<Long>();
//ArrayList<Team> teamList = new ArrayList<Team>();
//
//List<BigInteger> idsBig = userTeamCrud.findAssociatedTeam(idValue);
//
//if (!idsBig.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
//for (BigInteger id : idsBig) {
//ids.add(id.longValue());
//}
//teamList = (ArrayList<Team>) teamCrud.findAll(ids);
//}
//
//return teamList;
//}
//
///**NAME : setUsersForTeam
//*
//* Put an user in a new team by creating a new  association in user_has_team (or modify if exists)
//*
//* @param idTeam
//* @param idUser
//* @return
//*/
//public String setUsersForTeam(Long idTeam,Long idUser){
//
//userTeamCrud.save(new UserHasTeam(userCrud.findOne(idUser), teamCrud.findOne(idTeam), new Date()));
//
//String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_TEAM;
//
//return redirect;
//}
//
///**NAME : quitTeam
//*
//* Set the leaving date in user_has_team table when a user leave a team
//*
//* @param idUser
//* @param idTeam
//* @return redirect (path redirection after action)
//*/
//public String quitTeam(Long idUser, Long idTeam){
//
//Date date = new Date();
//
//UserHasTeam userHasTeamBuffer = userTeamCrud.findOne(new AssociationItemId(idUser, idTeam));
//userHasTeamBuffer.setLeavingDate(date);
//
//userTeamCrud.save(userHasTeamBuffer);
//
//String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_TEAM;
//
//return redirect;
//}
//
///** NAME : getTeamsForUser
//*
//* FUNCTION RETURNING ALL TEAM RELATED WITH ONE USER WITH leaving_date = null
//*
//* @param idUser
//* @return
//*/
//public ArrayList<Map<String, Object>> getTeamsForUser(Long idUser){
//
//ArrayList<Long> ids = new ArrayList<Long>();
//ArrayList<Team> teamList = new ArrayList<Team>();
//ArrayList<UserHasTeam> userHasTeamList = new ArrayList<UserHasTeam>();
//ArrayList<UserHasTeam> userHasTeamListClean = new ArrayList<UserHasTeam>();
//
//teamList = findAllTeamsForUser(idUser);
//
//for (int i = 0; i < teamList.size(); i++) {
//userHasTeamList.add(userTeamCrud.findAssociatedUserTeamALL(idUser, teamList.get(i).getId()));
//
//if(userHasTeamList.get(i).getLeavingDate() == null){
//
//userHasTeamListClean.add(userHasTeamList.get(i));
//ids.add(userHasTeamList.get(i).getIdRight());
//
//}
//}
//return DumpFields.listFielder((List<Team>) teamCrud.findAll(ids));
//}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
