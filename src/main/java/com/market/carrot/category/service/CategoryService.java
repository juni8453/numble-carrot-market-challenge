package com.market.carrot.category.service;

import com.market.carrot.category.controller.dto.request.CreateCategoryRequest;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;

public interface CategoryService {

  void save(CreateCategoryRequest categoryRequest, MemberContext memberContext);
}
