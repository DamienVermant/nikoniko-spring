package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.ITeamCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.dao.IVerticaleCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.User;

@Controller
@RequestMapping(CommentController.BASE_URL)
public class CommentController extends ViewBaseController<User>{

	public CommentController(Class<User> clazz, String baseURL) {
		super(clazz, baseURL);
	}

	public CommentController() {
		super(User.class,BASE_URL);
	}

	public final static String MENU_PATH = "menu";

	public final static String SHOW_COMMENT = "showComment";
	public final static String SHOW_GRAPH_MONTH = "showGraphMonth";
	public final static String SHOW_GRAPH_WEEK = "showGraphWeek";
	public final static String SHOW_GRAPH_DATE = "showDate";
	public final static String SHOW_GRAPH_ALL = "showGraphAll";
	public final static String SHOW_GRAPH_VERTICALE = "showGraphVerticale";
	public final static String SHOW_GRAPH_TEAM = "showGraphTeam";

	public final static String BASE_COMMENT = "comment";
	public final static String BASE_URL = PATH + BASE_COMMENT;

	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	IUserHasRoleCrudRepository userRoleCrud;

	@Autowired
	IRoleCrudRepository roleCrud;

	@Autowired
	INikoNikoCrudRepository nikonikoCrud;

	@Autowired
	IVerticaleCrudRepository verticaleCrud;

	@Autowired
	ITeamCrudRepository teamCrud;

	@Autowired
	IUserHasTeamCrudRepository userTeamCrud;

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

/////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
*
* GRAPH GESTION
*
*
*/

/////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * ALL NIKONIKO GRAPH FOR AN USER
	 * @param model
	 * @param idUser
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = PATH + SHOW_COMMENT, method = RequestMethod.GET)
	public String showComment(Model model) {

		Long idUser = this.getUserInformations().getId();
		User user = super.getItem(idUser);
		user.getRoles();
		Set<NikoNiko> niko =  user.getNikoNikos();
		List<NikoNiko> listOfNiko = new ArrayList<NikoNiko>(niko);
		List<NikoNiko> nikotoday = getNikoToday(listOfNiko);


		model.addAttribute("title", "Mes commentaires !" );

		model.addAttribute("back", PATH + MENU_PATH);
		return "graphs" + PATH + "pie";
	}

	/**
	 * Get nikoniko's list from today
	 * @param listOfNiko
	 * @return
	 */
	public List<NikoNiko> getNikoToday(List<NikoNiko> listOfNiko){

		LocalDate nikodate = new LocalDate();
		LocalDate date;
		List<NikoNiko> nikotoday = new ArrayList<NikoNiko>();

		for (int i = 0; i < listOfNiko.size(); i++) {
			Date firstniko = listOfNiko.get(i).getEntryDate();
			nikodate = new LocalDate(firstniko);
			date = new LocalDate();
			if (nikodate.isEqual(date)) {
				nikotoday.add(listOfNiko.get(i));
			}
		}

		return nikotoday;
	}
}