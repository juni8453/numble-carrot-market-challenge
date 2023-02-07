package com.market.carrot.product.hateoas;

import com.market.carrot.product.controller.ProductApiController;
import com.market.carrot.product.dto.response.ProductResponse;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class ProductModelAssembler extends RepresentationModelAssemblerSupport<ProductResponse, ProductModel> {

  public ProductModelAssembler() {
    super(ProductApiController.class, ProductModel.class);
  }

  /**
   * self 자동 생성
   */
  @Override
  public ProductModel toModel(ProductResponse productResponse) {
    return createModelWithId(productResponse.getId(), productResponse);
  }

  @Override
  protected ProductModel instantiateModel(ProductResponse productResponse) {
    return new ProductModel(productResponse);
  }
}
