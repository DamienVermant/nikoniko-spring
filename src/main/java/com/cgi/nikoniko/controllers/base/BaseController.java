package com.cgi.nikoniko.controllers.base;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;

public abstract class BaseController <T extends DatabaseItem> {

//	public final static String REDIRECT = "redirect:";
//
//	public final static String LIST_ACTION= "list";
//	public final static String LIST_ID_ACTION= "list_id";
//	public final static String UPDATE_ACTION= "update";
//	public final static String DELETE_ACTION= "delete";
//	public final static String CREATE_ACTION= "create";
//	public final static String SHOW_ACTION= "show";
//	public final static String LOGIN_ACTION = "/login";
//
//	public final static String PATH = "/";
//	public final static String PATH_LIST_FILE = PATH + LIST_ACTION ;
//	public final static String PATH_LIST_ID_FILE = PATH + LIST_ID_ACTION ;
//	public final static String PATH_UPDATE_FILE = PATH + UPDATE_ACTION ;
//	public final static String PATH_DELETE_FILE = PATH + DELETE_ACTION ;
//	public final static String PATH_CREATE_FILE = PATH + CREATE_ACTION ;
//	public final static String PATH_SHOW_FILE = PATH + SHOW_ACTION ;
//	public final static String PATH_LOGIN = PATH + LOGIN_ACTION;
//
//	public final static String ROUTE_LIST = LIST_ACTION;
//	public final static String ROUTE_UPDATE = "{id}/"+ UPDATE_ACTION;
//	public final static String ROUTE_DELETE = "{id}/"+ DELETE_ACTION;
//	public final static String ROUTE_CREATE = CREATE_ACTION;
//	public final static String ROUTE_SHOW = "{id}/"+ SHOW_ACTION;
//
//	public final static String ROUTE_LOGIN = LOGIN_ACTION;

	@Autowired
	private IBaseCrudRepository<T> baseCrud;

	private Class<T> clazz;

	/**
	 *
	 * @return the clazz
	 */
	protected Class<T> getClazz(){
		return clazz;
	}

	protected BaseController (Class<T> clazz){
		this.clazz = clazz;
	}

	public String deleteItem(Long id){
		try {
			baseCrud.delete(id);
		} catch (Exception e) {
			return "Delete : FAIL";
		}
		return "Delete : SUCCESS";
	}

	public String deleteItem (@ModelAttribute T item) {
		try {
			baseCrud.delete(item);
		} catch (Exception e) {
			return "Delete : FAIL";
		}
		return "Delete : SUCCESS";
	}

	public T insertItem (@ModelAttribute T item) {
		baseCrud.save(item);
		return item;
	}

	public String updateItem (@ModelAttribute T item) {
		try {
			baseCrud.save(item);
		} catch (Exception e) {
			return "Update : FAIL";
		}
		return "Update : SUCCESS";
	}

	public T getItem (Long id) {
		T item = null;
		item = baseCrud.findOne(id);
		return item;
	}

	public ArrayList<T> getItems() {

		ArrayList<T> items = null;
		items = (ArrayList<T>) baseCrud.findAll();
		return items;
	}


}
