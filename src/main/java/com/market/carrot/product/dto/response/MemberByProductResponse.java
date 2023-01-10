package com.market.carrot.product.dto.response;

import com.market.carrot.login.domain.Role;
import com.market.carrot.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberByProductResponse {

  private final Long id;
  private final String username;
  private final String email;
  private final Role role;

  @Builder
  private MemberByProductResponse(Long id, String username, String email,
      Role role) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
  }

  public static MemberByProductResponse from(Product product) {
    return MemberByProductResponse.builder()
        .id(product.getMember().getId())
        .username(product.getMember().getUsername())
        .email(product.getMember().getEmail())
        .role(product.getMember().getRole())
        .build();
  }
}
