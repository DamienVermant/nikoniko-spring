package com.cgi.nikoniko.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;
import com.cgi.nikoniko.models.tables.User;

public interface INikoNikoCrudRepository extends IBaseCrudRepository<NikoNiko>{


	/**
	 * FIND ALL NIKONIKO WITH SAME MOOD
	 * @param mood (range from 1 to 3)
	 * @return
	 */
	List<NikoNiko> findAllByMood(int mood);
	
	@Query(value = "SELECT * FROM user WHERE registration_number LIKE %:name%", nativeQuery=true)
	public ArrayList<User> getUsers(@Param("name") String name);
	
	@Query(value = "SELECT * FROM nikoniko INNER JOIN user on nikoniko.user_id = user.id WHERE user.registration_number LIKE %:name%", nativeQuery=true)
	public ArrayList<NikoNiko> getNikoNiko(@Param("name") String name);

}
