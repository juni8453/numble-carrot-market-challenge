package com.market.carrot.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
  private final Long id;
  private final String username;
  private final String email;

  @Builder
  public MemberResponse(Long id, String username, String email) {
    this.id = id;
    this.username = username;
    this.email = email;
  }
}
