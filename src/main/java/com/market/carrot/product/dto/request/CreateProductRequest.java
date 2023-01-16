package com.market.carrot.product.dto.request;

import com.market.carrot.product.domain.ProductImage;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateProductRequest {

  // TODO : Validation 필요
  private Long categoryId;
  private String title;
  private String content;
  private int price;
  private List<ProductImage> imagesUrl;
}
