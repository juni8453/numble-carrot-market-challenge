package com.market.carrot.login.config.customAuthentication.oauth2;

import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.config.customAuthentication.oauth2.factory.OAuthFactory;
import com.market.carrot.login.config.customAuthentication.oauth2.providerType.CheckOAuthProvider;
import com.market.carrot.login.config.customAuthentication.oauth2.userinfo.OAuth2UserInfo;
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
  private Member member;

  /**
   * Form Login 에서 loadUserByUsername(username) 는 회원가입 처리가 아닌, DB 에서 회원을 찾아 해당 회원의 정보 + 권한을
   * Authentication 에 담는 역할 OAuth Login 에서 loadUser(userRequest) 는 강제 회원 가입, 중복 유저 검증 및 이미 있는 회원이라면
   * 검증 처리
   */
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String providerName = userRequest.getClientRegistration().getRegistrationId();
    CheckOAuthProvider OAuthType = checkType(providerName);
    OAuth2UserInfo userInfo = OAuthType.getUserInfo(oAuth2User);

    String providerId = userInfo.getProviderId();
    String username = providerName + "_" + providerId;
    String password = "";
    String email = userInfo.getEmail();
    Role role = Role.USER;

    Optional<Member> findMember = loginRepository.findByUsername(username);

    // 없다면 회원을 만들고 DB 에 저장 후 MemberContext 에 넣기위해 반환.
    if (findMember.isEmpty()) {
      member = saveMemberInfo(username, password, email, role);
    }

    // 이미 있다면 그 회원을 가져오고 member 변수에 저장
    findMember.ifPresent(value -> member = value);

    return new MemberContext(member, oAuth2User.getAttributes());
  }

  private CheckOAuthProvider checkType(String provider) {
    return new OAuthFactory().getProvider(provider);
  }

  private Member saveMemberInfo(String username, String password, String email, Role role) {
    Member member = Member.createMember(
        username, password, email, role);

    loginRepository.save(member);

    return member;
  }
}
