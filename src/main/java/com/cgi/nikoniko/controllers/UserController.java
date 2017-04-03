package com.cgi.nikoniko.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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

	public final static String SHOW_TEAM = "showTeam";
	public final static String SHOW_ROLE = "showRole";
	public final static String SHOW_LINK = "link";
	public final static String SHOW_VERTICAL = "showVerticale";

	public final static String ADD_TEAM = "addTeams";
	public final static String ADD_ROLE = "addRoles";
	public final static String ADD_VERTICAL = "addVerticale";

	public final static String REDIRECT = "redirect:";

	public final static double TIME = 0.999999;

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

	/**
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

		// ADD A DEFAUT VERTICALE
		if (userBuffer.getVerticale() == null) {
			idverticale = 1L;

			userBuffer.setVerticale(verticaleCrud.findOne(1L));
			userCrud.save(userBuffer);
		}
		else {
			 idverticale = userBuffer.getVerticale().getId();
		}

		model.addAttribute("page",  "USER : " + userBuffer.getRegistrationcgi());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(idUser)));
		model.addAttribute("show_nikonikos", DOT + PATH + SHOW_NIKONIKO);
		model.addAttribute("show_graphique", DOT + PATH + SHOW_GRAPH);
		model.addAttribute("show_verticale", DOT + PATH + SHOW_VERTICAL);
		model.addAttribute("show_teams", DOT + PATH + SHOW_TEAM);
		model.addAttribute("show_roles", DOT + PATH + SHOW_ROLE);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);


		return BASE_USER + PATH + SHOW_PATH;
	}

	/**
	 * SELECT ALL NIKONIKOS FOR A USER
	 * @param model
	 * @param userId
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{userId}" + PATH + SHOW_NIKONIKO, method = RequestMethod.GET)
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
	 * CREATION PAGE FOR A NEW NIKONIKO
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

		model.addAttribute("lastMood", this.getLastLastNikoNikoMood(userCrud.findByLogin(super.checkSession().getName()).getId()));

		model.addAttribute("status", this.checkDateNikoNiko(userCrud.findByLogin(super.checkSession().getName()).getId()));
		model.addAttribute("mood" , this.getUserLastMood(userCrud.findByLogin(super.checkSession().getName()).getId()));
		model.addAttribute("page",user.getFirstname() + " " + CREATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",NikoNiko.FIELDS);
		model.addAttribute("item",DumpFields.createContentsEmpty(niko.getClass()));
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("create_item", CREATE_ACTION);

		return "nikoniko/addNikoNiko";
	}

	/**
	 * CREATION PAGE FOR A NEW NIKONIKO
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
	 *	CHECK THE LAST MOOD OF USER IF IT EXISTS OR NOT
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
	 * CHECK LAST-1 NIKONIKO IF IT EXISTS
	 * @param idUser
	 * @return
	 */
	public Boolean getLastLastNikoNikoMood(Long idUser){

		Long idMax = userCrud.getLastLastNikoNikoUser(idUser);

		NikoNiko lastLastNiko = nikonikoCrud.findOne(userCrud.getLastLastNikoNikoUser(idUser));

		if (lastLastNiko == null) {
			return false;
		}

		else {

			Integer mood = lastLastNiko.getMood();

			if (mood == 0 || mood == null) {
				return true;
			}

			else {
				return false;
			}
		}
	}

	/**
	 * CHECK IF A USER AS ENTER A NIKONIKO DURING THE DAY
	 * @param idUser
	 * @return
	 */
	public Boolean checkDateNikoNiko(Long idUser){

		Boolean updateNiko = null;
		LocalDate todayDate = new LocalDate();

		Long idMaxNiko = userCrud.getLastNikoNikoUser(idUser);

		if (idMaxNiko == null) {
			updateNiko = false;
		}

		else {

			NikoNiko lastNiko = nikonikoCrud.findOne(idMaxNiko);
			Date entryDate = lastNiko.getEntryDate();
			LocalDate dateEntry = new LocalDate(entryDate);

			if (todayDate.isAfter(dateEntry)) {

					updateNiko = false;
				}

				else {

					updateNiko = true;
				}
			}

		return updateNiko;
	}

	/**
	*
	* FUNCTION THAT SAVE THE NIKONIKO IN DB IN FUNCTION OF THE DATE
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

	/**
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

		model.addAttribute("page",userBuffer.getRegistrationcgi());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items",this.getTeamsForUser(idUser));
		model.addAttribute("show_teams", DOT + PATH + SHOW_TEAM);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addTeams");

		return BASE_USER + PATH + SHOW_TEAM;
	}

	/**
	 *
	 * FUNCTION THAT RETIRE A TEAM FOR A USER
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

	/**
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
		model.addAttribute("page", ((User) userBuffer).getRegistrationcgi());
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", DOT + PATH + SHOW_TEAM);
		model.addAttribute("add", ADD_TEAM);

		return BASE_USER + PATH + ADD_TEAM;
	}

	/**
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

	/**
	 *
	 * FIND ALL TEAMS RELATED TO A USER
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

	/**
	 *
	 * PUT AND USER IN NEW TEAM BY CREATING NEW ASSOCIATION
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

	/**
	 * SET LEAVING DATE IN user_has_team WHEN A USER LEAVE A TEAM
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

	/**
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

	/**
	 * SHOW ALL ROLES RELATED TO ONE USER
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PATH + SHOW_ROLE, method = RequestMethod.GET)
	public String showRolesForUserGET(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("page",userBuffer.getRegistrationcgi());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(this.getAllRolesForUser(idUser)));
		model.addAttribute("show_roles", DOT + PATH + SHOW_ROLE);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addRoles");

		return BASE_USER + PATH + SHOW_ROLE;
	}

	/**
	 * REVOKE THE SELECT ROLE FOR USER
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

	/**
	 * ADD ROLE TO USER
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

	/**
	 * RETURN ALL ROLES RELATED TO USER
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

	/**
	 * PAGE TO ADD ROLES TO USER
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
		model.addAttribute("page", ((User) userBuffer).getRegistrationcgi());
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", DOT + PATH + SHOW_ROLE);
		model.addAttribute("add", ADD_ROLE);

		return BASE_USER + PATH + ADD_ROLE;
	}

	// TODO : RELATION USER -> VERTICAL

	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * ASSOCIATION USER --> VERTICAL
	 *
	 */

	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * SHOW VERTICALE FOR ONE USER
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

		model.addAttribute("page",userBuffer.getRegistrationcgi());
		model.addAttribute("sortedFields",Verticale.FIELDS);
		model.addAttribute("items",this.getVerticalForUser(idUser));
		model.addAttribute("show_teams", DOT + PATH + SHOW_VERTICAL);
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		model.addAttribute("add", "addVerticale");

		return BASE_USER + PATH + SHOW_VERTICAL;

	}

	/**
	 * GET VERTICAL FOR ONE USER
	 * @param idUser
	 * @return
	 */
	public ArrayList<Verticale> getVerticalForUser(Long idUser){
		ArrayList<Verticale> verticaleList = new ArrayList<Verticale>();
		Long idVerticale = userCrud.getUserVertical(idUser);
		verticaleList.add(verticaleCrud.findOne(idVerticale));
		return verticaleList;
	}

	/**
	 * SHOW VERTICAL TO ADD USER
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idUser}" + PATH + ADD_VERTICAL, method = RequestMethod.GET)
	public <T> String addVerticalForUserGET(Model model, @PathVariable Long idUser) {

	Object userBuffer = new Object();
	userBuffer = userCrud.findOne(idUser);

	model.addAttribute("items", DumpFields.listFielder((ArrayList<Verticale>) verticaleCrud.findAll()));
	model.addAttribute("sortedFields",Verticale.FIELDS);
	model.addAttribute("page", ((User) userBuffer).getRegistrationcgi());
	model.addAttribute("go_show", SHOW_ACTION);
	model.addAttribute("go_create", CREATE_ACTION);
	model.addAttribute("go_delete", DELETE_ACTION);
	model.addAttribute("back", DOT + PATH + SHOW_VERTICAL);
	model.addAttribute("add", ADD_VERTICAL);

	return BASE_USER + PATH + ADD_VERTICAL;
	}

	/**
	 * ADD ONE VERTICALE TO USER
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idUser}" + PATH + ADD_VERTICAL, method = RequestMethod.POST)
	public <T> String addVerticalForUserPOST(Model model, @PathVariable Long idUser, Long idVertical) {
		return setVerticalForUser(idUser, idVertical);
	}

	/**
	 * SET A VERTICALE FOR ONE USER
	 * @param idUser
	 * @param idVertical
	 * @return
	 */
	private String setVerticalForUser(Long idUser, Long idVertical) {

		String redirect = REDIRECT + PATH + BASE_USER + PATH + idUser + PATH + SHOW_VERTICAL;

		User userBuffer = new User();
		Verticale verticaleBuffer = new Verticale();

		userBuffer = userCrud.findOne(idUser);
		verticaleBuffer = verticaleCrud.findOne(idVertical);

		userBuffer.setVerticale(verticaleBuffer);
		userCrud.save(userBuffer);


		return redirect;
	}

	/////////////////////////////////////////////////////////////////////

	/**
	 *
	 * SEARCH USER, STATUS : WORK
	 *
	 */

	/////////////////////////////////////////////////////////////////////

	/**
	 * LIST USER METHOD POST
	 * @param model
	 * @param name
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = {PATH, ROUTE_LIST}, method = RequestMethod.POST)
	public String showUsers(Model model,String name){

		model.addAttribute("model", "");
		model.addAttribute("page",this.baseName + " " + LIST_ACTION.toUpperCase());
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("items",this.searchUser(name));
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
	public ArrayList<User> searchUser(String name){

		ArrayList<User> userList = new ArrayList<User>();
		userList = userCrud.getUsers(name);

		return userList;

	}

	////////////////////////////// IN WORK //////////////////////////////

	/**
	 * RETURN PAGE VOTE TO VOTE FOR THE PREVIOUS NIKONIKO
	 * @param model
	 * @param userId
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{userId}/addLast", method = RequestMethod.GET)
	public String lastNikoNikoForUserGET(Model model,@PathVariable Long userId,
						HttpServletResponse response) throws IOException {

		return "nikoniko/addNikoNikoLast";
	}

	/**
	 * UPDATE THE PREVIOUS NIKONIKO VOTE BY USER
	 * @param model
	 * @param userId
	 * @param response
	 * @param mood
	 * @param comment
	 * @return
	 * @throws IOException
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{userId}/addNikoNikoLast", method = RequestMethod.POST)
	public String lastNikoNikoForUserPOST(Model model,@PathVariable Long userId,
						HttpServletResponse response, int mood, String comment) throws IOException {

		return this.updateLastNikoNiko(userId, mood, comment);
	}

	/**
	 * FUNCTION FOR UPDATE THE PREVIOUS NIKONIKO VOTE BY USER
	 * @param idUser
	 * @param mood
	 * @param comment
	 * @return
	 */
	public String updateLastNikoNiko(Long idUser, Integer mood, String comment){

		if (mood == null) {
			return REDIRECT + PATH + MENU_PATH;
		}

		else {

			NikoNiko lastNiko = nikonikoCrud.findOne(userCrud.getLastLastNikoNikoUser(idUser));

			lastNiko.setMood(mood);
			lastNiko.setComment(comment);
			nikonikoCrud.save(lastNiko);

			return REDIRECT + PATH + MENU_PATH;

		}
	}

	//////////////////////////////////////////////////////////////////////

}
