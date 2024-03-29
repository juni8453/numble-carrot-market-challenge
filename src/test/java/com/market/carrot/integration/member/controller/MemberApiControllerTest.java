package com.market.carrot.integration.member.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.market.carrot.config.DatabaseCleanup;
import com.market.carrot.config.RestDocsConfig;
import com.market.carrot.config.WithMockCustomUser;
import com.market.carrot.global.Exception.ResponseMessage.ExceptionMessage;
import com.market.carrot.global.Exception.ResponseMessage.SuccessMessage;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Role;
import com.market.carrot.login.service.LoginService;
import com.market.carrot.member.domain.Member;
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
public class MemberApiControllerTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private MockMvc mvc;

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

  @DisplayName("비회원인경우 어떤 프로필도 조회하지 못하고 /loginForm 으로 302 Redirect 된다.")
  @Test
  void 비회원_프로필_조회() throws Exception {
    // when & then
    mvc.perform(get("/api/members/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }

  @DisplayName("회원이라면 자신의 프로필을 조회할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 회원_내_프로필_조회() throws Exception {
    // when & then
    mvc.perform(get("/api/members/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            SuccessMessage.SUCCESS_GET_MEMBER.getSuccessMessage()))

        .andDo(document("member/member/select-member",
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
            ),
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.OK),
                fieldWithPath("message").description(SuccessMessage.SUCCESS_GET_MEMBER),
                fieldWithPath("body").description("null"),

                fieldWithPath("body.id").description("회원 아이디"),
                fieldWithPath("body.username").description("회원 이름"),
                fieldWithPath("body.email").description("회원 이메일")
            ))
        );
  }

  @DisplayName("회원이지만 자신의 프로필을 조회하는게 아닌 경우 조회 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser", role = Role.USER)
  @Test
  void 회원_다른_회원의_프로필_조회() throws Exception {
    // when & then
    mvc.perform(get("/api/members/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andDo(document("member/member/select-member-is-not-mine",
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
            ),
            responseFields(
                fieldWithPath("code").description("응답 실패 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.BAD_REQUEST),
                fieldWithPath("message").description(ExceptionMessage.IS_NOT_MY_PROFILE),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("회원이라면 자신의 프로필을 삭제할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 회원_내_프로필_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/members/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            SuccessMessage.SUCCESS_DELETE_MEMBER.getSuccessMessage()))

        .andDo(document("member/member/delete-member",
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaTypes.HAL_JSON_VALUE)
            ),
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.OK),
                fieldWithPath("message").description(SuccessMessage.SUCCESS_DELETE_MEMBER),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("회원이지만 자신의 프로필을 삭제하는게 아닌 경우 삭제 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser", role = Role.USER)
  @Test
  void 회원_다른_회원_프로필_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/members/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(
            ExceptionMessage.IS_NOT_MY_PROFILE_BY_DELETE.getErrorMessage()))

        .andDo(document("member/member/delete-member-is-not-mine",
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
            ),
            responseFields(
                fieldWithPath("code").description("응답 실패 코드"),
                fieldWithPath("httpStatus").description("BAD_REQUEST"),
                fieldWithPath("message").description(ExceptionMessage.IS_NOT_MY_PROFILE_BY_DELETE),
                fieldWithPath("body").description("null")
            )
        ));
  }
}
