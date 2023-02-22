package com.market.carrot.product.controller.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

  private final Long id;
  private final String title;
  private final String content;
  private final int price;
  private final int heartCount;
  private final LocalDateTime createdDate;
  private final LocalDateTime modifiedDate;
  private final MemberByProductResponse member;
  private final CategoryByProductResponse category;
  private final ImagesResponse image;

  @Builder
  public ProductResponse(Long id, String title, String content, int price, int heartCount,
      LocalDateTime createdDate, LocalDateTime modifiedDate,
      MemberByProductResponse member,
      CategoryByProductResponse category,
      ImagesResponse image) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.price = price;
    this.heartCount = heartCount;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
    this.member = member;
    this.category = category;
    this.image = image;
  }
}
