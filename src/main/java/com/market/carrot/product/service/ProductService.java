package com.market.carrot.product.service;

import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.product.dto.request.CreateProductRequest;
import com.market.carrot.product.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {

  List<ProductResponse> readAll();

  ProductResponse detail(Long id);

  void save(CreateProductRequest productRequest, MemberContext member);

}
