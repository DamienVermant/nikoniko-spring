package com.cgi.nikoniko.controllers;

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
import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.dao.IVerticaleCrudRepository;
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
	 * SHOW ALL USERS TO ADD TO A VERTICALE
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

	/**
	 * ADD USER TO ONE VERTICALE
	 * @param model
	 * @param verticaleId
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}"+ PATH + SHOW_USER, method = RequestMethod.POST)
	public String getUsersForVerticalePOST(Model model, @PathVariable Long verticaleId, Long idUser) {
		return deleteUserFromVertical(idUser, verticaleId);
	}

	/**
	 * SHOW TEAMS RELATED TO ONE VERTICALE
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
	 * DELETE TEAMS AND USERS RELATED TO ONE VERTICALE
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

	/**
	 * FUNCTION USED FOR ADD ONE USER TO VERTICALE
	 * @param idUser
	 * @param idVertical
	 * @return
	 */
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

	/**
	 * NAME ??
	 * @param idUser
	 * @param idVerticale
	 * @return
	 */
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

	/**
	 * DELETE TEAM FROM A VERTICALE
	 * @param model
	 * @param verticaleId
	 * @param idTeam
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{verticaleId}"+ PATH + SHOW_TEAM, method = RequestMethod.POST)
	public String getTeamsForVerticalePOST(Model model, @PathVariable Long verticaleId, Long idTeam) {
		return deleteTeamFromVertical(idTeam, verticaleId);
	}

	/**
	 * ADD ONE TEAM TO VERTICALE
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

	/**
	 * FUNCTION USED FOR ADD ONE TEAM TO VERTICALE
	 * @param idTeam
	 * @param idVertical
	 * @return
	 */
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

	/**
	 * FUNCTION USED TO DELETE TEAM FROM VERTICALE
	 * @param idTeam
	 * @param idVerticale
	 * @return
	 */
	public String deleteTeamFromVertical(Long idTeam, Long idVerticale){

		String redirect = REDIRECT + PATH + BASE_VERTICALE + PATH + idVerticale + PATH + SHOW_TEAM;

		Team teamBuffer = teamCrud.findOne(idTeam);
		Verticale verticaleBuffer = verticaleCrud.findOne(DEFAULT_ID_VERTICAL);

		teamBuffer.setVerticale(verticaleBuffer);

		teamCrud.save(teamBuffer);

		return redirect;

	}

}
