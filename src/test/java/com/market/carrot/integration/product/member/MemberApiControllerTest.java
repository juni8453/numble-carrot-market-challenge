package com.market.carrot.integration.product.member;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.carrot.config.DatabaseCleanup;
import com.market.carrot.config.WithMockCustomUser;
import com.market.carrot.global.Exception.ExceptionMessage;
import com.market.carrot.global.GlobalResponseMessage;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Member;
import com.market.carrot.login.domain.Role;
import com.market.carrot.login.service.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class MemberApiControllerTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private MockMvc mvc;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private DatabaseCleanup databaseCleanup;

  @Autowired
  private LoginService loginService;

  @BeforeEach
  void saveMember() {
    Member createMember = Member.testConstructor(
        1L, "username", "password", "email", Role.USER);
    MemberContext memberContext = new MemberContext(createMember);

    loginService.save(memberContext.getMember());
  }

  @AfterEach
  void clearDB() {
    databaseCleanup.execute();
  }

  private final String accept = "application/hal+json;charset=UTF-8";

  @DisplayName("비회원인경우 어떤 프로필도 조회하지 못하고 /loginForm 으로 302 Redirect 된다.")
  @Test
  void 비회원_프로필_조회() throws Exception {
    // when & then
    mvc.perform(get("/api/user/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("회원이면서 자신의 프로필을 조회한다면 self, update, delete link 모두 응답에 포함되어야한다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 회원_내_프로필_조회() throws Exception {
    // when & then
    mvc.perform(get("/api/user/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_GET_MEMBER.getSuccessMessage()));
  }

  @DisplayName("회원이지만 자신의 프로필을 조회하는게 아닌 경우 조회 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser")
  @Test
  void 회원_다른_회원의_프로필_조회() throws Exception {
    // when & then
    mvc.perform(get("/api/user/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @DisplayName("회원이라면 자신의 프로필을 삭제할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 회원_내_프로필_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/user/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_DELETE_MEMBER.getSuccessMessage()));
  }

  @DisplayName("회원이지만 자신의 프로필을 삭제하는게 아닌 경우 삭제 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser")
  @Test
  void 회원_다른_회원_프로필_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/user/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(
            ExceptionMessage.IS_NOT_MY_PROFILE_BY_DELETE.getErrorMessage()));
  }
}
