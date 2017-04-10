package com.cgi.nikoniko.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.dao.IChangeDatesCrudRepository;
import com.cgi.nikoniko.dao.INikoNikoCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;

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
