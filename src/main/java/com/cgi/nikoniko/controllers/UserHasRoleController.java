package com.cgi.nikoniko.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cgi.nikoniko.controllers.base.view.ViewBaseAssociatedController;
import com.cgi.nikoniko.models.association.UserHasRole;

@Controller
@RequestMapping(UserHasRoleController.BASE_URL)
public class UserHasRoleController extends ViewBaseAssociatedController<UserHasRole> {

	public final static String BASE_URL = "/user_has_role";

	public UserHasRoleController() {
		super(UserHasRole.class,BASE_URL);
	}
}
