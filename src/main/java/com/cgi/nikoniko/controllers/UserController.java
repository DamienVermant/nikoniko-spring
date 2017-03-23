package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Cascade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.models.NikoNiko;
import com.cgi.nikoniko.models.User;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(UserController.BASE_URL)
public class UserController extends ViewBaseController<User> {

	public final static String BASE_URL = "/user";


	@Autowired
	INikoNikoCrudRepository nikonikoCrud;


	public UserController() {
		super(User.class,BASE_URL);
	}


	protected UserController(Class<User> clazz, String baseURL) {
		super(clazz, baseURL);

	}


	/**
	 *
	 * Recupération de tous les nikoniko liés à un user
	 */
	@RequestMapping("{userId}/link")
	public String getNikoNikosForUser(Model model, @PathVariable Long userId) {
		User user = super.getItem(userId);
		Set<NikoNiko> niko =  user.getNikoNikos();
		List<NikoNiko> listOfNiko = new ArrayList<NikoNiko>(niko);

		model.addAttribute("page", user.getFirstname() + " nikonikos");
		model.addAttribute("sortedFields", NikoNiko.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfNiko));
		return "user/showAllRelation";
	}

	/**
	 *
	 * Page de creation d'un nikoniko pour un user
	 */
	@RequestMapping(path = "{userId}/add", method = RequestMethod.GET)
	public String createItemGet(Model model, @PathVariable Long userId) {
		User user = super.getItem(userId);
		NikoNiko niko = new NikoNiko();

		model.addAttribute("page",user.getFirstname() + " " + CREATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",NikoNiko.FIELDS);
		model.addAttribute("item",DumpFields.createContentsEmpty(niko.getClass()));
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("create_item", CREATE_ACTION);
		return "nikoniko/addNikoNiko";
	}

	/**
	 *
	 * Creation d'un nikoniko
	 */
	@RequestMapping(path = "{userId}/create", method = RequestMethod.POST)
	public String createItemPost(Model model, NikoNiko niko, @PathVariable Long userId) {

		try {
			User user = super.getItem(userId);
			niko.setUser(user);
			nikonikoCrud.save(niko);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return "redirect:/user/1/link";
	}

}
