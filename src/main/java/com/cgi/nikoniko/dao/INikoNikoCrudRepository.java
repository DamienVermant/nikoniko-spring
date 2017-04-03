package com.cgi.nikoniko.dao;

import java.util.List;

import com.cgi.nikoniko.dao.base.IBaseCrudRepository;
import com.cgi.nikoniko.models.tables.NikoNiko;

public interface INikoNikoCrudRepository extends IBaseCrudRepository<NikoNiko>{


	/**
	 * FIND ALL NIKONIKO WITH SAME MOOD
	 * @param mood (range from 1 to 3)
	 * @return
	 */
	List<NikoNiko> findAllByMood(int mood);

}
