package com.market.carrot.login.controller;

import com.market.carrot.member.controller.dto.request.MemberCreateDto;
import com.market.carrot.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class LoginController {

  private final LoginService loginService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/")
  public @ResponseBody
  String index() {
    return "index";
  }

  // 로그인 폼
  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }

  // 회원가입 폼 (Form Login)
  @GetMapping("/joinForm")
  public String joinForm() {
    return "joinForm";
  }

  // 회원가입 처리
  @PostMapping("/join")
  public String join(MemberCreateDto memberCreateDto) {
    String encodedPassword = passwordEncoder.encode(memberCreateDto.getPassword());
    loginService.save(MemberCreateDto.toEntity(memberCreateDto, encodedPassword));

    return "redirect:/loginForm";
  }

  @GetMapping("/user")
  public @ResponseBody
  String user() {
    return "user";
  }

  @GetMapping("/member")
  public @ResponseBody
  String member() {
    return "member";
  }

  @GetMapping("/admin")
  public @ResponseBody
  String admin() {
    return "admin";
  }

  @Secured("ROLE_USER")
  @GetMapping("/info")
  public @ResponseBody
  String info() {
    return "개인정보";
  }
}
