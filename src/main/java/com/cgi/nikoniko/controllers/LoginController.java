package com.cgi.nikoniko.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.User;

@Controller
@RequestMapping(LoginController.BASE_URL)
public class LoginController {

	@Autowired
	private IBaseCrudRepository<User> baseCrud;

	public final static String BASE_URL = "/login";

	public final static String PATH = "/";

		@RequestMapping(path = PATH, method = RequestMethod.GET)
		public String loginGet() {
			return "base" + BASE_URL;
		}

		@RequestMapping(path = PATH, method = RequestMethod.POST)
		public String loginPost(String login, String password) {
			return authentification(login, password);
		}

		// Fonction permettant de s'authentifier en tant que user (retourne un PATH vers le show de user)
		// A modifier
		public String authentification(String login, String password){

			// Création d'un chemin de redirection
			String road = "";

			// Création d'un nouvel utilisateur
			User user = new User();

			// Récupération de tous les utilisateurs
			ArrayList<User> items = null;
			items = (ArrayList<User>) baseCrud.findAll();

			// Condition pour avoir le bon mot de passe de password
			for (User object : items) {
				if (((User) object).getLogin().equals(login) && ((User) object).getPassword().equals(password) ) {
					user = (User) object;
					user.getId();
					// Création de PATH pour la redirection vers la page USER
					road = "redirect:" + "/user" + "/" + user.getId() + "/"+ "show";
				}
				else {
					System.err.println("Mauvais mot de passe ou login");
				}
			}
			// On retourne le chemin de redirection
			return road;
		}

}
