package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.cgi.nikoniko.models.tables.ChangeDates;
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
	 *
	 * @param model
	 * @return
	 */
	@Override
	@Secured({"ROLE_ADMIN","ROLE_VP"})
	@RequestMapping(path = {PATH, ROUTE_LIST}, method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("page",super.baseName + " " + LIST_ACTION.toUpperCase());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("items",DumpFields.listFielder(super.getItems()));
		model.addAttribute("go_show", SHOW_ACTION);
		model.addAttribute("go_create", CREATE_ACTION);
		model.addAttribute("go_delete", DELETE_ACTION);
		return super.listView;
	}
	/**
	 *
	 */
	@Override
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{idNiko}" + PATH + SHOW_PATH, method = RequestMethod.GET)
	public String showItemGet(Model model,@PathVariable Long idNiko) {

		NikoNiko nikoBuffer = new NikoNiko();
		nikoBuffer = nikoCrud.findOne(idNiko);
		Long iduser = nikoBuffer.getUser().getId();

		model.addAttribute("page",  "NikoNiko : " + nikoBuffer.getEntry_date());
		model.addAttribute("sortedFields",DumpFields.createContentsEmpty(super.getClazz()).fields);
		model.addAttribute("item",DumpFields.fielder(super.getItem(idNiko)));
		model.addAttribute("show_user", USER + PATH + iduser + PATH + SHOW_PATH);
		model.addAttribute("show_changes_dates", DOT + PATH + SHOW_CHANGE_DATES);
		model.addAttribute("go_delete", DELETE_ACTION);
		model.addAttribute("go_update", UPDATE_ACTION);

		return BASE_NIKONIKO + PATH + SHOW_PATH;
	}

	/**
	 *
	 * Recupération de tous les changedate liés à un nikoniko
	 */
	@Secured({"ROLE_ADMIN"})
	@RequestMapping("{nikonikoId}/showChangeDates")
	public String getChangeDatesFromNikoNiko(Model model, @PathVariable Long nikonikoId) {
		NikoNiko niko = super.getItem(nikonikoId);
		Set<ChangeDates> chang =  niko.getChangeDates();
		List<ChangeDates> listOfChang = new ArrayList<ChangeDates>(chang);

		model.addAttribute("page", niko.getId() + " nikonikos");
		model.addAttribute("sortedFields", ChangeDates.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfChang));
		model.addAttribute("back", DOT + PATH + SHOW_PATH);
		return "nikoniko/showAllRelation";
	}

	/**
	 *
	 * Page de creation d'un nikoniko pour un user
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{changId}/add", method = RequestMethod.GET)
	public String createItemGet(Model model, @PathVariable Long changId) {
		NikoNiko niko = super.getItem(changId);
		ChangeDates chang = new ChangeDates();

		model.addAttribute("page",niko.getId() + " " + CREATE_ACTION.toUpperCase());
		model.addAttribute("sortedFields",ChangeDates.FIELDS);
		model.addAttribute("item",DumpFields.createContentsEmpty(chang.getClass()));
		model.addAttribute("go_index", LIST_ACTION);
		model.addAttribute("create_item", CREATE_ACTION);
		return "nikoniko/addNikoNiko";
	}

	/**
	 *
	 * Creation d'un nikoniko
	 */
	@Secured({"ROLE_ADMIN","ROLE_GESTIONNAIRE","ROLE_VP","ROLE_USER"})
	@RequestMapping(path = "{nikonikoId}/create", method = RequestMethod.POST)
	public String createItemPost(Model model, ChangeDates chang, @PathVariable Long nikonikoId) {

		try {
			NikoNiko niko = super.getItem(nikonikoId);
			chang.setNikoniko(niko);
			changeCrud.save(chang);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return "redirect:/nikoniko/1/link";
	}

}
