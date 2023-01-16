package com.market.carrot.product.dto.request;

import com.market.carrot.product.domain.ProductImage;
import java.util.List;
import lombok.Getter;

@Getter
public class UpdateProductRequest {

  // TODO Validation 필요
  // 수정 페이지 -> 원래 있던 값들이 다 들어가 있고, 자기가 원하는 값만 수정하도록 수정 페이지가 뜬다고 가정
  private String title;
  private String content;
  private int price;
  private List<ProductImage> imagesUrl;
}
