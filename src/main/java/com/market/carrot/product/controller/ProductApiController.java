package com.market.carrot.product.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.product.dto.request.CreateProductRequest;
import com.market.carrot.product.dto.request.UpdateProductRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/product/", produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class ProductApiController {

  private final ProductService productService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public GlobalResponseDto read(@AuthenticationPrincipal MemberContext member) {
    CollectionModel<ProductModel> productResponses = productService.readAll(member);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message("모든 제품 조회 성공")
        .body(productResponses)
        .build();
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("{id}")
  public GlobalResponseDto detail(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext member) {
    ProductModel productResponse = productService.detail(id, member);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message("단일 제품 조회 성공")
        .body(productResponse)
        .build();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public GlobalResponseDto save(@Valid @RequestBody CreateProductRequest productRequest,
      @AuthenticationPrincipal MemberContext member) {
    productService.save(productRequest, member);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.CREATED)
        .message("상품 등록 성공")
        .build();
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("{id}")
  public GlobalResponseDto update(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext member,
      @Valid @RequestBody UpdateProductRequest productRequest) {
    productService.update(id, productRequest, member);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message("상품 수정 성공")
        .build();
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("{id}")
  public GlobalResponseDto delete(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext member) {
    productService.delete(id, member);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message("상품 삭제 성공")
        .build();
  }
}
