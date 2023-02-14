package com.market.carrot.likes.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.global.GlobalResponseMessage;
import com.market.carrot.likes.service.LikesService;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/likes/", produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class LikesApiController {

  private final LikesService likesService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("{productId}")
  GlobalResponseDto like(@PathVariable Long productId, @AuthenticationPrincipal MemberContext memberContext) {

    likesService.like(productId, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.CREATED)
        .message(GlobalResponseMessage.SUCCESS_POST_PRODUCT_LIKE.getSuccessMessage())
        .build();
  }
}
