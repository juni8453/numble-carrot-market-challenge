package com.market.carrot.product.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

  private final Long id;
  private final String title;
  private final String content;
  private final int price;
  private final int heartCount;
  private final MemberByProductResponse member;
  private final CategoryByProductResponse category;
  private final ImagesResponse images;

  @Builder
  public ProductResponse(Long id, String title, String content, int price, int heartCount,
      MemberByProductResponse member,
      CategoryByProductResponse category,
      ImagesResponse images) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.price = price;
    this.heartCount = heartCount;
    this.member = member;
    this.category = category;
    this.images = images;
  }
}
