package com.cgi.nikoniko.controllers.base.view;

import java.util.ArrayList;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.PathClass.PathFinder;
import com.cgi.nikoniko.controllers.base.BaseController;
import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;
import com.cgi.nikoniko.utils.DumpFields;


public abstract class ViewBaseController<T extends DatabaseItem> extends BaseController<T> {

	protected String baseName;

	protected String listView;
	private String baseView;

	protected String updateView;
	protected String updateRedirect;

	private String deleteView;
	protected String deleteRedirect;

	private String createView;
	private String createRedirect;

	private String showView;

	public final static int LENGHT_VIEW = 10;

	public ViewBaseController (Class<T> clazz, String baseURL) {
		super(clazz);

		this.baseName = DumpFields.createContentsEmpty(super.getClazz()).table.toUpperCase();
		this.baseView = "base";
		this.listView = this.baseView + PathFinder.PATH_LIST_FILE;
		this.updateView = this.baseView + PathFinder.PATH_UPDATE_FILE;
		this.deleteView = this.baseView + PathFinder.PATH_DELETE_FILE;
		this.createView = this.baseView + PathFinder.PATH_CREATE_FILE;
		this.showView = this.baseView + PathFinder.PATH_SHOW_FILE;

		this.updateRedirect = PathFinder.REDIRECT + baseURL + PathFinder.PATH_LIST_FILE;
		this.deleteRedirect = PathFinder.REDIRECT + baseURL + PathFinder.PATH_LIST_FILE;
		this.createRedirect = PathFinder.REDIRECT + baseURL + PathFinder.PATH_LIST_FILE;

	}

	/**
	 *	SHOW THE LIST OF ALL ITEMS
	 * @param model
	 * @return
	 */
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(path = {PathFinder.PATH, PathFinder.ROUTE_LIST}, method = RequestMethod.GET)
	public String index(Model model) {

		ArrayList<T> emptyList = new ArrayList<T>();

		model.addAttribute("model", this.baseName.toLowerCase());
		model.addAttribute("page",this.baseName + " " + PathFinder.LIST_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);

		if (this.baseName.toLowerCase().equals("role")) {
			model.addAttribute("items",super.getItems());
		}
		else {
				if (super.getItems().size() < LENGHT_VIEW) {
					model.addAttribute("items",DumpFields.listFielder(super.getItems()));
				}else{
					model.addAttribute("items",DumpFields.listFielder(super.getItems().subList(0, LENGHT_VIEW)));
				}

			}
		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		return listView;
	}

	/**
	 *
	 * @param model
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = PathFinder.ROUTE_CREATE, method = RequestMethod.GET)
	public String createItemGet(Model model) {
		model.addAttribute("page",this.baseName + " " + PathFinder.CREATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.createContentsEmpty(super.getClazz()));
		model.addAttribute("go_index", PathFinder.LIST_ACTION);
		model.addAttribute("create_item", PathFinder.CREATE_ACTION);
		return createView;
	}

	/**
	 *
	 * @param model
	 * @param item
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
	@RequestMapping(path = PathFinder.ROUTE_CREATE, method = RequestMethod.POST)
	public String createItemPost(Model model, T item) {


		try {
			insertItem(item);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return createRedirect;
	}

	/**
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(path = PathFinder.ROUTE_DELETE, method = RequestMethod.GET)
	public String deleteItemGet(Model model,@PathVariable Long id) {
		model.addAttribute("page",this.baseName + " " + PathFinder.DELETE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("go_index", PathFinder.LIST_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		return deleteView;
	}

	/**
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(path = PathFinder.ROUTE_DELETE, method = RequestMethod.POST)
	public String deleteItemPost(Model model,@PathVariable Long id) {
		super.deleteItem(id);
		return deleteRedirect;
	}

	/**
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_USER"})
	@RequestMapping(path = PathFinder.ROUTE_UPDATE, method = RequestMethod.GET)
	public String updateItemGet(Model model,@PathVariable Long id) {

		model.addAttribute("page",this.baseName + " " + PathFinder.UPDATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("items",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("go_index",PathFinder. LIST_ACTION);
		model.addAttribute("update_item", PathFinder.UPDATE_ACTION);
		return updateView;
	}

	/**
	 *
	 * @param model
	 * @param item
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_USER"})
	@RequestMapping(path = PathFinder.ROUTE_UPDATE, method = RequestMethod.POST)
	public String updateItemPost(Model model,T item) {
		updateItem(item);
		return updateRedirect;
	}

	/**
	 *
	 * RETIRER VP DANS LES DROITS D'ACCES???
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = PathFinder.ROUTE_SHOW, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long id) {
		model.addAttribute("page",this.baseName + " " + PathFinder.SHOW_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(id)));
		model.addAttribute("go_index", PathFinder.LIST_ACTION);
		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
		model.addAttribute("go_update", PathFinder.UPDATE_ACTION);
		return showView;
	}

	/**
	 * FUNCTION USED TO GET CONNECT USER INFORMATION
	 *
	 * @return Session informations of type Authentication
	 */
	public Authentication checkSession(){
		return  SecurityContextHolder.getContext().getAuthentication();
	}

}
