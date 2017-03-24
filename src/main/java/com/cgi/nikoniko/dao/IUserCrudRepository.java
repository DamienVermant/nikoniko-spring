package com.cgi.nikoniko.dao;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.User;

public interface IUserCrudRepository extends IBaseCrudRepository<User>{

	User findByLogin(String login);
}
