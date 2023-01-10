package com.market.carrot.product.dto.response;

import com.market.carrot.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryByProductResponse {

  private final Long id;
  private final String name;

  @Builder
  private CategoryByProductResponse(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public static CategoryByProductResponse from(Product product) {
    return CategoryByProductResponse.builder()
        .id(product.getCategory().getId())
        .name(product.getCategory().getName())
        .build();
  }
}
