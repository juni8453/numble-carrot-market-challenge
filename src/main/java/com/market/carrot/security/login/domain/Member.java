package com.market.carrot.security.login.domain;

import com.market.carrot.security.login.domain.dto.CreateMemberDto;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String password;

  private String phoneNumber;

  private String nickname;

  private String role;

  @Builder
  public Member(String username, String password, String phoneNumber, String nickname, String role) {
    this.username = username;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.nickname = nickname;
    this.role = role;
  }

  public static Member toEntity(CreateMemberDto createMemberDto, String encodedPassword) {
    return Member.builder()
        .username(createMemberDto.getUsername())
        .password(encodedPassword)
        .phoneNumber(createMemberDto.getPhoneNumber())
        .nickname(createMemberDto.getNickname())
        .role(createMemberDto.getRole())
        .build();
  }
}
