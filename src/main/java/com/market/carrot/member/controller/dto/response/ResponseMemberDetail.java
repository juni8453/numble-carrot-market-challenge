package com.market.carrot.member.controller.dto.response;

import com.market.carrot.login.domain.Role;
import com.market.carrot.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseMemberDetail {

  private final String username;
  private final String email;
  private final Role role;
  private final LocalDateTime createdDate;
  private final LocalDateTime modifiedDate;

  @Builder
  private ResponseMemberDetail(String username, String email, Role role,
      LocalDateTime createdDate, LocalDateTime modifiedDate) {
    this.username = username;
    this.email = email;
    this.role = role;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
  }

  public static ResponseMemberDetail of(Member member) {
    return ResponseMemberDetail.builder()
        .username(member.getUsername())
        .email(member.getEmail())
        .role(member.getRole())
        .createdDate(member.getCreatedDate())
        .modifiedDate(member.getModifiedDate())
        .build();
  }
}
