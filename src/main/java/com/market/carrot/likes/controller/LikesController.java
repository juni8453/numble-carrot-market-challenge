package com.market.carrot.likes.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.likes.service.LikesService;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class LikesController {

  private final LikesService likesService;

  @PostMapping("/likes/{id}")
  GlobalResponseDto like(@PathVariable Long id, @AuthenticationPrincipal MemberContext member) {

    likesService.like(id, member);

    return GlobalResponseDto.builder()
        .code(1)
        .message(id + "번 제품 좋아요 API 호출 성공")
        .build();
  }
}
