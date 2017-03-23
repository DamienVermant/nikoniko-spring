package com.cgi.nikoniko.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.tables.FunctionCGI;

@Controller
@RequestMapping(FunctionCGIController.BASE_URL)
public class FunctionCGIController extends ViewBaseController<FunctionCGI> {

	public final static String BASE_URL = "/function_cgi";

	public FunctionCGIController() {
		super(FunctionCGI.class,BASE_URL);
	}
}
