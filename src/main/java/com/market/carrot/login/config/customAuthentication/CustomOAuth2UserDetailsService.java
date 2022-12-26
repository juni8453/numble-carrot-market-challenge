package com.market.carrot.login.config.customAuthentication;

import com.market.carrot.login.config.customAuthentication.provider.OAuth2UserInfo;
import com.market.carrot.login.domain.LoginRepository;
import com.market.carrot.login.domain.Member;
import com.market.carrot.login.domain.Role;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * loadUser() 는 Form Login 후처리 메서드인 loadUserByUsername() 와 비슷한 역할을 한다.
 * userRequest.getClientRegistration(); -> 클라이언트 정보가 담겨져있다. (Google, Facebook 등..)
 * userRequest.getAccessToken(); -> 인증 후 발급된 Access Token 정보, 하지만 Access Token + 사용자 프로필 정보를 한번에
 * 가져오기 때문에 Access Token 정보는 딱히 필요없다. userRequest.getAttributes(); -> Google, Facebook 의 사용자 정보가
 * 담겨져있고, 내 Member Entity 에 알맞게 저장하면 됨.
 */

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserDetailsService extends DefaultOAuth2UserService {

  private final LoginRepository loginRepository;
  private OAuth2UserInfo userInfo;

  /**
   * Form Login 에서 loadUserByUsername(username) 는 회원가입 처리가 아닌, DB 에서 회원을 찾아 해당 회원의 정보 + 권한을
   * Authentication 에 담는 역할 OAuth Login 에서 loadUser(userRequest) 는 강제 회원 가입, 중복 유저 검증 및 이미 있는 회원이라면 검증 처리
   */
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    Member member;
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String provider = userRequest.getClientRegistration().getRegistrationId();
    String providerId = oAuth2User.getAttribute("sub");
    String username = provider + "_" + providerId;
    String password = "";
    String email = oAuth2User.getAttribute("email");
    Role role = Role.USER;

    // 중복 회원 검사를 통해 이미 있다면 그 회원을 가져오고, 없다면 회원을 만들고 DB 에 저장한다.
    Optional<Member> findMember = loginRepository.findByUsername(username);

    if (findMember.isPresent()) { // 이미 있는 값이라면
      member = findMember.get();

    } else {
      member = Member.builder()
          .username(username)
          .password(password)
          .email(email)
          .role(role)
          .build();

      loginRepository.save(member);
    }

    return new MemberContext(member, oAuth2User.getAttributes());
  }

  private Member saveMemberInfo(OAuth2UserInfo userInfo, String provider, String providerId,
      String username) {
    String email = userInfo.getEmail();
    String password = "";
    Role role = Role.USER;

    return Member.builder()
        .username(username)
        .password(password)
        .email(email)
        .role(role)
        .build();
  }
}
