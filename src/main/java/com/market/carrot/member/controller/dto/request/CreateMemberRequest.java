package com.market.carrot.member.controller.dto.request;

import com.market.carrot.login.domain.Role;
import com.market.carrot.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateMemberRequest {

  private String username;
  private String password;
  private String email;

  public static Member toEntity(CreateMemberRequest createMemberRequest, String encodedPassword) {
    return Member.createMember(
        createMemberRequest.getUsername(),
        encodedPassword,
        createMemberRequest.getEmail(),
        Role.USER
    );
  }
}
