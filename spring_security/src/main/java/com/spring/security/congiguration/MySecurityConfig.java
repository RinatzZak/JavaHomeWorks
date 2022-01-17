package com.spring.security.congiguration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

import javax.sql.DataSource;

@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource);

//        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
//
//        auth.inMemoryAuthentication().withUser(userBuilder.username("Rinat").password("Rinat").roles("employee"));
//        auth.inMemoryAuthentication().withUser(userBuilder.username("Elena").password("Elena").roles("HR"));
//        auth.inMemoryAuthentication().withUser(userBuilder.username("Ivan").password("Ivan").roles("manager", "HR"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").hasAnyRole("employee", "HR", "manager");
        http.authorizeRequests().antMatchers("/hr_info").hasRole("HR");
        http.authorizeRequests().antMatchers("/manager_info").hasRole("manager").and().formLogin().permitAll();
    }

}
