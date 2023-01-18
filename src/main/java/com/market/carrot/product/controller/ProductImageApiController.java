package com.market.carrot.product.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.product.dto.request.UpdateProductImageRequest;
import com.market.carrot.product.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductImageApiController {

  private final ProductImageService productImageService;

  @PatchMapping("/image/{id}")
  public GlobalResponseDto updateProductImage(
      @PathVariable Long id, @RequestBody UpdateProductImageRequest imageRequest) {

    productImageService.updateImage(id, imageRequest);

    return GlobalResponseDto.builder()
        .code(1)
        .message("이미지 수정 성공")
        .build();
  }

  // TODO 이미지 삭제 구현 예정 (이미지 등록은 상품 등록 시 한번에 하도록 구현했기 때문에 따로 구현하지 않음)

}