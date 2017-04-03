package com.cgi.nikoniko.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.IChangeDatesCrudRepository;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(NikoNikoController.BASE_URL)
public class NikoNikoController extends ViewBaseController<NikoNiko> {


	public final static String BASE_URL = "/nikoniko";

	public final static String DOT = ".";
	public final static String PATH = "/";
	public final static String USER = "/user";
	public final static String BASE_NIKONIKO = "nikoniko";
	public static final String SHOW_PATH = "show";
	public final static String SHOW_CHANGE_DATES = "showChangeDates";

	@Autowired
	INikoNikoCrudRepository nikoCrud;

	@Autowired
	IChangeDatesCrudRepository changeCrud;

	public NikoNikoController() {
		super(NikoNiko.class,BASE_URL);
	}

	/**
	 * SHOW A SELECT NIKONIKO
	 */
	@Override
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{idNiko}" + PATH + SHOW_PATH, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long idNiko) {

		NikoNiko nikoBuffer = new NikoNiko();
		nikoBuffer = nikoCrud.findOne(idNiko);
		Long iduser = nikoBuffer.getUser().getId();

		model.addAttribute("page",  "NikoNiko : " + nikoBuffer.getEntryDate());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(idNiko)));
		model.addAttribute("show_user", USER + PATH + iduser + PATH + SHOW_PATH);
		model.addAttribute("show_changes_dates", DOT + PATH + SHOW_CHANGE_DATES);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);

		return BASE_NIKONIKO + PATH + SHOW_PATH;
	}
}
