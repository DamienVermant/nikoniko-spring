package com.cgi.nikoniko.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.ChangeDates;

@Controller
@RequestMapping(ChangeDatesController.BASE_URL)
public class ChangeDatesController extends ViewBaseController<ChangeDates> {

	public final static String BASE_URL = "/change_date";

	public ChangeDatesController() {
		super(ChangeDates.class,BASE_URL);
	}
}
