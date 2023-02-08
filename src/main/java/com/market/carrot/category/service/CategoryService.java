package com.market.carrot.category.service;

import com.market.carrot.category.domain.dto.CreateCategoryRequest;

public interface CategoryService {

  void save(CreateCategoryRequest categoryRequest);
}
