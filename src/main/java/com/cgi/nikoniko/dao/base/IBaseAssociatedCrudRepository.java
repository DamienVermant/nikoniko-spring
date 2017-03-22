package com.cgi.nikoniko.dao.base;

import org.springframework.data.repository.CrudRepository;

import com.cgi.nikoniko.models.modelbase.AssociationItem;
import com.cgi.nikoniko.models.modelbase.AssociationItemId;

public interface IBaseAssociatedCrudRepository<T extends AssociationItem> extends CrudRepository<T, AssociationItemId>{

}
