package com.market.carrot.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class securityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    /**
     * 인가
     */
    http.authorizeRequests()
        .antMatchers("/user/**").authenticated()
        .anyRequest().permitAll();

    /**
     * 로그인 인증
     */
    http.csrf().disable().formLogin()
        .loginPage("/loginForm") // 사용자가 로그인을 시도하는 화면
        .usernameParameter("username")
        .passwordParameter("password")
        .loginProcessingUrl("/login") // 로그인 시 처리할 URL
        .successHandler(((request, response, authentication) -> {
          log.debug("로그인 성공");
          response.sendRedirect("/");
        }))

        .failureHandler(((request, response, exception) -> {
          log.debug("로그인 실패");
          response.sendRedirect("/");
        }))

        .and()
        .logout().logoutSuccessUrl("/");

    /**
     * 로그아웃 인증
     */
    http.logout()
        .logoutSuccessUrl("/loginForm")
        .deleteCookies("JSESSIONID");

    return http.build();
  }
}
