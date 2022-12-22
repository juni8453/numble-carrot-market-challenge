package com.market.carrot.security.login.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateMemberDto {

  private String username;
  private String password;
  private String phoneNumber;
  private String nickname;
  private String role = "ROLE_USER";
}
