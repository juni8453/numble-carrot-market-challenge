package com.market.carrot.likes.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.global.GlobalResponseMessage;
import com.market.carrot.likes.service.LikesService;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/")
@RestController
public class LikesController {

  private final LikesService likesService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("likes/{id}")
  GlobalResponseDto like(@PathVariable Long id, @AuthenticationPrincipal MemberContext member) {

    likesService.like(id, member);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.CREATED)
        .message(GlobalResponseMessage.SUCCESS_PRODUCT_LIKE.getSuccessMessage())
        .build();
  }
}
