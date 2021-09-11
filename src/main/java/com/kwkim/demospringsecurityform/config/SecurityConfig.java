package com.kwkim.demospringsecurityform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//요청을 인가하는 방식을 설정
                .mvcMatchers("/", "info","/account/**").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated();
        http.formLogin();
        http.httpBasic();
    }

/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //noop 입력된 패스워드를 암호화줌
        // 아래설정을 바탕으로 인메모리 유저가 생성됨
        auth.inMemoryAuthentication()
                .withUser("kwkim").password("{noop}123").roles("USER")
                .and().withUser("admin").password("{noop}!@#").roles("ADMIN");
    }*/
}
