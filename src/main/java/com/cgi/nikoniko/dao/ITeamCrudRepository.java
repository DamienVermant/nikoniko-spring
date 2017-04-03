package com.cgi.nikoniko.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.Team;

public interface ITeamCrudRepository extends IBaseCrudRepository<Team>{

	@Query(value = "SELECT nikoniko.id FROM nikoniko "
			+ "INNER JOIN user_has_team ON nikoniko.user_id = user_has_team.idLeft "
			+ "INNER JOIN team ON team.id= user_has_team.idRight WHERE team.id= :idTeam",nativeQuery=true)
	public List<BigInteger> getNikoNikoFromTeam(@Param("idTeam") long idTeam);

	/**
	 * FIND TEAM BY HIS NAME
	 * @param name
	 * @return team
	 */
	Team findByName(String name);


	/**
	 * FIND TEAM BY HIS SERIAL
	 * @param Serial
	 * @return team
	 */
	Team findBySerial(String serial);


}
