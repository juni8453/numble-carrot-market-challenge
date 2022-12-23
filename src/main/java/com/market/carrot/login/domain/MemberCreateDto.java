package com.market.carrot.login.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberCreateDto {

  private String username;
  private String password;
  private String email;

  public static Member toEntity(MemberCreateDto userCreateDto, String encodedPassword) {
    return Member.builder()
        .username(userCreateDto.getUsername())
        .password(encodedPassword)
        .email(userCreateDto.getEmail())
        .role(Role.USER)
        .build();
  }
}
