package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
import com.cgi.nikoniko.models.Team;
import com.cgi.nikoniko.models.User;
import com.cgi.nikoniko.models.UserHasTeam;
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

	public TeamController() {
		super(Team.class, BASE_URL);
		// TODO Auto-generated constructor stub
	}
	
		// RELATIONS (TEAM HAS USER)
		@RequestMapping(path = "{id}" + "/showUsers", method = RequestMethod.GET)
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
			return "team" + "/showUsers";
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
			model.addAttribute("back", "./show");
			model.addAttribute("add", "addUsers");
			return "team" + "/addUsers";
			
		}
		
		// ADD USER FOR CURRENT TEAM
		@RequestMapping(path = "{idTeam}" + "/addUsers", method = RequestMethod.POST)
		public <T> String addUsersPost(Model model, @PathVariable Long idTeam, Long idUser) { 
			Long test = (long) 3;
			return setUsersForTeamPost(idTeam, test);
		}
		
		// FUNCTION RETURNING A LIST OF USER WITH THE ID OF TEAM (TODO : CREATE A MAP FOR FREEMARKER, TODO : CREATE A ARRAYLIST OF MAP)
		public ArrayList<User> setUsersForTeamGet(Long teamId) {
			
			// On initialise une nouvelle map
			Map<String, User> mapUsers;
		
			// On va chercher dans la table d'association les éléments correspondant à Team récupéré
			ArrayList<UserHasTeam> userHasTeamList = new ArrayList<UserHasTeam>();
			userHasTeamList = (ArrayList<UserHasTeam>) userTeamCrud.findAll();
			
			// On créer une liste vide qui va stocker les id des users correspondant au bon id team
			ArrayList<Long> ids = new ArrayList<Long>();
			
			// Boucle sur tous les élements de la liste pour récupérer la bonne team
			for (UserHasTeam userHasTeam : userHasTeamList) {
				if (userHasTeam.getIdRight() == teamId) {
					// On récupère les ids user correspondant à l'id de la team
					ids.add(userHasTeam.getIdLeft());
				}
			}
			
			// On créé une arrayList vide qui va contenir des users
			ArrayList<User> userList = new ArrayList<User>();
			
			// On va maintenant chercher les users en fonction des ids récupérés
			userList = (ArrayList<User>) userCrud.findAll(ids);
			
			// On stocke maintenant les fields et les champs de chaque users dans une map
			
			
			return userList;
		}
		
		// CREATE A FUNCTION THAT SET NEW USER IN TEAM (JUST AFFECT A USER ALREADY CREATE)
		public String setUsersForTeamPost(Long idTeam,Long idUser){
			
			// Redirection vers showUsers pour updater le résultat
			String redirect ="redirect:/team/" + idTeam + "/showUsers";
			
			// On récupère l'objet team renseigner par l'id en paramètre
			Team team = new Team();
			team = teamCrud.findOne(idTeam);
			
			// On récupère l'objet user renseigner par l'id en paramètre
			User user = new User();
			user = userCrud.findOne(idUser);

			// On créer l'association entre les deux objects
			UserHasTeam userHasTeamBuffer = new UserHasTeam(user, team);
			
			// On sauvegarde la relation dans la table UserHasTeam
			userTeamCrud.save(userHasTeamBuffer);
			
			return redirect;
			
		}
}
