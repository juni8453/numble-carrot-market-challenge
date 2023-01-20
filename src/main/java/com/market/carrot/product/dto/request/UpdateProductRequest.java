package com.market.carrot.product.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateProductRequest {

  @NotBlank(message = "상품 제목을 적어주세요.")
  private String title;

  @NotBlank(message = "상품 내용을 적어주세요.")
  private String content;

  @NotNull(message = "상품 가격을 적어주세요.")
  private int price;
}
