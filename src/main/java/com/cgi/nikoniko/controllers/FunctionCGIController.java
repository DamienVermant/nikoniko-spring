package com.cgi.nikoniko.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.view.ViewBaseController;
import com.cgi.nikoniko.models.tables.FunctionCGI;
import com.cgi.nikoniko.utils.DumpFields;

@Controller
@RequestMapping(FunctionCGIController.BASE_URL)
public class FunctionCGIController extends ViewBaseController<FunctionCGI> {

	public final static String BASE_URL = "/function";

	public FunctionCGIController() {
		super(FunctionCGI.class,BASE_URL);
	}
}
