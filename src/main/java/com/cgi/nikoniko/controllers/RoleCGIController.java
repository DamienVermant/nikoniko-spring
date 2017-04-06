package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.PathClass.PathFinder;
import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.User;
import com.cgi.nikoniko.dao.IFunctionCGICrudRepository;
import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.IRoleHasFunctionCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.models.association.UserHasRole;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(RoleCGIController.BASE_URL)
public class RoleCGIController extends ViewBaseController<RoleCGI> {

	public final static String BASE_ROLE = "role";
	public final static String BASE_URL = PathFinder.PATH + BASE_ROLE;

	public RoleCGIController() {
		super(RoleCGI.class,BASE_URL);
	}

	@Autowired
	IUserCrudRepository userCrud;

	@Autowired
	IRoleCrudRepository roleCrud;

	@Autowired
	IFunctionCGICrudRepository funcCrud;

	@Autowired
	IRoleHasFunctionCrudRepository roleFuncCrud;

	@Autowired
	IUserHasRoleCrudRepository userRoleCrud;

	//TODO : Retirer le delete et le update!!! (et rediriger vers un autre ftl si besoin)
	/**
	 *
	 * SHOW A SELECT ROLE
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = PathFinder.ROUTE_SHOW, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long id) {

		RoleCGI roleBuffer = new RoleCGI();
		roleBuffer= roleCrud.findOne(id);

		model.addAttribute("page","ROLE : " + roleBuffer.getName());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("show_users", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_USERS);
		model.addAttribute("go_index", PathFinder.LIST_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("go_update", PathFinder.UPDATE_ACTION);

		return BASE_ROLE + PathFinder.PATH + PathFinder.SHOW_PATH;
	}

	/**
	 * SHOW USERS FOR SELECT ROLE
	 * @param model
	 * @param idRole
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = "{idRole}" + PathFinder.PATH + PathFinder.SHOW_USERS, method = RequestMethod.GET)
	public <T> String showLinksGetUser(Model model, @PathVariable Long idRole) {

		RoleCGI roleBuffer = new RoleCGI();
		roleBuffer= roleCrud.findOne(idRole);

		model.addAttribute("items", DumpFields.listFielder(this.setUsersForRoleGet(idRole)));
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("page", ((RoleCGI) roleBuffer).getName());
		model.addAttribute("show_users", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_USERS);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_PATH);
		model.addAttribute("add", PathFinder.ADD_USER);

		return BASE_ROLE + PathFinder.PATH + PathFinder.SHOW_USERS;
	}

	/**
	 * DELETE USERS FROM A ROLE
	 * @param model
	 * @param idRole
	 * @param idFunction
	 * @return
	 */
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(path = "{idRole}" + PathFinder.PATH + PathFinder.SHOW_USERS, method = RequestMethod.POST)
	public String showItemDeleteUrt(Model model,@PathVariable Long idRole, Long idUser) {

		String redirect = PathFinder.REDIRECT + PathFinder.PATH + BASE_ROLE + PathFinder.PATH + idRole + PathFinder.PATH + PathFinder.SHOW_USERS;
		UserHasRole userHasRole = new UserHasRole(userCrud.findOne(idUser), roleCrud.findOne(idRole));
		userRoleCrud.delete(userHasRole);
		return redirect;
	}

	/**
	 * ADD USERS TO A ROLE
	 * @param model
	 * @param idRole
	 * @return
	 */
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(path = "{idRole}" + PathFinder.PATH + PathFinder.ADD_USER, method = RequestMethod.GET)
	public <T> String addUsersGet(Model model, @PathVariable Long idRole) {

		Object roleBuffer = new Object();
		roleBuffer = roleCrud.findOne(idRole);

		model.addAttribute("items", DumpFields.listFielder((ArrayList<User>) userCrud.findAll()));
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("page", ((RoleCGI) roleBuffer).getName());
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("back", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_USERS);
		model.addAttribute("add", PathFinder.ADD_USER);

		return BASE_ROLE + PathFinder.PATH + PathFinder.ADD_USER;
	}

	/**
	 * FUNCTION RETURNING ALL USERS RELATED TO ONE ROLE
	 * @param idRole
	 * @return
	 */
	public ArrayList<User> setUsersForRoleGet(Long idRole) {

		List<Long> ids = new ArrayList<Long>();
		ArrayList<User> userList = new ArrayList<User>();

		List<BigInteger> idsBig = userRoleCrud.findAssociatedUser(idRole);

		if (!idsBig.isEmpty()) {
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());

			}
			userList =  (ArrayList<User>) userCrud.findAll(ids);
		}

		return userList;
	}

}
