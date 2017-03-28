package com.cgi.nikoniko.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.User;

public interface IUserCrudRepository extends IBaseCrudRepository<User>{

	User findByLogin(String login);
	
	@Query(value = "SELECT max(nikoniko.id) from nikoniko where nikoniko.user_id = :idUser",nativeQuery=true)
	public Long getLastNikoNikoUser(@Param("idUser") long idUser);
	
}
