package com.cgi.nikoniko.dao;

import java.util.Date;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.User;

public interface IUserCrudRepository extends IBaseCrudRepository<User>{

	User findByLogin(String login);
	
	@Query(value = "SELECT change_date FROM change_dates INNER JOIN "
			+ "nikoniko ON change_dates.nikoniko_id = :idNiko "
			+ "INNER JOIN user ON nikoniko.user_id = :idUser", nativeQuery=true)
	public Date nikoChangeDates(@Param("idNiko") long idNiko, @Param("idUser") long idUser);
	
	@Query(value = "SELECT * from nikoniko where nikoniko.user_id = :idUser")
	public NikoNiko getNikoNikoUser(@Param("idUser") long idUser);
	

}
