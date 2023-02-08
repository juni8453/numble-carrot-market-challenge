package com.market.carrot.category.service;

import com.market.carrot.category.domain.Category;
import com.market.carrot.category.domain.CategoryRepository;
import com.market.carrot.category.domain.dto.CreateCategoryRequest;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Transactional
  @Override
  public void save(CreateCategoryRequest categoryRequest) {
    String name = categoryRequest.getName();
    Category saveCategory = Category.createCategory(name);

    categoryRepository.save(saveCategory);
  }
}
