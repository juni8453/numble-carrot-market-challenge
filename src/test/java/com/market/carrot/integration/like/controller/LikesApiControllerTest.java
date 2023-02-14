package com.market.carrot.integration.like.controller;

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
import com.market.carrot.likes.service.LikesService;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Member;
import com.market.carrot.login.domain.Role;
import com.market.carrot.login.service.LoginService;
import com.market.carrot.product.dto.request.CreateProductRequest;
import com.market.carrot.product.dto.request.ProductImageRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class LikesApiControllerTest {

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

  @Autowired
  private LikesService likesService;

  private final String accept = "application/hal+json;charset=UTF-8";

  @BeforeEach
  void saveProduct() {
    Member createMember = Member.testConstructor(
        1L, "username", "password", "email", Role.ADMIN);
    MemberContext memberContext = new MemberContext(createMember);

    CreateProductRequest productRequest = getInitProduct();
    CreateCategoryRequest categoryRequest = getInitCategory();

    loginService.save(memberContext.getMember());
    categoryService.save(categoryRequest, memberContext);
    productService.save(productRequest, memberContext);
  }

  @AfterEach
  void clearDB() {
    databaseCleanup.execute();
  }

  @DisplayName("비회원인 경우 좋아요 API 를 호출하면 로그인 페이지로 Redirect 된다.")
  @Test
  void 비회원_상품_좋아요() throws Exception {
    // when & then
    mvc.perform(post("/api/likes/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("존재하지 않는 상품 좋아요 API 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 존재하지_않는_상품_좋아요() throws Exception {
    // when & then
    mvc.perform(post("/api/likes/2")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(
            ExceptionMessage.NOT_FOUND_PRODUCT.getErrorMessage()));
  }

  @DisplayName("회원인 경우, 좋아요를 누르지 않은 상품에 대해 좋아요 API 호출 시 해당 상품에 대한 좋아요가 생성된다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 회원_좋아요_눌리지_않은_상품_좋아요() throws Exception {
    // when & then
    mvc.perform(post("/api/likes/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isCreated())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.CREATED.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_POST_PRODUCT_LIKE.getSuccessMessage()));
  }

  @DisplayName("회원인 경우, 좋아요를 이미 누른 상품에 대해 좋아요 API 호출 시 또한 성공적으로 호출된다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 회원_이미_좋아요_눌린_상품_좋아요() throws Exception {
    // given
    MemberContext memberContext = (MemberContext) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
    likesService.like(1L, memberContext);

    // when & then
    mvc.perform(post("/api/likes/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isCreated());
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
