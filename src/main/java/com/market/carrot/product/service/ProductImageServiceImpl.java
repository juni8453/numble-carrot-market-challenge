package com.market.carrot.product.service;

import com.market.carrot.global.Exception.NotFoundEntityException;
import com.market.carrot.product.domain.ProductImage;
import com.market.carrot.product.domain.ProductImageRepository;
import com.market.carrot.product.dto.request.UpdateProductImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductImageServiceImpl implements ProductImageService {

  private final ProductImageRepository productImageRepository;

  @Transactional
  @Override
  public void updateImage(Long id, UpdateProductImageRequest imageRequest) {
    ProductImage findProductImage = productImageRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException("존재하지 않는 이미지입니다.", HttpStatus.BAD_REQUEST));

    findProductImage.updateProductImage(imageRequest);
  }
}
