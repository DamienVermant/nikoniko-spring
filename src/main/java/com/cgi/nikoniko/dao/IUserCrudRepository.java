package com.cgi.nikoniko.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.User;

public interface IUserCrudRepository extends IBaseCrudRepository<User>{

	/**
	 * FIND USER BY HIS LOGIN
	 * @param login
	 * @return
	 */
	User findByLogin(String login);
	
	/**
	 * GET LAST USER NIKONIKO
	 * @param idUser
	 * @return
	 */
	@Query(value = "SELECT max(nikoniko.id) from nikoniko where nikoniko.user_id = :idUser", nativeQuery=true)
	public Long getLastNikoNikoUser(@Param("idUser") long idUser);
	
	@Query(value = "SELECT verticale.id FROM verticale INNER JOIN user ON verticale.id = user.verticale_id where user.id = :idUser", nativeQuery=true)
	public Long getUserVertical(@Param("idUser") long idUser);
	
}
