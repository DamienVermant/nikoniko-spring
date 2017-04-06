package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

/////////////////// GLOBAL CONSTANT /////////////////////////////////

	public final static String BASE_URL = PathFinder.PATH +PathFinder.VERTICALE;

	public final static Long DEFAULT_ID_VERTICAL = (long) 1;


/////////////////// ALL CRUD /////////////////////////////////


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


/////////////////// FUNCTION RELATED TO VERTICAL ONLY /////////////////////////////////


	/**
	 *SHOW SPECIFIC VERTICAL WITH A GIVEN ID
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = PathFinder.ROUTE_SHOW, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long id) {

		Verticale verticaleBuffer = new Verticale();
		verticaleBuffer = verticaleCrud.findOne(id);

		model.addAttribute("page","Verticale : " + verticaleBuffer.getName());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("show_users", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_USERS);
		model.addAttribute("show_team", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_TEAM);
		model.addAttribute("go_index", PathFinder.LIST_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("go_update", PathFinder.UPDATE_ACTION);

	return PathFinder.BASE_VERTICALE + PathFinder.PATH + PathFinder.SHOW_PATH;
}

	/**
	 * SHOW THE LIST OF VERTICALES
	 * @param model
	 * @param name
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = {PathFinder.PATH, PathFinder.ROUTE_LIST}, method = RequestMethod.POST)
	public String showVerticalesPOST(Model model,String name){

		model.addAttribute("model", "verticale");
		model.addAttribute("page",this.baseName + " " + PathFinder.LIST_ACTION.toUpperCase());
		model.addAttribute("sortedFields",Verticale.FIELDS);
		model.addAttribute("items",this.searchVerticales(name));
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);

		return listView;
	}

	/**
	 * FIND A SPECIFIC VERTICAL WITH A GIVEN NAME
	 * @param name
	 * @return
	 */
	public ArrayList<Verticale> searchVerticales(String name){

		ArrayList<Verticale> verticaleList = new ArrayList<Verticale>();
		verticaleList = verticaleCrud.getVerticales(name);

		return verticaleList;

	}


