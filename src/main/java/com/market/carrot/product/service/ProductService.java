package com.market.carrot.product.service;

import com.market.carrot.product.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {

  List<ProductResponse> readAll();

}