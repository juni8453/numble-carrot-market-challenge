package com.market.carrot.login.config.customAuthentication.oauth2.userinfo;

public interface OAuth2UserInfo {

  String getProviderId();

  String getEmail();

  String getName();
}
