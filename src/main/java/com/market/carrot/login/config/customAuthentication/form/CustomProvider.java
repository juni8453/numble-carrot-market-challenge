package com.market.carrot.login.config.customAuthentication.form;

import com.market.carrot.global.Exception.BadCredentialsException;
import com.market.carrot.global.Exception.ExceptionMessage;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    /**
     * UserDetailsService 의 loadUserByUsername(String username) 호출 후 채워진 Authentication 구현체를 전달 받은 뒤
     * Authentication Manager 로 알맞은 Authentication Token 객체를 반환한다.
     */
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();
    MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(username);

    if (!passwordEncoder.matches(password, memberContext.getPassword())) {
      throw new BadCredentialsException(ExceptionMessage.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
    }

    return new UsernamePasswordAuthenticationToken(
        memberContext, null, memberContext.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
