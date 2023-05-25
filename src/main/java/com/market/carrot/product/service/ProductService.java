package com.market.carrot.product.service;

import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.product.controller.dto.request.CreateProductRequest;
import com.market.carrot.product.controller.dto.request.UpdateProductRequest;
import com.market.carrot.product.controller.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {

  List<ProductResponse> readAll(MemberContext memberContext);

  ProductResponse readDetail(Long id, MemberContext memberContext);

  void save(CreateProductRequest productRequest, MemberContext memberContext);

  void update(Long id, UpdateProductRequest productRequest, MemberContext memberContext);

  void delete(Long id, MemberContext memberContext);
}
