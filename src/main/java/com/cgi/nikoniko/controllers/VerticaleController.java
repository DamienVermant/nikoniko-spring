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

	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	ITeamCrudRepository teamCrud;

	@Autowired
	IVerticaleCrudRepository verticaleCrud;

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
	@RequestMapping("{verticaleId}"+ PATH + SHOW_USER)
	public String getUsersForVerticale(Model model, @PathVariable Long verticaleId) {

		Verticale verticale = super.getItem(verticaleId);
		Set<User> user =  verticale.getUsers();
		List<User> listOfUser = new ArrayList<User>(user);

		model.addAttribute("page", verticale.getAgency());
		model.addAttribute("type","user");
		model.addAttribute("sortedFields", User.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfUser));
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		return "verticale/showAllRelation";
	}

	/**
	 *
	 * @param model
	 * @param verticaleId
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping("{verticaleId}/showTeam")
	public String getTeamsForVerticale(Model model, @PathVariable Long verticaleId) {
		Verticale verticale = super.getItem(verticaleId);
		Set<Team> team =  verticale.getTeams();
		List<Team> listOfTeam = new ArrayList<Team>(team);

		model.addAttribute("page", verticale.getAgency());
		model.addAttribute("type","team");
		model.addAttribute("sortedFields", Team.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfTeam));
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		return "verticale/showAllRelation";
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

}
