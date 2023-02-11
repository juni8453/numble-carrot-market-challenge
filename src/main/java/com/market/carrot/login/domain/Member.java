package com.market.carrot.login.domain;

import com.market.carrot.BaseTime;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
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

  @Column(length = 50)
  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  public Member(String username, String password, String email, Role role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }

  public Member(Long id, String username, String password, String email, Role role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }

  public static Member createMember(String username, String password, String email, Role role) {
    return new Member(username, password, email, role);
  }

  public static Member testConstructor(Long id, String username, String password, String email,
      Role role) {
    return new Member(id, username, password, email, role);
  }

  public boolean checkUser(MemberContext memberContext) {
    if (this.id.equals(memberContext.getMember().getId())) {
      return true;
    }

    return false;
  }
}
