package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.User;
import com.cgi.nikoniko.models.tables.security.SecurityUser;
import com.cgi.nikoniko.service.security.UserDetailsServiceImp;

@Controller
public class MenuController  {
	
	@Autowired
	IUserCrudRepository userCrud;
	
	@Autowired
	IUserHasRoleCrudRepository userRoleCrud;
	
	@Autowired
	IRoleCrudRepository roleCrud;
	
	public final static String DOT = ".";
	public final static String PATH = "/";
	public final static String BASE_MENU = "menu";
	public final static String BASE_URL = PATH + BASE_MENU;
	
	public final static String REDIRECT = "redirect:";
	
	public final static String GO_USERS = PATH + "user" + PATH;
	public final static String GO_TEAMS = PATH + "team" + PATH;
	public final static String GO_VERTICALE = PATH + "verticale" + PATH;
	public final static String GO_ROLES = PATH + "role" + PATH;
	public final static String GO_NIKOS =  PATH + "nikoniko" + PATH;
	public final static String GO_FUNCTIONS =  PATH + "function" + PATH;
	
	public final static String GO_USERTEAM = PATH + "user_has_team" + PATH;
	public final static String GO_USERROLE = PATH + "user_has_role" + PATH;
	public final static String GO_ROLEFUNC = PATH + "role_has_function" + PATH;
	
	
	// TODO : Récupération du rôle en fonction de l'utilisateur connecté
	// TODO : Redirection vers le bon menu en fonction du rôle et de l'id de user
	// TODO : Trier les rôles en fonction de leur importance pour faciliter l'affichage du menu
	
	@RequestMapping(path = "/menu", method = RequestMethod.GET)
	public String index(Model model, String login) {
		
		model.addAttribute("page","MENU");
		
		model.addAttribute("roles",this.getConnectUserRole());
		model.addAttribute("auth",this.getUserInformations().getFirstname());
		model.addAttribute("go_own_nikoniko", PATH + "user" + PATH + this.getUserInformations().getId() + PATH + "link");
		model.addAttribute("add_nikoniko", PATH + "user" + PATH + this.getUserInformations().getId() + PATH + "add");
		
		model.addAttribute("go_users", GO_USERS);
		model.addAttribute("go_nikonikos", GO_NIKOS);
		model.addAttribute("go_teams", GO_TEAMS);
		model.addAttribute("go_roles", GO_ROLES);
		model.addAttribute("go_functions", GO_FUNCTIONS);
		model.addAttribute("go_verticales", GO_VERTICALE);
		
		model.addAttribute("go_user_has_team", GO_USERS);
		model.addAttribute("go_user_has_role", GO_NIKOS);
		model.addAttribute("go_role_has_function", GO_TEAMS);
		
		
		return BASE_MENU + PATH + "mainMenu";
	}
	
	/**
	 * RETURN THE HIGHEST ROLE FROM A USER
	 * @return
	 */
	public String getConnectUserRole(){
		
		String login = "";
		String role = "";
		User user = new User();
		ArrayList<RoleCGI> roleList = new ArrayList<RoleCGI>();
		ArrayList<String> roleNames = new ArrayList<String>();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		login = auth.getName();
		
		user = userCrud.findByLogin(login);
		
		roleList = this.setRolesForUserGet(user.getId());
		roleNames = this.convertObjectToString(roleList);
		
		if (roleNames.contains("Admin")) {
			role = "admin";
		}
		else if (roleNames.contains("VP")) {
			role = "vp";
		}
		else if (roleNames.contains("Chef de projet")) {
			role = "chefProjet";
		}
		else if (roleNames.contains("Gestionnaire equipe")) {
			role = "gestionTeam";
		}
		else {
			role = "employee";
		}
		
		return role;
	}
	
	/**
	 * RETURN USER FROM AUTHENTIFICATION
	 * @return
	 */
	public User getUserInformations(){
		
		String login = "";
		User user = new User();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		login = auth.getName();
		user = userCrud.findByLogin(login);
		
		return user;
	}
	
	/**
	 * TODO : this function can be generic or we can put it in a parent class for other implement
	 * HAVE ALL ROLES ASSOCIATED TO A USER
	 * @param idUser
	 * @return
	 */
	public ArrayList<RoleCGI> setRolesForUserGet(Long idUser) {

		List<Long> ids = new ArrayList<Long>();
		ArrayList<RoleCGI> roleList = new ArrayList<RoleCGI>();

		List<BigInteger> idsBig = userRoleCrud.findAssociatedRole(idUser);

		if (!idsBig.isEmpty()) {
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());

			}
			roleList = (ArrayList<RoleCGI>) roleCrud.findAll(ids);
		}

		return roleList;
	}
	
	/**
	 * GET ROLE NAME FROM ROLE OBJECT
	 * @param roleList
	 * @return
	 */
	public ArrayList<String> convertObjectToString(ArrayList<RoleCGI> roleList){
		
		ArrayList<String> roleNames = new ArrayList<String>();
		for (int i = 0; i <roleList.size(); i++) {
			roleNames.add(roleList.get(i).getName());
		}
		return roleNames;
		
	}

}