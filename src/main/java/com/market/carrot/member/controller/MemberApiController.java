package com.market.carrot.member.controller;

import com.market.carrot.GlobalResponseDto;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberApiController {

  private final MemberService memberService;

  @GetMapping("user/{id}")
  public GlobalResponseDto detail(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext memberContext) {

    log.info("ROLE_{}", memberContext.getMember().getRole());
    return memberService.detail(id);
  }

  @DeleteMapping("user/{id}")
  public GlobalResponseDto delete(@PathVariable Long id,
      @AuthenticationPrincipal MemberContext memberContext) {

    log.info("ROLE_{}", memberContext.getMember().getRole());
    return memberService.delete(id);
  }
}
