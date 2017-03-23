package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.NikoNiko;
import com.cgi.nikoniko.models.Team;
import com.cgi.nikoniko.models.User;
import com.cgi.nikoniko.models.Verticale;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(VerticaleController.BASE_URL)
public class VerticaleController  extends ViewBaseController<Verticale> {

	public final static String BASE_URL = "/verticale";

	public VerticaleController() {
		super(Verticale.class,BASE_URL);
	}

	@RequestMapping("{verticaleId}/link/user")
	public String getUsersForVerticale(Model model, @PathVariable Long verticaleId) {
		Verticale verticale = super.getItem(verticaleId);
		Set<User> user =  verticale.getUsers();
		List<User> listOfUser = new ArrayList<User>(user);

		model.addAttribute("page", verticale.getAgency());
		model.addAttribute("type","user");
		model.addAttribute("sortedFields", User.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfUser));
		return "verticale/showAllRelation";
	}

	@RequestMapping("{verticaleId}/link/team")
	public String getTeamsForVerticale(Model model, @PathVariable Long verticaleId) {
		Verticale verticale = super.getItem(verticaleId);
		Set<Team> team =  verticale.getTeams();
		List<Team> listOfTeam = new ArrayList<Team>(team);

		model.addAttribute("page", verticale.getAgency());
		model.addAttribute("type","team");
		model.addAttribute("sortedFields", Team.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfTeam));
		return "verticale/showAllRelation";
	}
}
