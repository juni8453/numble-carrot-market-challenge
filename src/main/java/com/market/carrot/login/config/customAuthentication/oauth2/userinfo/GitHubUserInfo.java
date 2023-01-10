package com.market.carrot.login.config.customAuthentication.oauth2.userinfo;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GitHubUserInfo implements OAuth2UserInfo {

  private final Map<String, Object> attributes;

  @Override
  public String getProviderId() {
    return String.valueOf(attributes.get("id"));
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }

  @Override
  public String getName() {
    return (String) attributes.get("name");
  }
}
