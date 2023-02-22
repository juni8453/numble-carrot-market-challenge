package com.market.carrot.product.controller.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductImageRequest {

  private String imageUrl;

  private ProductImageRequest(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public static ProductImageRequest testConstructor(String imageUrl) {
    return new ProductImageRequest(imageUrl);
  }
}
