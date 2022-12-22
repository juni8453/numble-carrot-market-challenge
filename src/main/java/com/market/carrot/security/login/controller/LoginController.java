package com.market.carrot.security.login.controller;

import com.market.carrot.security.login.domain.Member;
import com.market.carrot.security.login.domain.dto.CreateMemberDto;
import com.market.carrot.security.login.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class LoginController {

  private final MemberService memberService;

  @GetMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/loginForm")
  public String loginForm() {
    return "login";
  }

  @GetMapping("/signinForm")
  public String signInForm() {
    return "signin";
  }

  @PostMapping("/signin")
  public String signin(CreateMemberDto createMemberDto) {
    String encodedPassword = memberService.passwordEncoder(createMemberDto.getPassword());
    Member member = Member.toEntity(createMemberDto, encodedPassword);
    memberService.createMember(member);

    return "redirect:/";
  }
}
