package com.market.carrot.login.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCreateDto {

  private String username;
  private String password;
  private String email;

  public static User toEntity(UserCreateDto userCreateDto, String encodedPassword) {
    return User.builder()
        .username(userCreateDto.getUsername())
        .password(encodedPassword)
        .email(userCreateDto.getEmail())
        .role(Role.USER)
        .build();
  }
}
