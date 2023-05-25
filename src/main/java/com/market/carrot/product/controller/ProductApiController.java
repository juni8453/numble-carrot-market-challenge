package com.market.carrot.product.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.global.Exception.ResponseMessage.SuccessMessage;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.product.controller.dto.request.CreateProductRequest;
import com.market.carrot.product.controller.dto.request.UpdateProductRequest;
import com.market.carrot.product.controller.dto.response.ProductResponse;
import com.market.carrot.product.hateoas.ProductModel;
import com.market.carrot.product.service.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/products/")
@RestController
public class ProductApiController {

  private final ProductService productService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public GlobalResponseDto read(@AuthenticationPrincipal MemberContext memberContext) {
    CollectionModel<ProductModel> productResponses = productService.readAll(memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(SuccessMessage.SUCCESS_GET_PRODUCTS.getSuccessMessage())
        .body(productResponses)
        .build();
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("{id}")
  public GlobalResponseDto detail(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext memberContext) {
    ProductResponse productResponse = productService.readDetail(id, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(SuccessMessage.SUCCESS_GET_PRODUCT.getSuccessMessage())
        .body(productResponse)
        .build();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public GlobalResponseDto save(@Valid @RequestBody CreateProductRequest productRequest,
      @AuthenticationPrincipal MemberContext memberContext) {
    productService.save(productRequest, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.CREATED)
        .message(SuccessMessage.SUCCESS_POST_INSERT_PRODUCT.getSuccessMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("{id}")
  public GlobalResponseDto update(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext memberContext,
      @Valid @RequestBody UpdateProductRequest productRequest) {
    productService.update(id, productRequest, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(SuccessMessage.SUCCESS_POST_UPDATE_PRODUCT.getSuccessMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("{id}")
  public GlobalResponseDto delete(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext memberContext) {
    productService.delete(id, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(SuccessMessage.SUCCESS_DELETE_PRODUCT.getSuccessMessage())
        .build();
  }
}
