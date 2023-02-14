package com.market.carrot.category.service;

import com.market.carrot.category.domain.Category;
import com.market.carrot.category.domain.CategoryRepository;
import com.market.carrot.category.domain.dto.CreateCategoryRequest;
import com.market.carrot.global.Exception.ExceptionMessage;
import com.market.carrot.global.Exception.RoleException;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Transactional
  @Override
  public void save(CreateCategoryRequest categoryRequest, MemberContext memberContext) {
    String name = categoryRequest.getName();
    Category saveCategory = Category.createCategory(name);

    Role role = memberContext.getMember().getRole();
    if (role.getKey().equals("ROLE_USER")) {
      throw new RoleException(ExceptionMessage.INCORRECT_ROLE, HttpStatus.UNAUTHORIZED);
    }

    categoryRepository.save(saveCategory);
  }
}
