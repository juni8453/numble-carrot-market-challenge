package com.market.carrot.login.config.customAuthentication.oauth2.providerType;

import com.market.carrot.login.config.customAuthentication.oauth2.userinfo.GitHubUserInfo;
import com.market.carrot.login.config.customAuthentication.oauth2.userinfo.OAuth2UserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class ProviderByGitHub implements CheckOAuthProvider {

  @Override
  public OAuth2UserInfo getUserInfo(OAuth2User oAuth2User) {
    return new GitHubUserInfo(oAuth2User.getAttributes());
  }
}
