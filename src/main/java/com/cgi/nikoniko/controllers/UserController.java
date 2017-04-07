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
import com.cgi.nikoniko.models.association.UserHasRole;
import com.cgi.nikoniko.models.association.UserHasTeam;
import com.cgi.nikoniko.models.association.base.AssociationItemId;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;
import com.cgi.nikoniko.models.tables.Verticale;
import com.cgi.nikoniko.utils.DumpFields;
import com.cgi.nikoniko.utils.UtilsFunctions;

@Controller
@RequestMapping(UserController.BASE_URL)
public class UserController extends ViewBaseController<User> {


/////////////////// GLOBAL CONSTANT /////////////////////////////////
	

	public final static String BASE_USER = "user";
	public final static String BASE_URL = PathFinder.PATH + BASE_USER;
	public final static double TIME = 0.999999;//can't use 1 due to double type

	//TODO : ce n'est pas une variable statique!!!!
	public final static LocalDate TODAY_DATE = new LocalDate();



/////////////////// NECESSARY CRUD /////////////////////////////////


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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	*
	* FONCTIONS RELATED TO USER ONLY
	*
	*/

	/////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * LIST USER METHOD POST
	 * @param model
	 * @param name
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = {PathFinder.PATH, PathFinder.ROUTE_LIST}, method = RequestMethod.POST)
	public String showUsers(Model model,String name){
		
		if (name == "") {
			return PathFinder.REDIRECT + PathFinder.ROUTE_LIST;
			}

		model.addAttribute("model", "user");
		model.addAttribute("page",this.baseName);
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("items",UtilsFunctions.searchUser(name, userCrud));
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		
		return listView;

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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.SHOW_PATH, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long idUser) {

		Long idverticale = null;

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		// ADD A DEFAUT VERTICALE
		if (userBuffer.getVerticale() == null) {
			idverticale = 1L;

			userBuffer.setVerticale(verticaleCrud.findOne(idverticale));
			userCrud.save(userBuffer);
		}
		else {
			 idverticale = userBuffer.getVerticale().getId();
		}

		model.addAttribute("page",  "USER : " + userBuffer.getRegistrationcgi());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(idUser)));
		model.addAttribute("show_nikonikos", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_NIKONIKO);
		model.addAttribute("show_graphique", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_GRAPH);
		model.addAttribute("show_verticale", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_VERTICAL);
		model.addAttribute("show_teams", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_TEAM);
		model.addAttribute("show_roles", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_ROLE);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("go_update", PathFinder.UPDATE_ACTION);


		return BASE_USER + PathFinder.PATH + PathFinder.SHOW_PATH;
	}

	/**
	 * SELECT ALL NIKONIKOS FOR A USER
	 * @param model
	 * @param userId
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{userId}" + PathFinder.PATH + PathFinder.SHOW_NIKONIKO, method = RequestMethod.GET)
	public String getNikoNikosForUser(Model model, @PathVariable Long userId) {

		User user = super.getItem(userId);
		Set<NikoNiko> niko =  user.getNikoNikos();
		List<NikoNiko> listOfNiko = new ArrayList<NikoNiko>(niko);
		model.addAttribute("page", user.getFirstname() + " nikonikos");
		model.addAttribute("sortedFields", NikoNiko.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfNiko));
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);

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

		try {
			userCrud.findOne(userId).getId();
		} catch (Exception e) {
			response.sendError(HttpStatus.FORBIDDEN.value(),("Don't try to hack url!").toUpperCase());
			return null;
		}

		if (userCrud.findByLogin(super.checkSession().getName()).getId()!= userId) {
			response.sendError(HttpStatus.FORBIDDEN.value(),("Don't try to hack url!").toUpperCase());
		}

		Long userBuffer = UtilsFunctions.getUserInformations(userCrud).getId();

		User user = super.getItem(userId);
		NikoNiko niko = new NikoNiko();



		niko = nikonikoCrud.findOne(userCrud.getLastNikoNikoUser(userId));
		if (niko.getComment().isEmpty()) {
			model.addAttribute("textAreaOption","");
		} else {
			model.addAttribute("textAreaOption",niko.getComment());
		}


		model.addAttribute("lastMood", UtilsFunctions.getLastLastNikoNikoMood(userBuffer, userCrud, nikonikoCrud));
		model.addAttribute("status", UtilsFunctions.checkDateNikoNiko(userBuffer, userCrud, nikonikoCrud));
		model.addAttribute("mood" , UtilsFunctions.getUserLastMood(userBuffer, userCrud, nikonikoCrud));
		model.addAttribute("page",user.getFirstname() + " " + PathFinder.CREATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",NikoNiko.FIELDS);
		model.addAttribute("item",DumpFields.createContentsEmpty(niko.getClass()));
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);
		model.addAttribute("create_item", PathFinder.CREATE_ACTION);

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
	*
	* FUNCTION THAT SAVE THE NIKONIKO IN DB IN FUNCTION OF THE DATE
	*
	* @param idUser, mood, comment
	* @return
	*/
	public String addNikoNikoInDB(Long idUser, Integer mood, String comment){


		Date date = TODAY_DATE.toDate();
		User user = new User();

		user = userCrud.findOne(idUser);

		if (UtilsFunctions.checkDateNikoNiko(idUser, userCrud, nikonikoCrud) == true) {

			if (mood ==  null) {

				return PathFinder.REDIRECT + PathFinder.PATH + PathFinder.MENU_PATH;
			}

			else {

				Long idMax = userCrud.getLastNikoNikoUser(idUser);
				NikoNiko nikoUpdate = nikonikoCrud.findOne(idMax);

				nikoUpdate.setChange_date(date);
				nikoUpdate.setComment(comment);
				nikoUpdate.setMood(mood);

				nikonikoCrud.save(nikoUpdate);

				return PathFinder.REDIRECT + PathFinder.PATH + PathFinder.MENU_PATH;
			}
		}

		else {

			if (mood ==  null) {

				return PathFinder.REDIRECT + PathFinder.PATH + PathFinder.MENU_PATH;
			}

			else {

				NikoNiko niko = new NikoNiko(user,mood,date,comment);
				nikonikoCrud.save(niko);
				return PathFinder.REDIRECT + PathFinder.PATH + PathFinder.MENU_PATH;
			}
		}
	}

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
	@RequestMapping(path = "{userId}/addLast", method = RequestMethod.POST)
	public String lastNikoNikoForUserPOST(Model model,@PathVariable Long userId,
						HttpServletResponse response, Integer mood, String comment) throws IOException {

		return UtilsFunctions.updateLastNikoNiko(userId, mood, comment, nikonikoCrud, userCrud);

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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.SHOW_TEAM, method = RequestMethod.GET)
	public String showTeamsForUserGET(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("page",userBuffer.getRegistrationcgi());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items",this.getTeamsForUser(idUser));
		model.addAttribute("show_teams", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_TEAM);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);
		model.addAttribute("add", "addTeams");

		return BASE_USER + PathFinder.PATH + PathFinder.SHOW_TEAM;
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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.SHOW_TEAM, method = RequestMethod.POST)
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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.ADD_TEAM, method = RequestMethod.GET)
	public String addUserInTeamGET(Model model, @PathVariable Long idUser) {

		Object userBuffer = new Object();
		userBuffer = userCrud.findOne(idUser);
		model.addAttribute("items", DumpFields.listFielder((ArrayList<Team>) teamCrud.findAll()));
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("page", ((User) userBuffer).getRegistrationcgi());
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_TEAM);
		model.addAttribute("add", PathFinder.ADD_TEAM);

		return BASE_USER + PathFinder.PATH + PathFinder.ADD_TEAM;
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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.ADD_TEAM,  params = "idTeam", method = RequestMethod.POST)
	public String addUserInTeamPOST(Model model, @PathVariable Long idUser, @RequestParam Long idTeam) {
		return setUsersForTeam(idTeam, idUser);
	}

	/**
	 * SHOW USERS TO ADD ON VERTICALE (SEARCH)
	 * @param model
	 * @param name
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.ADD_TEAM, params = "name", method = RequestMethod.POST)
	public String addVerticalForTeamPOST(Model model,@RequestParam String name , @PathVariable Long idUser){

		User userBuffer = userCrud.findOne(idUser);

		model.addAttribute("model", "user");
		model.addAttribute("page", userBuffer.getRegistrationcgi());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items",DumpFields.listFielder(UtilsFunctions.searchTeam(name, teamCrud)));
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_TEAM);

		return BASE_USER + PathFinder.PATH + PathFinder.ADD_TEAM;

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

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + BASE_USER + PathFinder.PATH + idUser + PathFinder.PATH + PathFinder.SHOW_TEAM;

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

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + BASE_USER + PathFinder.PATH + idUser + PathFinder.PATH + PathFinder.SHOW_TEAM;

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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.SHOW_ROLE, method = RequestMethod.GET)
	public String showRolesForUserGET(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("page",userBuffer.getRegistrationcgi());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(this.getAllRolesForUser(idUser)));
		model.addAttribute("show_roles", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_ROLE);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);
		model.addAttribute("add", "addRoles");

		return BASE_USER + PathFinder.PATH + PathFinder.SHOW_ROLE;
	}

	/**
	 * REVOKE THE SELECT ROLE FOR USER
	 * @param model
	 * @param idUser
	 * @param idRole
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.SHOW_ROLE, method = RequestMethod.POST)
	public String revokeRoleToUserPOST(Model model,@PathVariable Long idUser, Long idRole) {

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + BASE_USER + PathFinder.PATH + idUser + PathFinder.PATH + PathFinder.SHOW_ROLE;
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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.ADD_ROLE, method = RequestMethod.POST)
	public String addRoleToUserPOST(Model model,@PathVariable Long idUser, Long idRole) {

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + BASE_USER + PathFinder.PATH + idUser + PathFinder.PATH + PathFinder.SHOW_ROLE;
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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.ADD_ROLE, method = RequestMethod.GET)
	public String addRoleforUserGET(Model model, @PathVariable Long idUser) {

		Object userBuffer = new Object();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("items", DumpFields.listFielder((ArrayList<RoleCGI>) roleCrud.findAll()));
		model.addAttribute("sortedFields",RoleCGI.FIELDS);
		model.addAttribute("page", ((User) userBuffer).getRegistrationcgi());
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete",PathFinder.DELETE_ACTION);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_ROLE);
		model.addAttribute("add", PathFinder.ADD_ROLE);

		return BASE_USER + PathFinder.PATH + PathFinder.ADD_ROLE;
	}


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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.SHOW_VERTICAL, method = RequestMethod.GET)
	public String showVerticalForUserGET(Model model,@PathVariable Long idUser) {

		User userBuffer = new User();
		userBuffer = userCrud.findOne(idUser);

		model.addAttribute("page",userBuffer.getRegistrationcgi());
		model.addAttribute("sortedFields",Verticale.FIELDS);
		model.addAttribute("items",this.getVerticalForUser(idUser));
		model.addAttribute("show_teams", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_VERTICAL);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);
		model.addAttribute("add", PathFinder.ADD_VERTICAL);

		return BASE_USER + PathFinder.PATH + "showVerticale";

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
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.ADD_VERTICAL, method = RequestMethod.GET)
	public String addVerticalForUserGET(Model model, @PathVariable Long idUser) {

	Object userBuffer = new Object();
	userBuffer = userCrud.findOne(idUser);

	model.addAttribute("items", DumpFields.listFielder((ArrayList<Verticale>) verticaleCrud.findAll()));
	model.addAttribute("sortedFields",Verticale.FIELDS);
	model.addAttribute("page", ((User) userBuffer).getRegistrationcgi());
	model.addAttribute("go_show", PathFinder.SHOW_ACTION);
	model.addAttribute("go_create", PathFinder.CREATE_ACTION);
	model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
	model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_VERTICAL);
	model.addAttribute("add", PathFinder.ADD_VERTICAL);

	return BASE_USER + PathFinder.PATH + PathFinder.ADD_VERTICAL;
	}

	/**
	 * ADD ONE VERTICALE TO USER
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idUser}" + PathFinder.PATH + PathFinder.ADD_VERTICAL, method = RequestMethod.POST)
	public String addVerticalForUserPOST(Model model, @PathVariable Long idUser, Long idVertical) {
		return setVerticalForUser(idUser, idVertical);
	}

	/**
	 * SET A VERTICALE FOR ONE USER
	 * @param idUser
	 * @param idVertical
	 * @return
	 */
	private String setVerticalForUser(Long idUser, Long idVertical) {

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + BASE_USER + PathFinder.PATH + idUser + PathFinder.PATH + PathFinder.SHOW_VERTICAL;

		User userBuffer = new User();
		Verticale verticaleBuffer = new Verticale();

		userBuffer = userCrud.findOne(idUser);
		verticaleBuffer = verticaleCrud.findOne(idVertical);

		userBuffer.setVerticale(verticaleBuffer);
		userCrud.save(userBuffer);

		return redirect;
	}


/////////////////// CONTRUCTORS /////////////////////////////////

	public UserController() {
		super(User.class,BASE_URL);
	}

	protected UserController(Class<User> clazz, String baseURL) {
		super(clazz, baseURL);
	}
}
