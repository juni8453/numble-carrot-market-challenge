package com.market.carrot.login.config.customAuthentication.form;

import com.market.carrot.global.Exception.NotFoundEntityException;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.LoginRepository;
import com.market.carrot.login.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final LoginRepository loginRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    /**
     * Provider 로 부터 호출받아 Authentication 내부를 채우고 다시 Provider 로 반환한다.
     */
    Member findMember = loginRepository.findByUsername(username)
        .orElseThrow(() -> new NotFoundEntityException("찾을 수 없는 멤버입니다.", HttpStatus.BAD_REQUEST));

    return new MemberContext(findMember);
  }
}
