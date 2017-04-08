package com.cgi.nikoniko.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseAssociatedCrudRepository;
import com.cgi.nikoniko.models.association.UserHasTeam;


public interface IUserHasTeamCrudRepository extends IBaseAssociatedCrudRepository <UserHasTeam> {

	/**
	 * Query to see if one or more users are linked to a team
	 *
	 * @param idValue : id of the selected team
	 */
	@Query(value = "SELECT idLeft FROM user_has_team WHERE idRight = :idValue", nativeQuery=true)
	public List<BigInteger> findAssociatedUser(@Param("idValue") long idValue);

	/**
	 * Query to see if one or more teams are linked to a user
	 *
	 * @param idValue : id of the selected user
	 */
	@Query(value = "SELECT idRight FROM user_has_team WHERE idLeft = :idValue", nativeQuery=true)
	public List<BigInteger> findAssociatedTeam(@Param("idValue") long idValue);

	/**
	 * QUERY TO SET THE LEAVING DATE WHEN A USER LEAVE A TEAM
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@Query(value = "SELECT leaving_date FROM user_has_team WHERE idLeft = :idUser AND idRight = :idTeam", nativeQuery=true)
	public UserHasTeam findAssociatedUserTeam(@Param("idUser") long idUser, @Param("idTeam") long idTeam);

	/**
	 * QUERY THAT SELECT ALL INFORMATIONS FROM user_has_team WITH A GIVEN idUser AND idRight
	 * @param idUser
	 * @param idTeam
	 * @return
	 */
	@Query(value = "SELECT * FROM user_has_team WHERE idLeft = :idUser AND idRight = :idTeam", nativeQuery=true)
	public UserHasTeam findAssociatedUserTeamALL(@Param("idUser") long idUser, @Param("idTeam") long idTeam);


}
