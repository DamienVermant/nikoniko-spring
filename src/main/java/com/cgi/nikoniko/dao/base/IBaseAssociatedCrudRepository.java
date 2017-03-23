package com.cgi.nikoniko.dao.base;

import org.springframework.data.repository.CrudRepository;

import com.cgi.nikoniko.models.association.base.AssociationItem;
import com.cgi.nikoniko.models.association.base.AssociationItemId;

public interface IBaseAssociatedCrudRepository<T extends AssociationItem> extends CrudRepository<T, AssociationItemId>{



}
