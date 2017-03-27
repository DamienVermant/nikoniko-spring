package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
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
	public final static String BASE_URL = "/verticale";

	public final static String BASE_VERTICALE = "verticale";
	public final static String SHOW_USER = "showUser";
	public final static String SHOW_TEAM = "showTeam";

	public static final String SHOW_PATH = "show";


	@Autowired
	IVerticaleCrudRepository verticaleCrud;

	public VerticaleController() {
		super(Verticale.class,BASE_URL);
	}

	/**
	 * SHOW ALL VERTICALE WITH A GIVEN ID
	 */
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

	@RequestMapping("{verticaleId}/showUser")
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
}
