package com.cgi.nikoniko.service.security;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.nikoniko.dao.IRoleCrudRepository;
import com.cgi.nikoniko.dao.IUserCrudRepository;
import com.cgi.nikoniko.dao.IUserHasRoleCrudRepository;
import com.cgi.nikoniko.models.tables.RoleCGI;
import com.cgi.nikoniko.models.tables.User;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	private IUserCrudRepository userCrud;

	@Autowired
	private IRoleCrudRepository roleCrud;

	@Autowired
	private IUserHasRoleCrudRepository userRoleCrud;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String login)
			throws UsernameNotFoundException {
		User user = userCrud.findByLogin(login);

		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();

		if (user.getEnable()) {
			for (RoleCGI role : this.setRolesForUserGet(user.getId())) {
				grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
			}
		}
		return new org.springframework.security.core.userdetails
					.User(user.getLogin(),user.getPassword(),grantedAuthorities);
	}

	public ArrayList<RoleCGI> setRolesForUserGet(Long idUser) {

		List<Long> ids = new ArrayList<Long>();
		ArrayList<RoleCGI> roleList = new ArrayList<RoleCGI>();

		List<BigInteger> idsBig = userRoleCrud.findAssociatedRole(idUser);

		if (!idsBig.isEmpty()) {//if no association => return empty list which can't be use with findAll(ids)
			for (BigInteger id : idsBig) {
				ids.add(id.longValue());

			}
			roleList = (ArrayList<RoleCGI>) roleCrud.findAll(ids);
		}

		return roleList;
	}

}
