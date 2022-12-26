package com.market.carrot.login.controller;

import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

  /**
   * 직접 전역적으로 저장된 Authentication 구현체 내부 유저 정보를 가져오던가,
   *
   * @AuthenticationPrincipal 어노테이션 사용해서 내부 유저 정보를 가져오던가 똑같다. 아무렇게나 조회할 수 있음. 왜냐하면 Authentication 객체
   * 내부에는 유저 정보 + 권한 정보가 있기 때문에 유저 정보를 뽑아 사용할 수 있는거고
   * @AuthenticationPrincipal 어노테이션을 사용하면 Authentication 객체를 설정한 유저 정보 객체로 다운 캐스팅해서 유저 정보를 뽑아 사용할 수
   * 있는 것이기 때문. 여기서는 내가 유저 정보를 MemberContext 객체에 저장했기 떄문에 MemberContext 객체를 활용해 정보를 조회했다. Form,
   * OAuth Login 통합 후 MemberContext(UserDetails 구현체) 를 통해 MemberContext 객체로 유저 정보를 호출할 수 있게 되었다.
   */
  @GetMapping("/test/login")
  public @ResponseBody
  String testLogin(
      Authentication authentication, @AuthenticationPrincipal MemberContext memberContext2) {
    MemberContext memberContext = (MemberContext) authentication.getPrincipal();
    System.out.println("authentication 내부 멤버 정보 확인 : " + memberContext.getMember());
    System.out.println("아이디 : " + memberContext.getMember().getUsername());
    System.out.println("비밀번호 : " + memberContext.getMember().getPassword());
    System.out.println("이메일 : " + memberContext.getMember().getEmail());
    System.out.println("권한 : " + memberContext.getMember().getRole());

    System.out.println("아이디 : " + memberContext2.getMember().getUsername());
    System.out.println("비밀번호 : " + memberContext2.getMember().getPassword());
    System.out.println("이메일 : " + memberContext2.getMember().getEmail());
    System.out.println("권한 : " + memberContext2.getMember().getRole());

    return "저장된 Authentication 구현체 정보 확인하기";
  }
}
