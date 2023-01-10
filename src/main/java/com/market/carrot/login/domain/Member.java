package com.market.carrot.login.domain;

import com.market.carrot.BaseTime;
import java.io.Serializable;
import javax.persistence.Column;
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
public class Member extends BaseTime implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String username;

  private String password;

  @Column(length = 50, nullable = false)
  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Builder
  public Member(String username, String password, String email, Role role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }
}
