package com.market.carrot.product.dto.response;

import com.market.carrot.product.domain.Product;
import com.market.carrot.product.domain.ProductImage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImagesResponse {

  private final List<ImageResponse> images;

  @Builder
  private ImagesResponse(List<ImageResponse> images) {
    this.images = images;
  }

  public static ImagesResponse from(Product product) {
    List<ProductImage> productImage = product.getProductImage();

    List<ImageResponse> imageResponses = productImage.stream()
        .map(ImageResponse::from)
        .collect(Collectors.toList());

    return ImagesResponse.builder()
        .images(imageResponses)
        .build();
  }
}
