package com.cgi.nikoniko.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.cgi.nikoniko.models.hello.Greetings;

public interface GreetingsCrudRepository extends CrudRepository<Greetings, Long> {

	List<Greetings> findAll(Pageable pageable);

	

}
