package com.market.carrot.integration.product.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.market.carrot.product.dto.request.UpdateProductRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class ProductApiControllerTest {

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

  @DisplayName("CreateProductRequest 를 받아 상품을 등록할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 상품_등록() throws Exception {
    CreateProductRequest createProductRequest = getCreateProductRequest();

    // when & then
    mvc.perform(post("/api/product/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(createProductRequest)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, accept))

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.CREATED.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_POST_INSERT_PRODUCT.getSuccessMessage()));
  }

  @DisplayName("상품 등록 시 CreateProductRequest 에 이미지가 없다면 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 이미지없이_상품_등록() throws Exception {
    // given
    CreateProductRequest notIncludedProductRequest = getNotIncludedProductRequest();

    // when & then
    mvc.perform(post("/api/product/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(notIncludedProductRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(
            ExceptionMessage.IS_NOT_INCLUDED_IMAGE.getErrorMessage()));
  }


  @DisplayName("비회원인 경우 단일 상품을 조회했을 때 self link 만 응답에 포함되어야한다.")
  @Test
  void 비회원_단일_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/product/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, accept))

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_GET_PRODUCT.getSuccessMessage()))

        .andExpect(jsonPath("body.links[0].rel").value("self"))
        .andExpect(jsonPath("body.links[0].rel").value("self"))
        .andExpect(jsonPath("body.links[1].rel").doesNotExist())
        .andExpect(jsonPath("body.links[2].rel").doesNotExist());
  }

  @DisplayName("회원이지만 자신이 등록한 상품이 아닌 경우 self link 만 응답에 포함되어야한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser")
  @Test
  void 자신이_등록한_상품이_이닌경우_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/product/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_GET_PRODUCT.getSuccessMessage()))

        .andExpect(jsonPath("body.links[0].rel").exists())
        .andExpect(jsonPath("body.links[0].rel").value("self"));
  }

  @DisplayName("회원이면서 자신이 등록한 상품인 경우 self, update, delete link 모두 응답에 포함되어야한다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 자신이_등록한_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/product/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_GET_PRODUCT.getSuccessMessage()))

        .andExpect(jsonPath("body.links[0].rel").exists())
        .andExpect(jsonPath("body.links[0].rel").value("self"))
        .andExpect(jsonPath("body.links[1].rel").exists())
        .andExpect(jsonPath("body.links[1].rel").value("product-delete"))
        .andExpect(jsonPath("body.links[2].rel").exists())
        .andExpect(jsonPath("body.links[2].rel").value("product-update"))
    ;
  }

  @DisplayName("비회원인 경우 content 내부 각 상품의 단일 상품 조회 link 만 응답에 포함되어야한다.")
  @Test
  void 비회원_모든_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/product/")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_GET_PRODUCTS.getSuccessMessage()))

        .andExpect(jsonPath("body.content[0].links[0].rel").exists())
        .andExpect(jsonPath("body.content[0].links[0].rel").value("self"))
        .andExpect(jsonPath("body.links[0]").doesNotExist());
  }

  @DisplayName("회원일 경우 content 내부 각 상품의 단일 상품 조회 link, 바깥쪽 links[] 에 상품 등록 link 가 응답에 포함되어야한다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 회원_모든_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/product/")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_GET_PRODUCTS.getSuccessMessage()))

        .andExpect(jsonPath("body.content[0].links[0].rel").value("self"))
        .andExpect(jsonPath("body.links[0].rel").exists())
        .andExpect(jsonPath("body.links[0].rel").value("product-save"));
  }

  @DisplayName("비회원인 경우 수정 API 를 호출하면 로그인 페이지로 Redirect 된다.")
  @Test
  void 비회원_상품_수정() throws Exception {
    // when & then
    mvc.perform(post("/api/product/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("회원일 경우 자신이 등록한 상품을 수정할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username")
  @Test
  void 회원_상품_수정() throws Exception {
    // given
    UpdateProductRequest updateProductRequest = getUpdateProductRequest();

    // when & then
    mvc.perform(post("/api/product/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(updateProductRequest)))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_POST_UPDATE_PRODUCT.getSuccessMessage()));
  }

  @DisplayName("회원이지만 자신이 등록한 상품이 아닌 경우 수정 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser")
  @Test
  void 자신이_등록한_상품이_아닌경우_상품수정_400() throws Exception {
    // given
    // anotherUser 에 userId = 2를 설정하고 저장해야만 본 로직 부분에서 NOT_FOUND_MEMBER 예외가 발생하지 않는다.
    MemberContext memberContext = (MemberContext) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
    loginService.save(memberContext.getMember());

    UpdateProductRequest updateProductRequest = getUpdateProductRequest();

    //when & then
    mvc.perform(post("/api/product/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(updateProductRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.IS_NOT_WRITER.getErrorMessage()));
  }

  @DisplayName("비회원인 경우 삭제 API 를 호출하면 로그인 페이지로 Redirect 된다.")
  @Test
  void 비회원_상품_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/product/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().exists(HttpHeaders.LOCATION));
  }

  @DisplayName("회원일 경우 자신이 등록한 상품을 삭제할 수 있다.")
  @WithMockCustomUser
  @Test
  void 회원_상품_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/product/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            GlobalResponseMessage.SUCCESS_DELETE_PRODUCT.getSuccessMessage()));
  }

  @DisplayName("회원이지만 자신이 등록한 상품이 아닌 경우 삭제 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser")
  @Test
  void 자신이_등록한_상품이_아닌경우_상품삭제_400() throws Exception {
    // given
    // anotherUser 에 userId = 2를 설정하고 저장해야만 본 로직 부분에서 NOT_FOUND_MEMBER 예외가 발생하지 않는다.
    MemberContext memberContext = (MemberContext) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
    loginService.save(memberContext.getMember());

    // when & then
    mvc.perform(delete("/api/product/1")
            .with(csrf())
            .accept(accept))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.IS_NOT_WRITER.getErrorMessage()));
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

  private CreateProductRequest getCreateProductRequest() {
    Long categoryId = 1L;
    String title = "Product Title";
    String content = "Product Content";
    int price = 20_000;

    List<ProductImageRequest> imagesUrl = List.of(
        ProductImageRequest.testConstructor("URL1"),
        ProductImageRequest.testConstructor("URL2")
    );

    return CreateProductRequest.testConstructor(categoryId, title, content, price, imagesUrl);
  }

  private CreateProductRequest getNotIncludedProductRequest() {
    Long categoryId = 1L;
    String title = "Product Title";
    String content = "Product Content";
    int price = 20_000;

    return CreateProductRequest.testConstructor(categoryId, title, content, price, null);
  }

  private UpdateProductRequest getUpdateProductRequest() {
    return UpdateProductRequest.testConstructor("update title", "update content", 50000);
  }
}
