package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.ChangeDates;
import com.cgi.nikoniko.models.NikoNiko;
import com.cgi.nikoniko.models.User;
import com.cgi.nikoniko.models.Verticale;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(NikoNikoController.BASE_URL)
public class NikoNikoController extends ViewBaseController<NikoNiko> {

	public final static String BASE_URL = "/nikoniko";

	public NikoNikoController() {
		super(NikoNiko.class,BASE_URL);
	}

//	@RequestMapping("{nikoId}/nikonikolink")
//	public String getNikoNikosForUser(Model model, @PathVariable Long nikoId) {
//		NikoNiko niko = super.getItem(nikoId);
//		User user = new User();
//
//		model.addAttribute("page", " nikonikos");
//		model.addAttribute("sortedFields",user.fields);
//		model.addAttribute("item", DumpFields.fielder(niko.getUser()));
//		return "base/nikoniko";
//	}

	@RequestMapping("{nikonikoId}/link")
	public String getChangsForNikoNiko(Model model, @PathVariable Long nikonikoId) {
		NikoNiko niko = super.getItem(nikonikoId);
		Set<ChangeDates> chang =  niko.getChangeDates();
		List<ChangeDates> listOfChang = new ArrayList<ChangeDates>(chang);

		model.addAttribute("page", niko.getId() + " nikonikos");
		model.addAttribute("sortedFields", ChangeDates.FIELDS);
		model.addAttribute("items", DumpFields.listFielder(listOfChang));
		return "base/nikoUser";
	}

}
