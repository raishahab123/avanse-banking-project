package com.avanse.springboot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.avanse.springboot.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	CustomFilterAfterSecurityChainFilters filterForOpenPages;

	@Autowired
	CustomUserDetailService customUserDetailService;
//	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/admin/**").hasRole("ADMIN")
		
		.antMatchers("/**").permitAll()

//		.antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
//		.antMatchers("/","/viewPages/**","/viewDynamicPages/**","/index",  "/register" , "/public/api/**", "/career/**").permitAll()
//		.antMatchers("/resources/**", "/static/**", "/images/**","/css/**","/scss/**","/vendor/**", "/js/**").permitAll()
//		.antMatchers("/resources/**", "/static/**","/viewPagesAssets/**", "/img/**","/css/**","/scss/**","/vendors/**", "/js/**").permitAll()
//		.antMatchers("/addManualUser").permitAll() //this on to be deleted later, Only for testing
//		.antMatchers("/blog/**").permitAll() //this on to be deleted later, Only for testing
//		.antMatchers("/landing/**").permitAll() //this on to be deleted later, Only for testing
//		.antMatchers("/apply-now/**").permitAll() //this on to be deleted later, Only for testing
//		.antMatchers("/courseDetail/**").permitAll() //this on to be deleted later, Only for testing
//		.antMatchers("/Country/**").permitAll() //this on to be deleted later, Only for testing
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.permitAll()
		.failureUrl("/login?error= true")
		.defaultSuccessUrl("/admin")
		.usernameParameter("email")
		.passwordParameter("password")
		.and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login")
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID")
		.and()
		.exceptionHandling()
		.and()
		.csrf()
		.disable();
		
		http.addFilterAfter(filterForOpenPages, BasicAuthenticationFilter.class);
		
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * Need to override the default configure method because
	 * we have created a custom user
	*/
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(customUserDetailService);
	}	
}