package com.market.carrot.integration.category.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.carrot.category.controller.dto.request.CreateCategoryRequest;
import com.market.carrot.config.DatabaseCleanup;
import com.market.carrot.config.RestDocsConfig;
import com.market.carrot.config.WithMockCustomUser;
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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
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
    mvc.perform(post("/api/category/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(createCategoryRequest)))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().exists(HttpHeaders.LOCATION))

        .andDo(document("category/guest/insert-category",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                headerWithName(HttpHeaders.ACCEPT).description(MediaTypes.HAL_JSON_VALUE)
            ),
            responseHeaders(
                headerWithName(HttpHeaders.LOCATION).description("/loginForm")
            )
        ));
  }

  @DisplayName("회원이지만 USER 권한인 경우, 카테고리 생성 API 를 호출한다면 403 예외가 발생한다.")
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
        .andExpect(status().isForbidden())

        .andDo(document("category/member/insert-category",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                headerWithName(HttpHeaders.ACCEPT).description(MediaTypes.HAL_JSON_VALUE)
            )
        ));
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
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_POST_CATEGORY.getSuccessMessage()))

        .andDo(document("category/admin/insert-category",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                headerWithName(HttpHeaders.ACCEPT).description(MediaTypes.HAL_JSON_VALUE)
            ),
            requestFields(
                fieldWithPath("name").description("카테고리 이름")
            ),
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaTypes.HAL_JSON_VALUE)
            ),
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.CREATED),
                fieldWithPath("message").description(GlobalResponseMessage.SUCCESS_POST_CATEGORY),
                fieldWithPath("body").description("null")
            )
        ));
  }
}
