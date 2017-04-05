package com.cgi.nikoniko.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.authorizeRequests()
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()
					.loginPage("/login")
					.usernameParameter("login")
					.passwordParameter("password")
					.permitAll()//;
					.defaultSuccessUrl("/menu", true);

			// TODO : REMOVE THIS

//			.and()
//				.httpBasic()
//				.and()
//				.authorizeRequests().anyRequest().anonymous()
//				.antMatchers("/demo","/demo/**")
//				.permitAll()
//				;
	}

}



