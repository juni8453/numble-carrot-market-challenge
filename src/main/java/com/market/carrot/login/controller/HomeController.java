package com.market.carrot.login.controller;

import com.market.carrot.login.domain.LoginService;
import com.market.carrot.login.domain.User;
import com.market.carrot.login.domain.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class HomeController {

  private final LoginService loginService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/")
  public @ResponseBody String index() {
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
  public String join(UserCreateDto userCreateDto) {
    String encodedPassword = passwordEncoder.encode(userCreateDto.getPassword());
    loginService.save(UserCreateDto.toEntity(userCreateDto, encodedPassword));

    return "redirect:/loginForm";
  }

  @GetMapping("/user")
  public @ResponseBody String user() {
    return "user";
  }

  @GetMapping("/admin")
  public @ResponseBody String admin() {
    return "admin";
  }
}
