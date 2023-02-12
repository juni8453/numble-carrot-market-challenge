package com.market.carrot.product.service;

import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.product.dto.request.CreateProductRequest;
import com.market.carrot.product.dto.request.UpdateProductRequest;
import com.market.carrot.product.hateoas.ProductModel;
import org.springframework.hateoas.CollectionModel;

public interface ProductService {

  CollectionModel<ProductModel> readAll(MemberContext memberContext);

  ProductModel detail(Long id, MemberContext memberContext);

  void save(CreateProductRequest productRequest, MemberContext memberContext);

  void update(Long id, UpdateProductRequest productRequest, MemberContext memberContext);

  void delete(Long id, MemberContext memberContext);
}
