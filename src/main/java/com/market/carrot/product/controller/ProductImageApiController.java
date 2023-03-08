package com.market.carrot.product.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.global.Exception.ResponseMessage.GlobalResponseMessage;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.product.controller.dto.request.UpdateProductImageRequest;
import com.market.carrot.product.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/image/", produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class ProductImageApiController {

  private final ProductImageService productImageService;

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("{id}")
  public GlobalResponseDto updateProductImage(
      @PathVariable Long id, @RequestBody UpdateProductImageRequest imageRequest,
      @AuthenticationPrincipal
          MemberContext memberContext) {

    productImageService.updateImage(id, imageRequest, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(GlobalResponseMessage.SUCCESS_POST_UPDATE_IMAGE.getSuccessMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("{id}")
  public GlobalResponseDto deleteProductImage(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext memberContext) {

    productImageService.deleteImage(id, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(GlobalResponseMessage.SUCCESS_DELETE_IMAGE.getSuccessMessage())
        .build();
  }
}