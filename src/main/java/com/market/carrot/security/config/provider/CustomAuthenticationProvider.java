package com.market.carrot.security.config.provider;

import com.market.carrot.security.config.service.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    /**
     * AuthenticationManager 로 알맞은 형태의 Authentication Token 객체를 반환
     */
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(username);

    if (!passwordEncoder.matches(password, memberContext.getPassword())) {
      throw new BadCredentialsException("일치하지 않는 패스워드입니다.");
    }

    return UsernamePasswordAuthenticationToken.authenticated(
        memberContext, null, memberContext.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
