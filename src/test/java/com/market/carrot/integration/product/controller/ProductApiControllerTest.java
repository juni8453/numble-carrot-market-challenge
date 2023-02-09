package com.market.carrot.integration.product.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
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
    MemberContext member = new MemberContext(createMember);

    CreateProductRequest productRequest = getInitProduct();
    CreateCategoryRequest categoryRequest = getInitCategory();

    loginService.save(member.getMember());
    categoryService.save(categoryRequest);
    productService.save(productRequest, member);
  }

  @AfterEach
  void clearDB() {
    databaseCleanup.execute();
  }

  @DisplayName("CreateProductRequest, Member 를 받아 상품을 생성할 수 있다.")
  @WithMockCustomUser
  @Test
  void 상품_생성() throws Exception {
    CreateProductRequest createProductRequest = getCreateProductRequest();

    // when & then
    mvc.perform(post("/api/product")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept)
            .content(mapper.writeValueAsString(createProductRequest)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, accept));
  }

  @DisplayName("비회원인 경우 단일 상품을 조회했을 때 self link 만 응답에 포함되어야한다.")
  @Test
  void 비회원_단일_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/product/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, accept))
        .andExpect(jsonPath("body.links[0].rel").value("self"))
        .andExpect(jsonPath("body.links[0].rel").value("self"))
        .andExpect(jsonPath("body.links[1].rel").doesNotExist())
        .andExpect(jsonPath("body.links[2].rel").doesNotExist());
  }

  @DisplayName("회원이지만 자신이 등록한 상품이 아닌 경우 self link 만 응답에 포함되어야한다.")
  @WithMockCustomUser(username = "anotherUser")
  @Test
  void 자신이_등록한_상품이_이닌경우_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/product/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("body.links[0].rel").exists())
        .andExpect(jsonPath("body.links[0].rel").value("self"));
  }

  @DisplayName("회원이면서 자신이 등록한 상품인 경우 self, update, delete link 모두 응답에 포함되어야한다.")
  @WithMockCustomUser(username = "username")
  @Test
  void 자신이_등록한_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/product/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())
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
    mvc.perform(get("/api/product")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("body.content[0].links[0].rel").exists())
        .andExpect(jsonPath("body.content[0].links[0].rel").value("self"))
        .andExpect(jsonPath("body.links[0]").doesNotExist());
  }

  @DisplayName("회원일 경우 content 내부 각 상품의 단일 상품 조회 link, 바깥쪽 links[] 에 상품 등록 link 가 응답에 포함되어야한다.")
  @WithMockCustomUser(username = "username")
  @Test
  void 회원_모든_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/product")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(accept))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("body.content[0].links[0].rel").value("self"))
        .andExpect(jsonPath("body.links[0].rel").exists())
        .andExpect(jsonPath("body.links[0].rel").value("product-save"));
  }

  @Test
  void 상품_수정() {
    // given

    // when & then

  }

  @Test
  void 상품_삭제() {
    // given

    // when & then

  }

  // TODO : ADMIN 권한 유저의 경우도 테스트 필요

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
    Long categoryId = 2L;
    String title = "Product Title";
    String content = "Product Content";
    int price = 20_000;

    List<ProductImageRequest> imagesUrl = List.of(
        ProductImageRequest.testConstructor("URL1"),
        ProductImageRequest.testConstructor("URL2")
    );

    return CreateProductRequest.testConstructor(categoryId, title, content, price, imagesUrl);
  }
}
