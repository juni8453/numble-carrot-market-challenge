package com.market.carrot.product.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateProductRequest {

  @NotBlank(message = "상품 제목을 적어주세요.")
  private String title;

  @NotBlank(message = "상품 내용을 적어주세요.")
  private String content;

  @NotNull(message = "상품 가격을 적어주세요.")
  private int price;

  private UpdateProductRequest(String title, String content, int price) {
    this.title = title;
    this.content = content;
    this.price = price;
  }

  public static UpdateProductRequest testConstructor(String title, String content, int price) {
    return new UpdateProductRequest(title, content, price);
  }
}
