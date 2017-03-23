package com.cgi.nikoniko.dao.base;

import org.springframework.data.repository.CrudRepository;

import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;

public interface IBaseCrudRepository<T extends DatabaseItem> extends CrudRepository<T, Long> {

}
