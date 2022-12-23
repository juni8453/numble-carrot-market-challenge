package com.market.carrot.login.domain;

import com.market.carrot.BaseTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Builder
  public User(String username, String password, String email, Role role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }
}
