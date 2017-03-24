package com.cgi.nikoniko.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseAssociatedCrudRepository;
import com.cgi.nikoniko.models.association.UserHasRole;
import com.cgi.nikoniko.models.association.UserHasRole;
import com.cgi.nikoniko.models.association.UserHasTeam;

public interface IUserHasRoleCrudRepository extends IBaseAssociatedCrudRepository <UserHasRole> {

	@Query(value = "SELECT * FROM user_has_role WHERE idLeft = :idUser", nativeQuery=true)
	public UserHasTeam findAssociatedUserRoleALL(@Param("idUser") long idUser);

	@Query(value = "SELECT idRight FROM user_has_role WHERE idLeft = :idUser", nativeQuery=true)
	public List<BigInteger> findAssociatedRole(@Param("idUser") long idUser);

//	@Query(value = "SET @@FOREIGN_KEY_CHECKS = 0", nativeQuery=true)
//	public void unsetForeignKeys();
//
//	@Query(value = "SET @@FOREIGN_KEY_CHECKS = 1", nativeQuery=true)
//	public void setForeignKeys();


}
