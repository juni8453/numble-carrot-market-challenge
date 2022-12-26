package com.market.carrot.login.config.customAuthentication.provider;

public interface OAuth2UserInfo {

  String getProviderId();

  String getProvider();

  String getEmail();

  String getName();
}
