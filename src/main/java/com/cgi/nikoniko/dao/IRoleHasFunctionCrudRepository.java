package com.cgi.nikoniko.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cgi.nikoniko.dao.base.IBaseAssociatedCrudRepository;
import com.cgi.nikoniko.models.association.RoleHasFunction;

public interface IRoleHasFunctionCrudRepository extends IBaseAssociatedCrudRepository <RoleHasFunction>  {
	
	@Query(value = "SELECT idRight FROM role_has_function WHERE idLeft = :idValue", nativeQuery=true)
	public List<BigInteger> findAssociatedFunction(@Param("idValue") long idValue);

}
