package com.cgi.nikoniko.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cgi.nikoniko.controllers.base.view.ViewBaseAssociatedController;
import com.cgi.nikoniko.models.association.UserHasTeam;

@Controller
@RequestMapping(UserHasTeamController.BASE_URL)
public class UserHasTeamController extends ViewBaseAssociatedController<UserHasTeam> {

	public final static String BASE_URL = "/user_has_team";

	public UserHasTeamController() {
		super(UserHasTeam.class,BASE_URL);
	}
}
