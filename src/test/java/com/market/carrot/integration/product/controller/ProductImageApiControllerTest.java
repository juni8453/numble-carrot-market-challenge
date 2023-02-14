package com.market.carrot.integration.product.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.market.carrot.product.dto.request.CreateProductRequest;
import com.market.carrot.product.dto.request.ProductImageRequest;
import com.market.carrot.product.dto.request.UpdateProductImageRequest;
import com.market.carrot.product.service.ProductService;
import java.util.List;
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
public class ProductImageApiControllerTest {

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

  @Autowired
  private ProductService productService;

  @Autowired
  private CategoryService categoryService;

  private final String accept = "application/hal+json;charset=UTF-8";

  @BeforeEach
  void saveProduct() {
    Member createMember = Member.testConstructor(
        1L, "username", "password", "email", Role.USER);
    MemberContext memberContext = new MemberContext(createMember);

    CreateProductRequest productRequest = getInitProduct();
    CreateCategoryRequest categoryRequest = getInitCategory();

    loginService.save(memberContext.getMember());
    categoryService.save(categoryRequest);
    productService.save(productRequest, memberContext);
  }

  @AfterEach
  void clearDB() {
    databaseCleanup.execute();
  }

  @DisplayName("비회원인 경우 상품 이미지 수정 API 를 호출하면 로그인 페이지로 Redirect 된다.")
  @Test
  void 비회원_상품_이미지_수정() throws Exception {
    // given
    UpdateProductImageRequest updateProductImageRequest = UpdateProductImageRequest.testConstructor(
        "Update Image");

    // when & then
    mvc.perform(post("/api/image/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(updateProductImageRequest)))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("회원이면서 자신이 등록한 상품인경우 이미지를 수정할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 회원_자신이_등록한_상품_이미지_수정() throws Exception {
    // given
    UpdateProductImageRequest updateProductImageRequest = UpdateProductImageRequest.testConstructor(
        "Update Image");

    // when & then
    mvc.perform(post("/api/image/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(updateProductImageRequest)))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_POST_UPDATE_IMAGE.getSuccessMessage()));
  }

  @DisplayName("회원이지만 자신이 등록한 상품이 아닌경우 수정 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser")
  @Test
  void 회원_자신이_등록하지_않은_상품_이미지_수정() throws Exception {
    // given
    UpdateProductImageRequest updateProductImageRequest = UpdateProductImageRequest.testConstructor(
        "Update Image");

    // when & then
    mvc.perform(post("/api/image/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(updateProductImageRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.IS_NOT_WRITER.getErrorMessage()));
  }

  @DisplayName("비회원인 경우 상품 이미지 삭제 API 를 호출하면 로그인 페이지로 Redirect 된다.")
  @Test
  void 비회원_상품_이미지_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/image/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("회원이면서 자신이 등록한 상품인경우 이미지를 삭제할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 회원_자신이_등록한_상품_이미지_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/image/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_DELETE_IMAGE.getSuccessMessage()));
  }

  @DisplayName("회원이지만 자신이 등록한 상품이 아닌경우 삭제 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser")
  @Test
  void 회원_자신이_등록하지_않은_상품_이미지_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/image/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.IS_NOT_WRITER.getErrorMessage()));
  }

  @DisplayName("존재하지 않는 이미지 수정 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 존재하지_않는_상품_이미지_수정() throws Exception {
    // given
    UpdateProductImageRequest updateProductImageRequest = UpdateProductImageRequest.testConstructor(
        "Update Image");

    // when & then
    mvc.perform(post("/api/image/3")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(updateProductImageRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.NOT_FOUND_IMAGE.getErrorMessage()));
  }

  @DisplayName("존재하지 않는 이미지 삭제 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 존재하지_않는_상품_이미지_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/image/3")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.NOT_FOUND_IMAGE.getErrorMessage()));
  }

  /**
   * private Method
   */
  private CreateProductRequest getInitProduct() {
    Long categoryId = 1L;
    String title = "Init Product Title";
    String content = "Init Product Content";
    int price = 10_000;

    List<ProductImageRequest> imagesUrl = List.of(
        ProductImageRequest.testConstructor("URL1"),
        ProductImageRequest.testConstructor("URL2")
    );

    return CreateProductRequest.testConstructor(categoryId, title, content, price, imagesUrl);
  }

  private CreateCategoryRequest getInitCategory() {
    String name = "Init Category Name";

    return CreateCategoryRequest.testConstructor(name);
  }
}
