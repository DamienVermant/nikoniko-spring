package com.cgi.nikoniko.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.RoleCGI;

@Controller
@RequestMapping(RoleCGIController.BASE_URL)
public class RoleCGIController extends ViewBaseController<RoleCGI> {

	public final static String BASE_URL = "/rolecgi";

	public RoleCGIController() {
		super(RoleCGI.class,BASE_URL);
	}
}
