package com.market.carrot.product.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.product.dto.request.UpdateProductImageRequest;
import com.market.carrot.product.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/image", produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class ProductImageApiController {

  private final ProductImageService productImageService;

  @PostMapping("/{id}")
  public GlobalResponseDto updateProductImage(
      @PathVariable Long id, @RequestBody UpdateProductImageRequest imageRequest) {

    productImageService.updateImage(id, imageRequest);

    return GlobalResponseDto.builder()
        .code(1)
        .message("이미지 수정 성공")
        .build();
  }

  @DeleteMapping("/{id}")
  public GlobalResponseDto deleteProductImage(@PathVariable Long id) {

    productImageService.deleteImage(id);

    return GlobalResponseDto.builder()
        .code(1)
        .message("이미지 삭제 성공")
        .build();
  }
}