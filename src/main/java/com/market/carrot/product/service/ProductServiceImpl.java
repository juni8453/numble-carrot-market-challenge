package com.market.carrot.product.service;

import com.market.carrot.global.Exception.NotFoundEntityException;
import com.market.carrot.product.domain.Product;
import com.market.carrot.product.domain.ProductRepository;
import com.market.carrot.product.dto.response.CategoryByProductResponse;
import com.market.carrot.product.dto.response.ImagesResponse;
import com.market.carrot.product.dto.response.MemberByProductResponse;
import com.market.carrot.product.dto.response.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Transactional(readOnly = true)
  @Override
  public List<ProductResponse> readAll() {

    return productRepository.readAll().stream()
        .map(product -> ProductResponse.builder()
            .id(product.getId())
            .title(product.getTitle())
            .content(product.getContent())
            .price(product.getPrice())
            .heartCount(product.getHeartCount())
            .createdDate(product.getCreatedDate())
            .modifiedDate(product.getModifiedDate())
            .member(MemberByProductResponse.from(product))
            .category(CategoryByProductResponse.from(product))
            .images(ImagesResponse.from(product))
            .build())
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public ProductResponse detail(Long id) {
    Product findProduct = productRepository.findById(id)
        .orElseThrow(
            () -> new NotFoundEntityException("해당 제품이 존재하지 않습니다.", HttpStatus.BAD_REQUEST));

    return ProductResponse.builder()
        .id(findProduct.getId())
        .title(findProduct.getTitle())
        .content(findProduct.getContent())
        .price(findProduct.getPrice())
        .heartCount(findProduct.getHeartCount())
        .createdDate(findProduct.getCreatedDate())
        .modifiedDate(findProduct.getModifiedDate())
        .member(MemberByProductResponse.from(findProduct))
        .category(CategoryByProductResponse.from(findProduct))
        .images(ImagesResponse.from(findProduct))
        .build();
  }
}
