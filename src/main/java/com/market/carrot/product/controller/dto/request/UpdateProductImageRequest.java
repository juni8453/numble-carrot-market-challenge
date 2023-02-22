package com.market.carrot.product.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateProductImageRequest {

  @NotBlank(message = "이미지 URL 이 필요합니다.")
  private String imageUrl;

  private UpdateProductImageRequest(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public static UpdateProductImageRequest testConstructor(String imageUrl) {
    return new UpdateProductImageRequest(imageUrl);
  }
}