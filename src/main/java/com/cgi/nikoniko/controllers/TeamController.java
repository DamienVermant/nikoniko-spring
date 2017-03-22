package com.cgi.nikoniko.controllers;

import java.util.ArrayList;

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

@Controller
@RequestMapping(TeamController.BASE_URL)
public class TeamController extends ViewBaseController<Team> {
	
	public final static String BASE_URL = "/team";

	public TeamController() {
		super(Team.class, BASE_URL);
		// TODO Auto-generated constructor stub
	}
	
	// TEST SHOW RELATIONS (TEAM HAS USER)
	
		@RequestMapping(path = "{id}/" + "/showlink", method = RequestMethod.GET)
		public String showLinksGet(Model model, @PathVariable Long id, Team item) { 
			//model.addAttribute("usersFirstname", this.getUsersFirstname(this.setUsersForTeamGet(id)));
			//model.addAttribute("usersLastname", this.getUsersLastname(this.setUsersForTeamGet(id)));
			model.addAttribute("fullName", this.getFullName(this.getUsersFirstname(this.setUsersForTeamGet(id)), this.getUsersLastname(this.setUsersForTeamGet(id))));
			model.addAttribute("fields", User.FIELDS);
			//model.addAttribute("namePage", ((Team) item).getName()); // DOES NOT WORK
			return "base" + "/showlink";
		}
		
		@Autowired
		IUserHasTeamCrudRepository userTeamCrud;
		
		@Autowired
		IUserCrudRepository userCrud;
		
		@Autowired
		ITeamCrudRepository teamCrud;

		// FUNCTION RETURNING A LIST OF USER WITH THE ID OF TEAM
		public ArrayList<User> setUsersForTeamGet(Long teamId) {
		
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
			
			return userList;
		}
		
		// GET USER FIRSTNAME
		public ArrayList<String> getUsersFirstname(ArrayList<User> userlist){
			
			ArrayList<String> userFirstname = new ArrayList<String>();
			
			for (int i = 0; i < userlist.size(); i++) {
				userFirstname.add(userlist.get(i).getFirstname());
			}
			
			return userFirstname;
		}
		
		// GET USER LASTNAME
		public ArrayList<String> getUsersLastname(ArrayList<User> userlist){
			
			ArrayList<String> userLastname = new ArrayList<String>();
			
			for (int i = 0; i < userlist.size(); i++) {
				userLastname.add(userlist.get(i).getLastname());
			}
			
			return userLastname;
		}
		
		// GET FULL NAME (USERNAME + LASTNAME)
		public ArrayList<String> getFullName(ArrayList<String> firstname, ArrayList<String> lastname){
			ArrayList<String> fullName = new ArrayList<String>();
			for (int i = 0; i < firstname.size(); i++) {
				fullName.add(firstname.get(i) + " " + lastname.get(i));
				}
			return fullName;
		}
	
		// CREATE A FUNCTION THAT SET NEW USER IN TEAM
		public Team setUsersForTeam(Team team,User user){
			
			// On créer un object vide qui va contenir le nouvel user
			Object userBuffer = new Object();
			
			// On définit l'id de User
			Long idUser = user.getId();
			
			// On va récupérer l'user correspondant à l'id définit
			userBuffer = userCrud.findOne(idUser);
			
			// On va setter cet User dans la liste de users de team
			team.getUsers().add(user);
			
			// On sauvegarde dans la base données
			teamCrud.save(team);
			
			return team;
			
		}
}
