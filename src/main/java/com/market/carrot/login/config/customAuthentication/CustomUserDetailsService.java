package com.market.carrot.login.config.customAuthentication;

import com.market.carrot.login.domain.LoginRepository;
import com.market.carrot.login.domain.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
     * Provider 로 부터 호출받아 Authentication 내부를 채우고 반환한다.
     */
    Member findMember = loginRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 멤버입니다."));

    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority(findMember.getRole().getKey()));

    return new MemberContext(findMember, roles);
  }
}
