package com.market.carrot.login.config.customAuthentication.oauth2.providerType;

import com.market.carrot.login.config.customAuthentication.oauth2.userinfo.GoogleUserInfo;
import com.market.carrot.login.config.customAuthentication.oauth2.userinfo.OAuth2UserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class ProviderByGoogle implements CheckOAuthProvider {

  @Override
  public OAuth2UserInfo getUserInfo(OAuth2User oAuth2User) {
    return new GoogleUserInfo(oAuth2User.getAttributes());
  }
}
