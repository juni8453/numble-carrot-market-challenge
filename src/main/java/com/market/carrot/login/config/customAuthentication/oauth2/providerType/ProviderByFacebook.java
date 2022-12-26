package com.market.carrot.login.config.customAuthentication.oauth2.providerType;

import com.market.carrot.login.config.customAuthentication.oauth2.userinfo.FacebookUserInfo;
import com.market.carrot.login.config.customAuthentication.oauth2.userinfo.OAuth2UserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class ProviderByFacebook implements CheckOAuthProvider {

  @Override
  public OAuth2UserInfo getUserInfo(OAuth2User oAuth2User) {
    return new FacebookUserInfo(oAuth2User.getAttributes());
  }
}
