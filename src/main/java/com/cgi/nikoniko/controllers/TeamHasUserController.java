package com.cgi.nikoniko.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.TeamHasUser;

@Controller
@RequestMapping(TeamHasUserController.BASE_URL)
public class TeamHasUserController {

	public final static String BASE_URL = "/team_has_user";

	public TeamHasUserController() {
//		super(TeamHasUser.class,BASE_URL);
	}
}
