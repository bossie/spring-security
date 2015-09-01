package org.bossie.security.config;

import org.bossie.security.persistence.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

class SomeClass {}

@Configuration
@ComponentScan("org.bossie.security")
@EnableWebMvc
@EnableWebSecurity
@ImportResource("classpath:context.xml")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Dao dao;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/collection/*/**").anonymous()
			.antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
			.anyRequest().permitAll()
			.and()
		.httpBasic()
			.and()
		.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new UserDetailsService() {
			@Override public UserDetails loadUserByUsername(String username) {
				return dao.getUserByUsername(username);
			}
		});
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}
