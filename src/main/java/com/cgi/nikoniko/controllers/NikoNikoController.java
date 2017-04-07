package com.cgi.nikoniko.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.PathClass.PathFinder;
import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.IChangeDatesCrudRepository;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(NikoNikoController.BASE_URL)
public class NikoNikoController extends ViewBaseController<NikoNiko> {

	public final static String BASE_URL = "/nikoniko";

	@Autowired
	INikoNikoCrudRepository nikoCrud;

	@Autowired
	IChangeDatesCrudRepository changeCrud;

	public NikoNikoController() {
		super(NikoNiko.class,BASE_URL);
	}

//	/**UNUSED ??
//	 *
//	 *
//	 * SHOW A SELECT NIKONIKO
//	 */
//	@Override
//	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
//	@RequestMapping(path = "{idNiko}" + PathFinder.PATH + PathFinder.SHOW_PATH, method = RequestMethod.GET)
//	public String showItemGet(Model model,@PathVariable Long idNiko) {
//
//		NikoNiko nikoBuffer = new NikoNiko();
//		nikoBuffer = nikoCrud.findOne(idNiko);
//		Long iduser = nikoBuffer.getUser().getId();
//
//		model.addAttribute("page",  "NikoNiko : " + nikoBuffer.getEntryDate());
//		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
//		model.addAttribute("item",DumpFields.fielder(super.getItem(idNiko)));
//		model.addAttribute("show_user", PathFinder.USER + PathFinder.PATH + iduser + PathFinder.PATH + PathFinder.SHOW_PATH);
//		model.addAttribute("show_changes_dates", PathFinder.DOT + PathFinder.PATH + PathFinder.SHOW_CHANGE_DATES);
//		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
//		model.addAttribute("go_update", PathFinder.UPDATE_ACTION);
//
//		return PathFinder.BASE_NIKONIKO + PathFinder.PATH + PathFinder.SHOW_PATH;
//	}
//
//	/**UNUSED???
//	 *
//	 * @param model
//	 * @param name
//	 * @return
//	 */
//	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE"})
//	@RequestMapping(path = {PathFinder.PATH, PathFinder.ROUTE_LIST}, method = RequestMethod.POST)
//	public String showVerticales(Model model,String name){
//
//		model.addAttribute("model", "nikoniko");
//		model.addAttribute("page",this.baseName + " " + PathFinder.LIST_ACTION.toUpperCase());
//		model.addAttribute("sortedFields",NikoNiko.FIELDS);
//		model.addAttribute("items",this.searchNikoNikos(name));
//		model.addAttribute("go_show", PathFinder.SHOW_ACTION);
//		model.addAttribute("go_create", PathFinder.CREATE_ACTION);
//		model.addAttribute("go_delete", PathFinder.DELETE_ACTION);
//
//		return listView;
//	}

	/**
	 * FIND A SPECIFIC USER
	 * @param id
	 * @return
	 */
	public ArrayList<NikoNiko> searchNikoNikos(String name){

		ArrayList<NikoNiko> nikonikoList = new ArrayList<NikoNiko>();
		nikonikoList = nikoCrud.getNikoNiko(name);

		return nikonikoList;

	}
}
