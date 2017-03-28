package com.cgi.nikoniko.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.User;
import com.cgi.nikoniko.dao.IFunctionCGICrudRepository;
import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.IRoleHasFunctionCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.models.tables.FunctionCGI;
import com.cgi.nikoniko.models.association.RoleHasFunction;
import com.cgi.nikoniko.models.association.UserHasRole;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(RoleCGIController.BASE_URL)
public class RoleCGIController extends ViewBaseController<RoleCGI> {

	public RoleCGIController() {
		super(RoleCGI.class,BASE_URL);
	}

	public final static String DOT = ".";
	public final static String PATH = "/";
	public final static String BASE_ROLE = "role";
	public final static String BASE_URL = PATH + BASE_ROLE;

	public static final String SHOW_PATH = "show";

	public final static String SHOW_FUNC = "showFunction";
	public final static String ADD_FUNC = "addFunctions";
	private static final String SHOW_USERS = "showUser";
	public final static String ADD_USER = "addUsers";

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

	@RequestMapping(path = ROUTE_SHOW, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long id) {

		RoleCGI roleBuffer = new RoleCGI();
		roleBuffer= roleCrud.findOne(id);

		model.addAttribute("page","ROLE : " + roleBuffer.getName());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("show_users", DOT + PATH + SHOW_USERS);
		model.addAttribute("show_functions", DOT + PATH + SHOW_FUNC);
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);

