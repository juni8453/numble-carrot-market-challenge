package com.market.carrot.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String key;
}
