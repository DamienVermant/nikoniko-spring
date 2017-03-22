package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

	@RequestMapping("{userId}/link")
	public String getNikoNikosForUser(Model model, @PathVariable Long userId) {
		User user = super.getItem(userId);
		Set<NikoNiko> niko =  user.getNikoNikos();
		List<NikoNiko> listOfNiko = new ArrayList<NikoNiko>(niko);

		model.addAttribute("page", user.getFirstname() + " nikonikos");
		model.addAttribute("sortedFields", NikoNiko.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfNiko));
		return "base/nikoUser";
	}

}
