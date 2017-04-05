package com.cgi.nikoniko.controllers.base;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cgi.nikoniko.dao.base.IBaseAssociatedCrudRepository;
import com.cgi.nikoniko.models.association.base.AssociationItem;
import com.cgi.nikoniko.models.association.base.AssociationItemId;

public abstract class BaseAssociatedController <T extends AssociationItem> {
	//TODO : delete
	public final static String REDIRECT = "redirect:";

	public final static String LIST_ACTION= "list";
	public final static String UPDATE_ACTION= "update";
	public final static String DELETE_ACTION= "delete";
	public final static String CREATE_ACTION= "create";
	public final static String SHOW_ACTION= "show";

	public final static String PATH = "/";
	public final static String PATH_LIST_FILE = PATH + LIST_ACTION ;
	public final static String PATH_UPDATE_FILE = PATH + UPDATE_ACTION ;
	public final static String PATH_DELETE_FILE = PATH + DELETE_ACTION ;
	public final static String PATH_CREATE_FILE = PATH + CREATE_ACTION ;
	public final static String PATH_SHOW_FILE = PATH + SHOW_ACTION ;

	public final static String ROUTE_LIST = LIST_ACTION;
	public final static String ROUTE_UPDATE = "{idl}/{idr}/"+ UPDATE_ACTION;
	public final static String ROUTE_DELETE = "{idl}/{idr}/"+ DELETE_ACTION;
	public final static String ROUTE_CREATE = CREATE_ACTION;
	public final static String ROUTE_SHOW = "{idl}/{idr}/"+ SHOW_ACTION;


	@Autowired
	private IBaseAssociatedCrudRepository<T> baseAssociatedCrud;

	private Class<T> clazz;

	protected Class<T> getClazz(){
		return clazz;
	}

	protected BaseAssociatedController (Class<T> clazz){
		this.clazz = clazz;
	}

	public String deleteItem (@ModelAttribute T item) {
		try {
			baseAssociatedCrud.delete(item);
		} catch (Exception e) {
			return "Delete : FAIL";
		}
		return "Delete : SUCCESS";
	}

	public T insertItem (@ModelAttribute T item) {
		baseAssociatedCrud.save(item);
		return item;
	}

	public String updateItem (@ModelAttribute T item) {
		try {
			baseAssociatedCrud.save(item);
		} catch (Exception e) {
			return "Update : FAIL";
		}
		return "Update : SUCCESS";
	}

	public T getItem (Long idLeft, Long idRight) {
		T item = null;
		item = baseAssociatedCrud.findOne(new AssociationItemId(idLeft,idRight));
		return item;
	}//TODO : a test is necessary to validate this part

	public ArrayList<T> getItems() {

		ArrayList<T> items = null;
		items = (ArrayList<T>) baseAssociatedCrud.findAll();
		return items;
	}
}
