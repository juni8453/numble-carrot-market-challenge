package com.market.carrot.product.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.product.dto.response.ProductResponse;
import com.market.carrot.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {

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
}
