package org.bossie.security.config;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

class SomeClass {}

@Configuration
@ComponentScan("org.bossie.security")
@EnableWebMvc
@EnableWebSecurity
@PropertySource("classpath:/db.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	Environment env;

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
		JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
		jdbcDao.setDataSource(dataSource());

		auth.userDetailsService(jdbcDao);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("db.driverClassName"));
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));

		return dataSource;
	}

	@Bean
	LocalSessionFactoryBean sessionFactoryBean() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setConfigLocation(new ClassPathResource("/hibernate.cfg.xml"));

		return sessionFactoryBean;
	}

	@Bean(destroyMethod="close")
	SessionFactory sessionFactory() {
		return sessionFactoryBean().getObject();
	}
}
