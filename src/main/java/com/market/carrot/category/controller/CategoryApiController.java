package com.market.carrot.category.controller;

import com.market.carrot.category.controller.dto.request.CreateCategoryRequest;
import com.market.carrot.category.service.CategoryService;
import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.global.Exception.ResponseMessage.SuccessMessage;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/category/", produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class CategoryApiController {

  private final CategoryService categoryService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  GlobalResponseDto save(@RequestBody CreateCategoryRequest createCategoryRequest,
      @AuthenticationPrincipal MemberContext memberContext) {

    categoryService.save(createCategoryRequest, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.CREATED)
        .message(SuccessMessage.SUCCESS_POST_CATEGORY.getSuccessMessage())
        .build();
  }
}
