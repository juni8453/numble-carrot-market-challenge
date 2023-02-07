package com.market.carrot.product.hateoas;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.market.carrot.product.dto.response.ProductResponse;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;

@Getter
public class ProductModel extends EntityModel<ProductResponse> {

  @JsonUnwrapped
  private final ProductResponse product;

  public ProductModel(ProductResponse product) {
    this.product = product;
  }
}
