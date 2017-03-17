package com.cgi.nikoniko.dao;

import org.springframework.data.repository.CrudRepository;

import com.cgi.nikoniko.models.TeamHasUser;

public interface ITeamHasUserCrudRepository <T extends TeamHasUser> extends CrudRepository<T, Long>{

}
