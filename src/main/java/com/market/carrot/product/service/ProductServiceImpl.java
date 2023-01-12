package com.market.carrot.product.service;

import com.market.carrot.product.domain.ProductRepository;
import com.market.carrot.product.dto.response.CategoryByProductResponse;
import com.market.carrot.product.dto.response.ImagesResponse;
import com.market.carrot.product.dto.response.MemberByProductResponse;
import com.market.carrot.product.dto.response.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
}
