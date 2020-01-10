package com.example.exam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .disable()
                .csrf()
                    .csrfTokenRepository(this.csrfTokenRepository())
                .and()
                .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginPage("/users/login")
                    .usernameParameter("user")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/error/unauthorized")
                ;
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");

        return repository;
    }
}
