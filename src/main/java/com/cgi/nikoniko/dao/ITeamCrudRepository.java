package com.cgi.nikoniko.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.Team;

public interface ITeamCrudRepository extends IBaseCrudRepository<Team>{

	/**
	 * GET ALL NIKONIKOS IDS RELATED TO A TEAM
	 * @param idTeam
	 * @return
	 */
	@Query(value = "SELECT nikoniko.id FROM nikoniko "
			+ "INNER JOIN user_has_team ON nikoniko.user_id = user_has_team.idLeft "
			+ "INNER JOIN team ON team.id= user_has_team.idRight WHERE team.id= :idTeam",nativeQuery=true)
	public List<BigInteger> getNikoNikoFromTeam(@Param("idTeam") long idTeam);

	/**
	 * @param idTeam
	 * @return
	 */
	@Query(value = "SELECT verticale.id FROM verticale INNER JOIN team ON verticale.id = team.verticale_id where team.id = :idTeam", nativeQuery=true)
	public Long getTeamVertical(@Param("idTeam") long idTeam);

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

	/**
	 * GET USER LIST CORRESPONDING WITH A NAME PARAMETER
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM team WHERE name LIKE %:name%", nativeQuery=true)
	public ArrayList<Team> getTeams(@Param("name") String name);

	/**
	 * GET ALL TEAMS ASSOCIATED WITH THE SAME VERTICALE
	 * @param idVerticale
	 * @return
	 */
	@Query(value = "SELECT * FROM team WHERE verticale_id = :idVerticale", nativeQuery=true)
	public ArrayList<Team> getAssociatedTeams(@Param("idVerticale") long idVerticale);

}
