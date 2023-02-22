package com.market.carrot.member.controller.dto.request;

import com.market.carrot.login.domain.Role;
import com.market.carrot.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberCreateDto {

  private String username;
  private String password;
  private String email;

  public static Member toEntity(MemberCreateDto userCreateDto, String encodedPassword) {
    return Member.createMember(
        userCreateDto.getUsername(),
        encodedPassword,
        userCreateDto.getEmail(),
        Role.USER
    );
  }
}