/////////////////// RELATION FUNCTIONS BETWEEN VERTICALE & USER /////////////////////////////////


	/**
	 * SHOW USERS RELATED TO A VERTICALE
	 * @param model
	 * @param verticaleId
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}"+ PathFinder.PATH + PathFinder.SHOW_USERS, method = RequestMethod.GET)
	public String getUsersForVerticaleGET(Model model, @PathVariable Long verticaleId) {

		Verticale verticale = super.getItem(verticaleId);
		Set<User> user =  verticale.getUsers();
		List<User> listOfUser = new ArrayList<User>(user);

		model.addAttribute("idVerticale", verticaleId);
		model.addAttribute("page", verticale.getName());
		model.addAttribute("type","user");
		model.addAttribute("sortedFields", User.FIELDS);
		model.addAttribute("add", PathFinder.DOT + PathFinder.PATH + PathFinder.ADD_USER);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);

		if (listOfUser.size() < LENGHT_VIEW) {
			model.addAttribute("items",DumpFields.listFielder(listOfUser));
		}else{
			model.addAttribute("items",DumpFields.listFielder(listOfUser.subList(0, LENGHT_VIEW)));
		}

		return PathFinder.BASE_VERTICALE + PathFinder.PATH + PathFinder.SHOW_USERS;
	}

	/**
	 * DELETE USER TO ONE VERTICALE
	 * @param model
	 * @param verticaleId
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}"+ PathFinder.PATH + PathFinder.SHOW_USERS, method = RequestMethod.POST)
	public String getUsersForVerticalePOST(Model model, @PathVariable Long verticaleId, Long idUser) {
		return defaultVerticalForUser(idUser, verticaleId);
	}

	/**
	 * SHOW USERS TO ADD ON VERTICALE (SEARCH)
	 * @param model
	 * @param name
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{verticaleId}"+ PathFinder.PATH + PathFinder.SHOW_USERS, params = "name", method = RequestMethod.POST)
	public String addVerticalForUserPOST(Model model,@RequestParam String name , @PathVariable Long verticaleId){

		Verticale verticalBuffer = verticaleCrud.findOne(verticaleId);
		Set<User> user =  verticalBuffer.getUsers();
		List<User> listOfUser = new ArrayList<User>(user);

		model.addAttribute("model", "team");
		model.addAttribute("idVerticale", verticaleId);
		model.addAttribute("page", verticalBuffer.getName());
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);
		model.addAttribute("add", PathFinder.DOT + PathFinder.PATH + PathFinder.ADD_USER);

		if (name != "") {
			model.addAttribute("items",DumpFields.listFielder(this.searchAssociatedUsers(verticaleId, name)));
		}
		else {

			if (listOfUser.size() < LENGHT_VIEW) {
				model.addAttribute("items",DumpFields.listFielder(listOfUser));
			}else{
				model.addAttribute("items",DumpFields.listFielder(listOfUser.subList(0, LENGHT_VIEW)));
			}

		}

		return PathFinder.BASE_VERTICALE + PathFinder.PATH + PathFinder.SHOW_USERS;

	}

	/**
	 * SEARCH ALL USERS ASSOCIATED WITH THE SAME VERTICALE
	 * @param idVerticale
	 * @param name
	 * @return
	 */
	public ArrayList<User> searchAssociatedUsers(Long idVerticale, String name){

		ArrayList<User> userList = new ArrayList<User>();
		ArrayList<User> userListClean = new ArrayList<User>();

		userList = userCrud.getAssociatedUsers(idVerticale);

		for (User user : userList) {
			if (user.getRegistrationcgi().toLowerCase().equals(name)) {
				userListClean.add(user);
			}
		}

		return userListClean;
	}

	/**
	 * SHOW USERS TO ADD ON VERTICALE
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})

	@RequestMapping(path = "{idVerticale}" + PathFinder.PATH + PathFinder.ADD_USER , method = RequestMethod.GET)
	public String addVerticalForUserGET(Model model, @PathVariable Long idVerticale) {

	ArrayList<User> userList = new ArrayList<User>();

	Object verticaleBuffer = new Object();
	verticaleBuffer = verticaleCrud.findOne(idVerticale);

	model.addAttribute("items", DumpFields.listFielder(userList));
	model.addAttribute("sortedFields",User.FIELDS);
	model.addAttribute("page", ((Verticale) verticaleBuffer).getName());
	model.addAttribute("go_show", PathFinder.SHOW_ACTION);
	model.addAttribute("go_create", PathFinder.CREATE_ACTION);
	model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
	model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_USERS);
	model.addAttribute("add", PathFinder.ADD_USER);

	return PathFinder.BASE_VERTICALE + PathFinder.PATH + PathFinder.ADD_USER;
	}

	/**
	 *
	 * SHOW USERS TO ADD ON VERTICALE (SEARCH)
	 * @param model
	 * @param name
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idVerticale}" + PathFinder.PATH + PathFinder.ADD_USER, params = "name", method = RequestMethod.POST)
	public String addVerticalForUserPOST(Model model,@RequestParam String name){

		model.addAttribute("model", "user");
		model.addAttribute("page",this.baseName + " " + PathFinder.LIST_ACTION.toUpperCase());
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("items",DumpFields.listFielder(this.searchUser(name)));
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_USERS);

		return PathFinder.BASE_VERTICALE + PathFinder.PATH + PathFinder.ADD_USER;

	}

	/**
	 * SEARCH USER WITH HIS REGISTRATION CGI
	 * @param name
	 * @return
	 */
	public ArrayList<User> searchUser(String name){

		ArrayList<User> userList = new ArrayList<User>();
		userList = userCrud.getUsers(name);

		return userList;

	}

	/**
	 * ADD ONE USER TO VERTICALE
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idVertical}" + PathFinder.PATH + PathFinder.ADD_USER, params = "idUser",  method = RequestMethod.POST)
	public String addVerticalForUserPOST(Model model,@RequestParam Long idUser, @PathVariable Long idVertical) {
		return setUserforVertical(idUser, idVertical);
	}

	/**
	 * FUNCTION USED FOR ADD ONE USER TO VERTICALE
	 * @param idUser
	 * @param idVertical
	 * @return
	 */
	private String setUserforVertical(Long idUser, Long idVertical) {

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + PathFinder.BASE_VERTICALE + PathFinder.PATH + idVertical + PathFinder.PATH + PathFinder.SHOW_USERS;

		User userBuffer = new User();
		Verticale verticaleBuffer = new Verticale();

		userBuffer = userCrud.findOne(idUser);
		verticaleBuffer = verticaleCrud.findOne(idVertical);

		userBuffer.setVerticale(verticaleBuffer);
		userCrud.save(userBuffer);


		return redirect;
	}

	/**
	 * SET A DEFAULT VERTICAL TO A USER IF USER QUITS A VERTICAL
	 * @param idUser
	 * @param idVerticale
	 * @return
	 */
	public String defaultVerticalForUser(Long idUser, Long idVerticale){

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + PathFinder.BASE_VERTICALE + PathFinder.PATH + idVerticale + PathFinder.PATH + PathFinder.SHOW_USERS;

		User userBuffer = userCrud.findOne(idUser);
		Verticale verticaleBuffer = verticaleCrud.findOne(DEFAULT_ID_VERTICAL);

		userBuffer.setVerticale(verticaleBuffer);

		userCrud.save(userBuffer);

		return redirect;
	}


