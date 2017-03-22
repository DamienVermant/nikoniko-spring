package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.BaseController;
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

	private String listIdView;
	private String baseView;

	private String baseName;

	protected UserController(Class<User> clazz, String baseURL) {
		super(clazz, baseURL);

		this.baseName = DumpFields.createContentsEmpty(super.getClazz()).table.toUpperCase();
		this.baseView = "base";
		this.listIdView = this.baseView + PATH_LIST_ID_FILE;
	}




	@RequestMapping("{userId}/nikonikolink")
	public String getNikoNikosForUser(Model model, @PathVariable Long userId) {
		User user = super.getItem(userId);

		model.addAttribute("page", user.getFirstname() + " nikonikos");
		model.addAttribute("fields", NikoNiko.FIELDS);
		model.addAttribute("currentItem", DumpFields.fielder(user));
		ArrayList<NikoNiko> nikos = user.getNikoNikos();
		model.addAttribute("items", DumpFields.listFielder(user.getNikoNikos()));
		return "nikonikolink";
	}



}
