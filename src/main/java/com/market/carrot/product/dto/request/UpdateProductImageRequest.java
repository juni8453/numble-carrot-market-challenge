package com.market.carrot.product.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateProductImageRequest {

  private String imageUrl;

  private UpdateProductImageRequest(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public static UpdateProductImageRequest testConstructor(String imageUrl) {
    return new UpdateProductImageRequest(imageUrl);
  }
}