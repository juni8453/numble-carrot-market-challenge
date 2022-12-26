package com.market.carrot.login.config;

import com.market.carrot.login.config.customAuthentication.oauth2.CustomOAuth2UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private final CustomOAuth2UserDetailsService oAuth2UserDetailsService;

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
    http.csrf().disable();

    http.authorizeRequests()
        .antMatchers("/user/**").authenticated()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().permitAll();

    // Form Login 인증 설정
    http.formLogin()
        .loginPage("/loginForm")
        .loginProcessingUrl("/login") // /login 추소 호출 시 시큐리티카 낚아채서 대신 로그인 진행 (컨트롤러 만들 필요 X)
        .defaultSuccessUrl("/"); // 로그인 성공 후 보내지는 주소 (특정 페이지 요청해서 인증이 성공되면 / 가 아닌 요청한 페이지로 자동 이동)

    // OAuth2 Login 인증 설정
    http.oauth2Login()
        .loginPage("/loginForm")

        /**
         * OAuth 로그인 후 후처리가 필요.
         * 1. Access Token + 사용자 프로필 정보 가져오기
         * 2. OAuth 에서 넘긴 프로필 정보 이외의 추가적인 정보가 필요없다면 자동으로 회원가입 진행시키면 됨.
         */
        .userInfoEndpoint()
        .userService(oAuth2UserDetailsService);

    return http.build();
  }
}
