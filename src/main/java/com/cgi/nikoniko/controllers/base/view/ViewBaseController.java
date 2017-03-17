package com.cgi.nikoniko.controllers.base.view;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cgi.nikoniko.controllers.base.BaseController;
import com.cgi.nikoniko.models.modelbase.DatabaseItem;
import com.cgi.nikoniko.utils.DumpFields;


public abstract class ViewBaseController<T extends DatabaseItem> extends BaseController<T> {

	protected ViewBaseController(Class<T> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}
}
