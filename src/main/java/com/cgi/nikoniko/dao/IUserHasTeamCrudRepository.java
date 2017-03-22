package com.cgi.nikoniko.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseAssociatedCrudRepository;
import com.cgi.nikoniko.models.UserHasTeam;


public interface IUserHasTeamCrudRepository extends IBaseAssociatedCrudRepository <UserHasTeam> {

//	/**
//	 * WARNING : Unable to recognize "selectedId" proprely (return null event if the query is ok)
//	 *
//	 * @param linkedIds : id(s) of element(s) linked to the selectedId in association table
//	 * @param selectedId
//	 * @param idValue
//	 */
//	@Query(value = "SELECT :linkedIds FROM user_has_team WHERE :selectedId"
//			+ "=:idValue", nativeQuery=true)
//	public List<BigInteger> findAssociated(	@Param("linkedIds") String linkedIds,
//										@Param("selectedId") String selectedId,
//										@Param("idValue") long idValue);


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


//	/**
//	 *  Query to set an association between an user and a team
//	 *
//	 * @param idLeft : id of the user
//	 * @param idRight : id of the team
//	 * @param arrivalDate : arrival date (given in a string format)
//	 * @param leavingDate : leaving date (given in a string format)
//	 * @return
//	 */
//	@Query(value = "INSERT INTO user_has_team VALUES (:idLeft, :idRight,"
//				 + ":arrivalDate, :leavingDate)", nativeQuery=true)
//	public List<BigInteger> associateTeamAndUser(	@Param("idLeft") long idLeft,
//													@Param("idRight") long idRight,
//													@Param("arrivalDate") String arrivalDate,
//													@Param("leavingDate") String leavingDate);



}
