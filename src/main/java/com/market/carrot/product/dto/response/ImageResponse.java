package com.market.carrot.product.dto.response;

import com.market.carrot.product.domain.ProductImage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageResponse {

  private final Long id;
  private final String imageUrl;

  @Builder
  private ImageResponse(Long id, String imageUrl) {
    this.id = id;
    this.imageUrl = imageUrl;
  }

  public static ImageResponse from(ProductImage productImage) {
    return ImageResponse.builder()
        .id(productImage.getId())
        .imageUrl(productImage.getImageUrl())
        .build();
  }
}
