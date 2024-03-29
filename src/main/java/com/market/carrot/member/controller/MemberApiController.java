package com.market.carrot.member.controller;

import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.global.Exception.ResponseMessage.SuccessMessage;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.member.controller.dto.response.MemberResponse;
import com.market.carrot.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/members/")
@RestController
public class MemberApiController {

  private final MemberService memberService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("{id}")
  public GlobalResponseDto readMyProfile(@PathVariable Long id, @AuthenticationPrincipal
      MemberContext memberContext) {
    MemberResponse memberResponse = memberService.readDetail(id, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(SuccessMessage.SUCCESS_GET_MEMBER.getSuccessMessage())
        .body(memberResponse)
        .build();
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("{id}")
  public GlobalResponseDto delete(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext memberContext) {
    memberService.delete(id, memberContext);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(SuccessMessage.SUCCESS_DELETE_MEMBER.getSuccessMessage())
        .build();
  }
}
