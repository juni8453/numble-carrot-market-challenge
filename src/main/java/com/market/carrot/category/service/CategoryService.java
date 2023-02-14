package com.market.carrot.category.service;

import com.market.carrot.category.domain.dto.CreateCategoryRequest;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;

public interface CategoryService {

  void save(CreateCategoryRequest categoryRequest, MemberContext memberContext);
}
