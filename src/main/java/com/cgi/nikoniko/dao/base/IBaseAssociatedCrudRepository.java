package com.cgi.nikoniko.dao.base;

import org.springframework.data.repository.CrudRepository;

import com.cgi.nikoniko.models.AssociationItemId;
import com.cgi.nikoniko.models.modelbase.AssociationItem;

public interface IBaseAssociatedCrudRepository<T extends AssociationItem> extends CrudRepository<T, AssociationItemId>{

}