/////////////////// RELATION FUNCTIONS BETWEEN VERTICALE & TEAMS /////////////////////////////////


	/**
	 * SHOW TEAMS RELATED TO ONE VERTICALE
	 * @param model
	 * @param verticaleId
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}" + PathFinder.PATH + PathFinder.SHOW_TEAM, method= RequestMethod.GET)
	public String getTeamsForVerticale(Model model, @PathVariable Long verticaleId) {

		Verticale verticale = super.getItem(verticaleId);
		Set<Team> team =  verticale.getTeams();
		List<Team> listOfTeam = new ArrayList<Team>(team);

		model.addAttribute("idVerticale", verticaleId);
		model.addAttribute("page", verticale.getName());
		model.addAttribute("type","team");
		model.addAttribute("sortedFields", Team.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfTeam));
		model.addAttribute("add", PathFinder.DOT + PathFinder.PATH + PathFinder.ADD_TEAM);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);
		return PathFinder.BASE_VERTICALE + PathFinder.PATH + PathFinder.SHOW_TEAM;
	}

	/**
	 * SHOW TEAMS TO ADD ON VERTICALE (SEARCH)
	 * @param model
	 * @param name
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{verticaleId}"+ PathFinder.PATH + PathFinder.SHOW_TEAM, params = "name", method = RequestMethod.POST)
	public String addVerticalForTeamPOST(Model model,@RequestParam String name , @PathVariable Long verticaleId){

		Verticale verticalBuffer = verticaleCrud.findOne(verticaleId);
		Set<Team> team =  verticalBuffer.getTeams();
		List<Team> listOfTeam = new ArrayList<Team>(team);

		model.addAttribute("model", "team");
		model.addAttribute("idVerticale", verticaleId);
		model.addAttribute("page", verticalBuffer.getName());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);
		model.addAttribute("add", PathFinder.DOT + PathFinder.PATH + PathFinder.ADD_USER);

		if (name != "") {
			model.addAttribute("items",DumpFields.listFielder(this.searchAssociatedTeams(verticaleId, name)));
		}
		else {

			if (listOfTeam.size() < LENGHT_VIEW) {
				model.addAttribute("items",DumpFields.listFielder(listOfTeam));
			}else{
				model.addAttribute("items",DumpFields.listFielder(listOfTeam.subList(0, LENGHT_VIEW)));
			}

		}

		return PathFinder.BASE_VERTICALE + PathFinder.PATH + PathFinder.SHOW_TEAM;

	}

	/**
	 * SEARCH ALL TEAMS ASSOCIATED WITH THE SAME VERTICALE
	 * @param idVerticale
	 * @param name
	 * @return
	 */
	public ArrayList<Team> searchAssociatedTeams(Long idVerticale, String name){

		ArrayList<Team> teamList = new ArrayList<Team>();
		ArrayList<Team> teamListClean = new ArrayList<Team>();

		teamList = teamCrud.getAssociatedTeams(idVerticale);

		for (Team team : teamList) {
			if (team.getName().toLowerCase().equals(name)) {
				teamListClean.add(team);
			}
		}

		return teamListClean;
	}

	/**
	 * DELETE TEAMS AND USERS RELATED TO ONE VERTICALE
	 * @param model
	 * @param id
	 * @return
	 */
	@Override
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(path = PathFinder.ROUTE_DELETE, method = RequestMethod.POST)
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
	@RequestMapping(path = "{idVerticale}" + PathFinder.PATH + PathFinder.ADD_TEAM, method = RequestMethod.GET)

	public String addVerticalForTeamGET(Model model, @PathVariable Long idVerticale) {

	ArrayList<Team> teamList = new ArrayList<Team>();

	Object verticaleBuffer = new Object();
	verticaleBuffer = verticaleCrud.findOne(idVerticale);
	model.addAttribute("items", DumpFields.listFielder(teamList));
	model.addAttribute("sortedFields",Team.FIELDS);
	model.addAttribute("page", ((Verticale) verticaleBuffer).getName());
	model.addAttribute("go_show", PathFinder.SHOW_ACTION);
	model.addAttribute("go_create", PathFinder.CREATE_ACTION);
	model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
	model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_TEAM);
	model.addAttribute("add", PathFinder.ADD_TEAM);

	return PathFinder.BASE_VERTICALE + PathFinder.PATH + PathFinder.ADD_TEAM;
	}

	/**
	 *
	 * SHOW TEAMS TO ADD ON VERTICALE (SEARCH)
	 * @param model
	 * @param name
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idVerticale}" + PathFinder.PATH + PathFinder.ADD_TEAM, params = "name", method = RequestMethod.POST)
	public String addVerticalForTeamPOST(Model model,@RequestParam String name){

		model.addAttribute("model", "user");
		model.addAttribute("page",this.baseName + " " + PathFinder.LIST_ACTION.toUpperCase());
		model.addAttribute("sortedFields",Team.FIELDS);
		model.addAttribute("items",DumpFields.listFielder(this.searchTeam(name)));
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_TEAM);

		return PathFinder.BASE_VERTICALE + PathFinder.PATH + PathFinder.ADD_TEAM;

	}

	/**
	 * SEARCH TEAM BY HIS NAME
	 * @param name
	 * @return
	 */
	public ArrayList<Team> searchTeam(String name){

		ArrayList<Team> teamList = new ArrayList<Team>();
		teamList = teamCrud.getTeams(name);

		return teamList;

	}

	/**
	 * DELETE TEAM FROM A VERTICALE
	 * @param model
	 * @param verticaleId
	 * @param idTeam
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}"+ PathFinder.PATH + PathFinder.SHOW_TEAM, params = "idTeam", method = RequestMethod.POST)
	public String getTeamsForVerticalePOST(Model model, @PathVariable Long verticaleId, @RequestParam Long idTeam) {
		return defaultVerticalForTeam(idTeam, verticaleId);
	}

	/**
	 * SET DEFAULT VERTICALE WHEN A TEAM LEAVE A VERTICAL
	 * @param idTeam
	 * @param idVerticale
	 * @return
	 */
	public String defaultVerticalForTeam(Long idTeam, Long idVerticale){

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + PathFinder.BASE_VERTICALE + PathFinder.PATH + idVerticale + PathFinder.PATH + PathFinder.SHOW_TEAM;

		Team teamBuffer = teamCrud.findOne(idTeam);
		Verticale verticaleBuffer = verticaleCrud.findOne(DEFAULT_ID_VERTICAL);

		teamBuffer.setVerticale(verticaleBuffer);

		teamCrud.save(teamBuffer);

		return redirect;

	}

	/**
	 * ADD ONE TEAM TO VERTICALE
	 * @param model
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = "{idVertical}" + PathFinder.PATH + PathFinder.ADD_TEAM ,method = RequestMethod.POST)
	public String addVerticalForTeamPOST(Model model, Long idTeam, @PathVariable Long idVertical) {
		return setTeamforVertical(idTeam, idVertical);
	}

	/**
	 * FUNCTION USED TO ADD ONE TEAM TO VERTICALE
	 * @param idTeam
	 * @param idVertical
	 * @return
	 */
	private String setTeamforVertical(Long idTeam, Long idVertical) {

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + PathFinder.BASE_VERTICALE + PathFinder.PATH + idVertical + PathFinder.PATH + PathFinder.SHOW_TEAM;

		Team teamBuffer = new Team();
		Verticale verticaleBuffer = new Verticale();

		teamBuffer = teamCrud.findOne(idTeam);
		verticaleBuffer = verticaleCrud.findOne(idVertical);

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

	/**
	 * FIND ALL NIKONIKO RELATED TO A TEAM
	 * @param idTeam
	 * @return
	 */
	public ArrayList<NikoNiko> findNikoNikosOfATeam(Long idTeam){

		ArrayList<User> usersOfTeam = findUsersOfATeam(idTeam);

		ArrayList<NikoNiko> nikonikos = new ArrayList<NikoNiko>();

		if (!usersOfTeam.isEmpty()) {
			for (User user : usersOfTeam) {
				if (!user.getNikoNikos().isEmpty()) {
					nikonikos.addAll(user.getNikoNikos());
				}
			}
		}

		return nikonikos;
	}

	/**
	 * FIND ALL USER RELATED TO A TEAM
	 * @param idValue
	 * @return
	 */
	public ArrayList<User> findUsersOfATeam(Long idValue) {

		List<Long> ids = new ArrayList<Long>();
		ArrayList<User> userList = new ArrayList<User>();
		List<BigInteger> idsBig = userTeamCrud.findAssociatedUser(idValue);

		if(!idsBig.isEmpty()){
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());
			}
			userList = (ArrayList<User>) userCrud.findAll(ids);
		}
		return userList;
	}


	/////////////////// CONSTRUCTORS /////////////////////////////////


	public VerticaleController() {
		super(Verticale.class,BASE_URL);
	}

}
