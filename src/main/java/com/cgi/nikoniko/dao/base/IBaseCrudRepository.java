package com.cgi.nikoniko.dao.base;

import org.springframework.data.repository.CrudRepository;

import com.cgi.nikoniko.models.modelbase.DatabaseItem;

public interface IBaseCrudRepository<T extends DatabaseItem> extends CrudRepository<T, Long> {

}
