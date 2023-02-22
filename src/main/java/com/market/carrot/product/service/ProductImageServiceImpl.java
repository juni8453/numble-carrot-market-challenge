package com.market.carrot.product.service;

import com.market.carrot.global.Exception.AnotherMemberException;
import com.market.carrot.global.Exception.ExceptionMessage;
import com.market.carrot.global.Exception.NotFoundEntityException;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.product.domain.Product;
import com.market.carrot.product.domain.ProductImage;
import com.market.carrot.product.domain.ProductImageRepository;
import com.market.carrot.product.controller.dto.request.UpdateProductImageRequest;
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
  public void updateImage(Long id, UpdateProductImageRequest imageRequest,
      MemberContext memberContext) {
    ProductImage findProductImage = productImageRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException(ExceptionMessage.NOT_FOUND_IMAGE,
            HttpStatus.BAD_REQUEST));

    Product findProductByImage = findProductImage.getProduct();

    if (!findProductByImage.checkUser(memberContext.getMember())) {
      throw new AnotherMemberException(ExceptionMessage.IS_NOT_WRITER, HttpStatus.BAD_REQUEST);
    }

    String updateImageUrl = imageRequest.getImageUrl();
    findProductImage.updateProductImage(updateImageUrl);
  }

  @Transactional
  @Override
  public void deleteImage(Long id, MemberContext memberContext) {
    ProductImage findProductImage = productImageRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException(ExceptionMessage.NOT_FOUND_IMAGE,
            HttpStatus.BAD_REQUEST));

    Product findProductByImage = findProductImage.getProduct();

    if (!findProductByImage.checkUser(memberContext.getMember())) {
      throw new AnotherMemberException(ExceptionMessage.IS_NOT_WRITER, HttpStatus.BAD_REQUEST);
    }

    productImageRepository.delete(findProductImage);
  }
}