		return BASE_ROLE + PATH + SHOW_PATH;
	}

	/**
	 * SHOW FUNCTIONS FOR SELECT ROLE
	 * @param model
	 * @param idRole
	 * @return
	 */
	@RequestMapping(path = "{idRole}" + PATH + SHOW_FUNC, method = RequestMethod.GET)
	public <T> String showLinksGet(Model model, @PathVariable Long idRole) {

		RoleCGI roleBuffer = new RoleCGI();
		roleBuffer= roleCrud.findOne(idRole);

		model.addAttribute("items", DumpFields.listFielder(this.setFunctionsForRoleGet(idRole)));
		model.addAttribute("sortedFields",RoleCGI.FIELDS);
		model.addAttribute("page", ((RoleCGI) roleBuffer).getName());
		model.addAttribute("show_users", DOT + PATH + SHOW_FUNC);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", "./show");
		model.addAttribute("add", "addFunctions");

		return BASE_ROLE + PATH + SHOW_FUNC;
	}

	/**
	 * SHOW USERS FOR SELECT ROLE
	 * @param model
	 * @param idRole
	 * @return
	 */
	@RequestMapping(path = "{idRole}" + PATH + SHOW_USERS, method = RequestMethod.GET)
	public <T> String showLinksGetUser(Model model, @PathVariable Long idRole) {

		RoleCGI roleBuffer = new RoleCGI();
		roleBuffer= roleCrud.findOne(idRole);

		model.addAttribute("items", DumpFields.listFielder(this.setUsersForRoleGet(idRole)));
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("page", ((RoleCGI) roleBuffer).getName());
		model.addAttribute("show_users", DOT + PATH + SHOW_USERS);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", "./show");
		model.addAttribute("add", "addUsers");

		return BASE_ROLE + PATH + SHOW_USERS;
	}


	/**
	 * DELETE FUNCTIONS FROM A ROLE
	 * @param model
	 * @param idRole
	 * @param idFunction
	 * @return
	 */
	@RequestMapping(path = "{idRole}" + PATH + SHOW_FUNC, method = RequestMethod.POST)
	public String showItemDeleteFunction(Model model,@PathVariable Long idRole, Long idFunction) {

		String redirect = REDIRECT + PATH + BASE_ROLE + PATH + idRole + PATH + SHOW_FUNC;
		RoleHasFunction roleHasFunction = new RoleHasFunction(roleCrud.findOne(idRole), funcCrud.findOne(idFunction));
		roleFuncCrud.delete(roleHasFunction);
		return redirect;
	}
	
	/**
	 * DELETE USERS FROM A ROLE
	 * @param model
	 * @param idRole
	 * @param idFunction
	 * @return
	 */
	@RequestMapping(path = "{idRole}" + PATH + SHOW_USERS, method = RequestMethod.POST)
	public String showItemDeleteUrt(Model model,@PathVariable Long idRole, Long idUser) {

		String redirect = REDIRECT + PATH + BASE_ROLE + PATH + idRole + PATH + SHOW_USERS;
		UserHasRole userHasRole = new UserHasRole(userCrud.findOne(idUser), roleCrud.findOne(idRole));
		userRoleCrud.delete(userHasRole);
		return redirect;
	}


	/**
	 * ADD FUNCTIONS TO A ROLE
	 * @param model
	 * @param idRole
	 * @return
	 */
	@RequestMapping(path = "{idRole}" + PATH + ADD_FUNC, method = RequestMethod.GET)
	public <T> String addFunctionsGet(Model model, @PathVariable Long idRole) {

		Object roleBuffer = new Object();
		roleBuffer = roleCrud.findOne(idRole);

		model.addAttribute("items", DumpFields.listFielder((ArrayList<FunctionCGI>) funcCrud.findAll()));
		model.addAttribute("sortedFields",FunctionCGI.FIELDS);
		model.addAttribute("page", ((RoleCGI) roleBuffer).getName());
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", "./showFunction");
		model.addAttribute("add", ADD_FUNC);

		return BASE_ROLE + PATH + ADD_FUNC;
	}

	/**
	 * CREATE RELATION BETWEEN ROLE AND FUNCTION
	 * @param model
	 * @param idRole
	 * @param idFunction
	 * @return
	 */
	@RequestMapping(path = "{idRole}" + PATH + ADD_FUNC, method = RequestMethod.POST)
	public String showItemPostRole(Model model,@PathVariable Long idRole, Long idFunction) {

		String redirect = REDIRECT + PATH + BASE_ROLE + PATH + idRole + PATH + SHOW_FUNC;
		RoleHasFunction roleHasFunction = new RoleHasFunction(roleCrud.findOne(idRole), funcCrud.findOne(idFunction));
		roleFuncCrud.save(roleHasFunction);

		return redirect;
	}
	
	/**
	 * ADD USERS TO A ROLE
	 * @param model
	 * @param idRole
	 * @return
	 */
	@RequestMapping(path = "{idRole}" + PATH + ADD_USER, method = RequestMethod.GET)
	public <T> String addUsersGet(Model model, @PathVariable Long idRole) {

		Object roleBuffer = new Object();
		roleBuffer = roleCrud.findOne(idRole);

		model.addAttribute("items", DumpFields.listFielder((ArrayList<User>) userCrud.findAll()));
		model.addAttribute("sortedFields",User.FIELDS);
		model.addAttribute("page", ((RoleCGI) roleBuffer).getName());
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("back", "./showUser");
		model.addAttribute("add", ADD_USER);

		return BASE_ROLE + PATH + ADD_USER;
	}

	/**
	 * CREATE RELATION BETWEEN ROLE AND FUNCTION
	 * @param model
	 * @param idRole
	 * @param idFunction
	 * @return
	 */
	@RequestMapping(path = "{idRole}" + PATH + ADD_USER, method = RequestMethod.POST)
	public String relationUserRole(Model model,@PathVariable Long idRole, Long idUser) {

		String redirect = REDIRECT + PATH + BASE_ROLE + PATH + idRole + PATH + SHOW_USERS;
		
		UserHasRole userHasRole = new UserHasRole(userCrud.findOne(idUser), roleCrud.findOne(idRole));
		userRoleCrud.save(userHasRole);
		
		//RoleHasFunction roleHasFunction = new RoleHasFunction(roleCrud.findOne(idRole), funcCrud.findOne(idFunction));
		//roleFuncCrud.save(roleHasFunction);

		return redirect;
	}

	/**
	 * FUNCTION THAT CATCH ALL FUNCTIONS RELATED TO ONE ROLE
	 * @param idRole
	 * @return
	 */
	public ArrayList<FunctionCGI> setFunctionsForRoleGet(Long idRole) {

		List<Long> ids = new ArrayList<Long>();
		ArrayList<FunctionCGI> functionList = new ArrayList<FunctionCGI>();

		List<BigInteger> idsBig = roleFuncCrud.findAssociatedFunction(idRole);

		if (!idsBig.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());

			}
			functionList = (ArrayList<FunctionCGI>) funcCrud.findAll(ids);
		}

		return functionList;
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
