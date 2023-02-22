package com.market.carrot.product.controller.dto.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateProductRequest {

  @NotNull(message = "카테고리를 공백으로 둘 수 없습니다.")
  private Long categoryId;

  @NotBlank(message = "상품 제목을 적어주세요.")
  private String title;

  @NotBlank(message = "상품 내용을 적어주세요.")
  private String content;

  @NotNull(message = "상품 가격을 적어주세요.")
  private int price;

  private List<ProductImageRequest> imagesUrl;

  private CreateProductRequest(Long categoryId, String title, String content, int price,
      List<ProductImageRequest> imagesUrl) {
    this.categoryId = categoryId;
    this.title = title;
    this.content = content;
    this.price = price;
    this.imagesUrl = imagesUrl;
  }

  public static CreateProductRequest testConstructor(Long categoryId, String title, String content, int price,
      List<ProductImageRequest> imagesUrl) {
    return new CreateProductRequest(categoryId, title, content, price, imagesUrl);
  }
}
