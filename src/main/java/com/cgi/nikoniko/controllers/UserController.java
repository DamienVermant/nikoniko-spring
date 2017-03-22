package com.cgi.nikoniko.controllers;

import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.NikoNiko;
import com.cgi.nikoniko.models.User;

@Controller
@RequestMapping(UserController.BASE_URL)
public class UserController extends ViewBaseController<User> {

	public final static String BASE_URL = "/user";
	

	public UserController() {
		super(User.class,BASE_URL);
	}
	
	
}
