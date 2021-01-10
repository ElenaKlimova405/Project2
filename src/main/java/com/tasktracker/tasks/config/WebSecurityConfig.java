package com.tasktracker.tasks.config;

import com.tasktracker.tasks.service.UsersSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UsersSevice userSevice;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/", "/about", "/registration").permitAll()  // разешаем этим пользоваться всем

                    .antMatchers("/tasks").access("hasAuthority('USER')")
                    .antMatchers("/tasks/add").access("hasAuthority('USER')")
                    .antMatchers("/tasks/{id}").access("hasAuthority('USER')")
                    .antMatchers("/tasks/{id}/edit").access("hasAuthority('USER')")
                    .antMatchers("/tasks/{id}/remove").access("hasAuthority('USER')")

                    .antMatchers("/users").access("hasAuthority('USER')")
                    .antMatchers("/users/add").access("hasAuthority('USER')")
                    .antMatchers("/users/{id}").access("hasAuthority('USER')")
                    .antMatchers("/users/{id}/edit").access("hasAuthority('USER')")
                    .antMatchers("/users/{id}/remove").access("hasAuthority('USER')")

                    .anyRequest().authenticated() // а на другие запросы нужна регистрация
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll() // разешаем этим пользоваться всем
                .and()
                    .logout()
                    .permitAll(); // разешаем этим пользоваться всем
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSevice)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}