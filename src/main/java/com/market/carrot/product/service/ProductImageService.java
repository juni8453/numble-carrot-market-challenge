package com.market.carrot.product.service;

import com.market.carrot.product.dto.request.UpdateProductImageRequest;

public interface ProductImageService {

  void updateImage(Long id, UpdateProductImageRequest imageRequest);
}
