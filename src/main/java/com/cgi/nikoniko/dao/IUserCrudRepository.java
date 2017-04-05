package com.cgi.nikoniko.dao;

import java.util.List;
import java.util.ArrayList;

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
	 * FIND USERS BY FIRSTNAME
	 * @param login
	 * @return
	 */
	List<User> findAllByFirstname(String firstname);

	/**
	 * FIND USERS BY REGISTRATION_NUMBER
	 * @param login
	 * @return
	 */
	User findByRegistrationcgi(String registrationcgi);

	/**
	 * GET LAST USER NIKONIKO
	 * @param idUser
	 * @return
	 */
	@Query(value = "SELECT max(nikoniko.id) from nikoniko where nikoniko.user_id = :idUser", nativeQuery=true)
	public Long getLastNikoNikoUser(@Param("idUser") long idUser);

	/**
	 * GET VERTICAL ID FOR A USER
	 * @param idUser
	 * @return
	 */
	@Query(value = "SELECT verticale.id FROM verticale INNER JOIN user ON verticale.id = user.verticale_id where user.id = :idUser", nativeQuery=true)
	public Long getUserVertical(@Param("idUser") long idUser);
	
	/**
	 * GET USER LIST CORRESPONDING WITH A NAME PARAMETER
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM user WHERE registration_number LIKE %:name%", nativeQuery=true)
	public ArrayList<User> getUsers(@Param("name") String name);

	/**
	 * GET SECOND TO LAST NIKO NIKO
	 * @param idUser
	 * @return
	 */
	@Query(value = "SELECT * from nikoniko where nikoniko.user_id = :idUser LIMIT 0,1", nativeQuery=true)
	public Long getLastLastNikoNikoUser(@Param("idUser") long idUser);

	/**
	 * GET NUMBER OF NIKONIKO FOR USER
	 * @param idUser
	 * @return
	 */
	@Query(value = "SELECT COUNT(*) from nikoniko where nikoniko.user_id = :idUser", nativeQuery=true)
	public int getNikoNikoNumberUser(@Param("idUser") long idUser);
	
	/**
	 * GET ALL USERS ASSOCIATED WITH THE SAME VERTICALE
	 * @param idVerticale
	 * @return
	 */
	@Query(value = "SELECT * FROM user WHERE verticale_id = :idVerticale", nativeQuery=true)
	public ArrayList<User> getAssociatedUsers(@Param("idVerticale") long idVerticale);
	
}
