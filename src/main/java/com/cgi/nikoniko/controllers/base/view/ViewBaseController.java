package com.cgi.nikoniko.controllers.base.view;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.TeamController;
import com.cgi.nikoniko.controllers.base.BaseController;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasTeamCrudRepository;
import com.cgi.nikoniko.models.Team;
import com.cgi.nikoniko.models.User;
import com.cgi.nikoniko.models.UserHasTeam;
import com.cgi.nikoniko.models.modelbase.DatabaseItem;
import com.cgi.nikoniko.utils.DumpFields;
import com.mysql.fabric.xmlrpc.base.Array;


public abstract class ViewBaseController<T extends DatabaseItem> extends BaseController<T> {

	private String baseName;

	private String listView;
	private String listRedirect;
	private String baseView;

	private String updateView;
	private String updateRedirect;

	private String deleteView;
	private String deleteRedirect;

	private String createView;
	private String createRedirect;

	private String showView;
	private String showRedirect;

	private String loginView;
	private String loginRedirect;


	public ViewBaseController (Class<T> clazz, String baseURL) {
		super(clazz);

		this.baseName = DumpFields.createContentsEmpty(super.getClazz()).table.toUpperCase();
		this.baseView = "base";
		this.listView = this.baseView + PATH_LIST_FILE;
		this.updateView = this.baseView + PATH_UPDATE_FILE;
		this.deleteView = this.baseView + PATH_DELETE_FILE;
		this.createView = this.baseView + PATH_CREATE_FILE;
		this.showView = this.baseView + PATH_SHOW_FILE;
		this.loginView = this.baseView + PATH_LOGIN;


		this.listRedirect = REDIRECT + baseURL + PATH_LIST_FILE;
		this.updateRedirect = REDIRECT + baseURL + PATH_LIST_FILE;
		this.deleteRedirect = REDIRECT + baseURL + PATH_LIST_FILE;
		this.createRedirect = REDIRECT + baseURL + PATH_LIST_FILE;
		this.showRedirect = REDIRECT + baseURL + PATH_LIST_FILE;
		this.loginRedirect = REDIRECT + baseURL + PATH_LOGIN;

	}

	@RequestMapping(path = {PATH, ROUTE_LIST}, method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("page",this.baseName + " " + LIST_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("items",DumpFields.listFielder(super.getItems()));
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		return listView;
	}

	@RequestMapping(path = ROUTE_CREATE, method = RequestMethod.GET)
	public String createItemGet(Model model) {
		model.addAttribute("page",this.baseName + " " + CREATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.createContentsEmpty(super.getClazz()));
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("create_item", CREATE_ACTION);
		return createView;
	}

	@RequestMapping(path = ROUTE_CREATE, method = RequestMethod.POST)
	public String createItemPost(Model model, T item) {
		try {
			insertItem(item);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return createRedirect;
	}

	@RequestMapping(path = ROUTE_DELETE, method = RequestMethod.GET)
	public String deleteItemGet(Model model,@PathVariable Long id) {
		model.addAttribute("page",this.baseName + " " + DELETE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		return deleteView;
	}

	@RequestMapping(path = ROUTE_DELETE, method = RequestMethod.POST)
	public String deleteItemPost(Model model,@PathVariable Long id) {
		super.deleteItem(id);
		return deleteRedirect;
	}

	@RequestMapping(path = ROUTE_UPDATE, method = RequestMethod.GET)
	public String updateItemGet(Model model,@PathVariable Long id) {

		model.addAttribute("page",this.baseName + " " + UPDATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("items",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("update_item", UPDATE_ACTION);
		return updateView;
	}

	@RequestMapping(path = ROUTE_UPDATE, method = RequestMethod.POST)
	public String updateItemPost(Model model,T item) {
		updateItem(item);
		return updateRedirect;
	}

	@RequestMapping(path = ROUTE_SHOW, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long id) {
		model.addAttribute("page",this.baseName + " " + SHOW_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);
		model.addAttribute("show_users", "./showUsers");
		return "team/show"; // changer en show view (on est toujours ici en generic, il faudra implanter cette fonction dans TeamController)
	}

	// ADD REDIRECTION LOGIN
	@RequestMapping(path = ROUTE_LOGIN, method = RequestMethod.GET)
	public String loginGet(Model model) {
		return loginView;
	}

	@RequestMapping(path = ROUTE_LOGIN, method = RequestMethod.POST)
	public String loginPost(String login, String password) {
		return authentification(login, password);
	}


}
