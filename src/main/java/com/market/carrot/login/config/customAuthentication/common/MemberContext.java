package com.market.carrot.login.config.customAuthentication.common;

import com.market.carrot.member.domain.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * 기존에 User 객체를 상속받아서 해당 생성자를 통해 Authentication 에 유저 정보 및 권한을 셋팅했었는데, 객체는 다중 상속이 안되기 때문에 UserDetails
 * 인터페이스를 구현할 수 있도록 변경
 */
@Getter
public class MemberContext implements UserDetails, OAuth2User {

  private final Member member;
  private final Map<String, Object> attributes;

  public MemberContext(Member member, Map<String, Object> attributes) {
    this.member = member;
    this.attributes = attributes;
  }

  public MemberContext(Member member) {
    this(member, null);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority(member.getRole().getKey()));

    return roles;
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }

  @Override
  public String getUsername() {
    return member.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  /**
   * getAttributes(), getName() 은 OAuth2User 추상 메서드
   */

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public String getName() {
    return null;
  }
}
