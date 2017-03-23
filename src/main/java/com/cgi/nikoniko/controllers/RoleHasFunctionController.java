package com.cgi.nikoniko.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cgi.nikoniko.controllers.base.view.ViewBaseAssociatedController;
import com.cgi.nikoniko.models.association.RoleHasFunction;

@Controller
@RequestMapping(RoleHasFunctionController.BASE_URL)
public class RoleHasFunctionController extends ViewBaseAssociatedController<RoleHasFunction> {

	public final static String BASE_URL = "/role_has_function";

	public RoleHasFunctionController() {
		super(RoleHasFunction.class,BASE_URL);
	}
}
