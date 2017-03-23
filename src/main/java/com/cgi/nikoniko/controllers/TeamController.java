package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.models.association.UserHasTeam;
import com.cgi.nikoniko.models.tables.Team;
import com.cgi.nikoniko.models.tables.User;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(TeamController.BASE_URL)
public class TeamController extends ViewBaseController<Team> {
	
	@Autowired
	IUserHasTeamCrudRepository userTeamCrud;
	
	@Autowired
	IUserCrudRepository userCrud;
	
	@Autowired
	ITeamCrudRepository teamCrud;
	
	public final static String BASE_URL = "/team";
	public final static String BASE_TEAM = "team";
	
	public TeamController() {
		super(Team.class, BASE_URL);
	}
	
		@RequestMapping(path = ROUTE_SHOW, method = RequestMethod.GET)
		public String showItemGet(Model model,@PathVariable Long id) {
		model.addAttribute("page","team" + " " + SHOW_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("show_users", "./showUser");
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);
		return BASE_TEAM + "/show";
	}

		// RELATIONS (TEAM HAS USER)
		@RequestMapping(path = "{id}" + "/showUser", method = RequestMethod.GET)
		public <T> String showLinksGet(Model model, @PathVariable Long id) { 
			
			// Récupération de la team en fonction de l'objet souhaitée
			Team teamBuffer = new Team();
			teamBuffer = teamCrud.findOne(id);
			
			// On ajoute au model les champs nécessaires
			model.addAttribute("items", DumpFields.listFielder(this.setUsersForTeamGet(id)));
			model.addAttribute("sortedFields",User.FIELDS);
			model.addAttribute("page", ((Team) teamBuffer).getName()); 
			model.addAttribute("go_show", SHOW_ACTION);
			model.addAttribute("go_create", CREATE_ACTION);
			model.addAttribute("go_delete", DELETE_ACTION);
			model.addAttribute("back", "./show");
			model.addAttribute("add", "addUsers");
			return "team" + "/showUser";
		}
		
		// ADD USER FOR CURRENT TEAM
		@RequestMapping(path = "{idTeam}" + "/addUsers", method = RequestMethod.GET)
		public <T> String addUsersGet(Model model, @PathVariable Long idTeam) { 
			
			// Récupération de la team en fonction de l'objet souhaitée
			Object teamBuffer = new Object();
			teamBuffer = teamCrud.findOne(idTeam);
			model.addAttribute("items", DumpFields.listFielder((ArrayList<User>) userCrud.findAll()));
			model.addAttribute("sortedFields",User.FIELDS);
			model.addAttribute("page", ((Team) teamBuffer).getName()); 
			model.addAttribute("go_show", SHOW_ACTION);
			model.addAttribute("go_create", CREATE_ACTION);
			model.addAttribute("go_delete", DELETE_ACTION);
			model.addAttribute("back", "./showUser");
			model.addAttribute("add", "addUsers");
			return BASE_TEAM + "/addUsers";
			
		}
		
		// ADD USER FOR CURRENT TEAM
		@RequestMapping(path = "{idTeam}" + "/addUsers", method = RequestMethod.POST)
		public <T> String addUsersPost(Model model, @PathVariable Long idTeam, Long idUser) { 
			return setUsersForTeamPost(idTeam, idUser);
		}
		
		/**
		 *
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
		
		// CREATE A FUNCTION THAT SET NEW USER IN TEAM (JUST AFFECT A USER ALREADY CREATE)
		public String setUsersForTeamPost(Long idTeam,Long idUser){
			
			// Redirection vers showUsers pour updater le résultat
			String redirect ="redirect:/team/" + idTeam + "/showUser";
			
			// On récupère l'objet team renseigner par l'id en paramètre
			Team team = new Team();
			team = teamCrud.findOne(idTeam);
			
			// On récupère l'objet user renseigner par l'id en paramètre
			User user = new User();
			user = userCrud.findOne(idUser);

			// On créer l'association entre les deux objects
			UserHasTeam userHasTeamBuffer = new UserHasTeam(user, team, new Date());
			
			// On sauvegarde la relation dans la table UserHasTeam
			userTeamCrud.save(userHasTeamBuffer);
			
			return redirect;
			
		}
}
