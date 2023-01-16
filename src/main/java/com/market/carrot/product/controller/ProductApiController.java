package com.market.carrot.product.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.product.dto.request.CreateProductRequest;
import com.market.carrot.product.dto.response.ProductResponse;
import com.market.carrot.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductApiController {

  private final ProductService productService;

  @GetMapping("/product")
  public GlobalResponseDto read() {
    List<ProductResponse> productResponses = productService.readAll();

    return GlobalResponseDto.builder()
        .code(1)
        .message("모든 제품 조회 성공")
        .body(productResponses)
        .build();
  }

  @GetMapping("/product/{id}")
  public GlobalResponseDto detail(@PathVariable Long id) {
    ProductResponse productResponse = productService.detail(id);

    return GlobalResponseDto.builder()
        .code(1)
        .message("단일 제품 조회 성공")
        .body(productResponse)
        .build();
  }

  @PostMapping("/product")
  public GlobalResponseDto save(@RequestBody CreateProductRequest productRequest,
      @AuthenticationPrincipal MemberContext member) {
    productService.save(productRequest, member);

    return GlobalResponseDto.builder()
        .code(1)
        .message("상품 등록 성공")
        .build();
  }
}
