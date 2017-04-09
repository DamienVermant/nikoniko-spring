package com.cgi.nikoniko.service.security;

import java.util.HashSet;
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
import com.cgi.nikoniko.utils.UtilsFunctions;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	private IUserCrudRepository userCrud;

	@Autowired
	private IRoleCrudRepository roleCrud;

	@Autowired
	private IUserHasRoleCrudRepository userRoleCrud;

	/**
	 * RECUPERATION D'UN UTILISATEUR PAR SON LOGIN (DROIT + LOGIN ET PASSWORD POUR POUVOIR SE CONNECTER)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String login)
			throws UsernameNotFoundException {
		
		User user = userCrud.findByLogin(login);

		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();

		if (user.getEnable()) {
			for (RoleCGI role : UtilsFunctions.setRolesForUserGet(user.getId(), userRoleCrud, roleCrud)) {
				grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
			}
		}
		return new org.springframework.security.core.userdetails
					.User(user.getLogin(),user.getPassword(),grantedAuthorities);
	}


}
