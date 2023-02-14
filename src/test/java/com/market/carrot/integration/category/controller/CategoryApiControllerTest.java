package com.market.carrot.integration.category.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.carrot.category.domain.dto.CreateCategoryRequest;
import com.market.carrot.category.service.CategoryService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class CategoryApiControllerTest {

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

  private final String accept = "application/hal+json;charset=UTF-8";

  @BeforeEach
  void saveProduct() {
    Member createMember = Member.testConstructor(
        1L, "username", "password", "email", Role.ADMIN);
    MemberContext memberContext = new MemberContext(createMember);

    loginService.save(memberContext.getMember());
  }

  @AfterEach
  void clearDB() {
    databaseCleanup.execute();
  }

  @DisplayName("비회원인 경우, 카테고리 생성 API 를 호출한다면 로그인 페이지로 Redirect 된다.")
  @Test
  void 비회원_카테고리_생성() throws Exception {
    // given
    CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.testConstructor(
        "Category Name");

    // when & then
    mvc.perform(post("/api/category")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(createCategoryRequest)))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("회원이지만 USER 권한인 경우, 카테고리 생성 API 를 호출한다면 401 예외가 발생한다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 회원_카테고리_생성() throws Exception {
    // given
    CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.testConstructor(
        "Category Name");

    // when & then
    mvc.perform(post("/api/category/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(createCategoryRequest)))
        .andDo(print())
        .andExpect(status().isUnauthorized())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.UNAUTHORIZED.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.INCORRECT_ROLE.getErrorMessage()));
  }

  @DisplayName("회원이면서 ADMIN 권한인 경우, 카테고리 생성 API 를 호출하면 카테고리가 하나 생성된다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.ADMIN)
  @Test
  void 관리자_카테고리_생성() throws Exception {
    // given
    CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.testConstructor(
        "Category Name");

    // when & then
    mvc.perform(post("/api/category/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(createCategoryRequest)))
        .andDo(print())
        .andExpect(status().isCreated())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.CREATED.name()))
        .andExpect(jsonPath("message").value(GlobalResponseMessage.SUCCESS_POST_CATEGORY.getSuccessMessage()));
  }
}
