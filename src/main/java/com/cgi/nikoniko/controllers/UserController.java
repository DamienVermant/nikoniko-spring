package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.List;

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
		model.addAttribute("items", DumpFields.<NikoNiko>listFielder(new ArrayList<NikoNiko>(user.getNikoNikos())));
		return "nikonikolink";
	}

//	@RequestMapping(path="{userId}/nikonikolink", method = RequestMethod.GET)
//	public String setNikoNikoForUserGet(Model model, @PathVariable Long userId) {
//		User user = super.getItem(userId);
//
//		model.addAttribute("page", user.getFirstname() + user.getLastname()
//							+ " teams linker");
//		model.addAttribute("fields", NikoNiko.FIELDS);
//		model.addAttribute("currentItem", DumpFields.fielder(user));
//
//		ArrayList<NikoNiko> nikos = (ArrayList<NikoNiko>) nikonikoCrud.findAll();
//		model.addAttribute("items", DumpFields.<NikoNiko>listFielder(nikos));
//
//		ArrayList<Long> nikoIds = new ArrayList<Long>();
//		for (NikoNiko niko : user.getNikoNikos()) {
//			nikoIds.add(niko.getId());
//		}
//		model.addAttribute("linkedItems", nikoIds);
//
//		return "nikonikolink";
//	}

}
