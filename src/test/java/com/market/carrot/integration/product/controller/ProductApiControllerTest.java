package com.market.carrot.integration.product.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.carrot.category.controller.dto.request.CreateCategoryRequest;
import com.market.carrot.category.service.CategoryService;
import com.market.carrot.config.DatabaseCleanup;
import com.market.carrot.config.RestDocsConfig;
import com.market.carrot.config.WithMockCustomUser;
import com.market.carrot.global.Exception.ResponseMessage.ExceptionMessage;
import com.market.carrot.global.Exception.ResponseMessage.SuccessMessage;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Role;
import com.market.carrot.login.service.LoginService;
import com.market.carrot.member.domain.Member;
import com.market.carrot.product.controller.dto.request.CreateProductRequest;
import com.market.carrot.product.controller.dto.request.ProductImageRequest;
import com.market.carrot.product.controller.dto.request.UpdateProductRequest;
import com.market.carrot.product.service.ProductService;
import java.util.List;
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

  @DisplayName("비회원인 경우 등록 API 를 호출하면 커스텀 예외 페이지로 이동한다.")
  @Test
  void 비회원_상품_등록() throws Exception {
    // given
    CreateProductRequest createProductRequest = getCreateProductRequest();

    // when & then
    mvc.perform(post("/api/products/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(createProductRequest)))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }

  @DisplayName("CreateProductRequest 를 받아 상품을 등록할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 회원_상품_등록() throws Exception {
    CreateProductRequest createProductRequest = getCreateProductRequest();

    // when & then
    mvc.perform(post("/api/products/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(createProductRequest)))
        .andDo(print())
        .andExpect(status().isCreated())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.CREATED.name()))
        .andExpect(jsonPath("message").value(
            SuccessMessage.SUCCESS_POST_INSERT_PRODUCT.getSuccessMessage()))

        .andDo(document("product/member/insert-product",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
            ),
            requestFields(
                fieldWithPath("categoryId").description("카테고리 아이디"),
                fieldWithPath("title").description("상품 제목"),
                fieldWithPath("content").description("상품 내용"),
                fieldWithPath("price").description("상품 가격"),
                fieldWithPath("imagesUrl[].imageUrl").description("상품 이미지 URL")
            ),
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaTypes.HAL_JSON_VALUE)
            ),
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.CREATED),
                fieldWithPath("message").description(
                    SuccessMessage.SUCCESS_POST_INSERT_PRODUCT),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("상품 등록 시 CreateProductRequest 에 이미지가 없다면 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 이미지_없이_상품_등록() throws Exception {
    // given
    CreateProductRequest notIncludedProductRequest = getNotIncludedProductRequest();

    // when & then
    mvc.perform(post("/api/products/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(notIncludedProductRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(
            ExceptionMessage.IS_NOT_INCLUDED_IMAGE.getErrorMessage()))

        .andDo(document("product/member/insert-product-non-existent-image",
            requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
            ),
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaTypes.HAL_JSON_VALUE)
            ),
            responseFields(
                fieldWithPath("code").description("응답 실패 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.BAD_REQUEST),
                fieldWithPath("message").description(ExceptionMessage.IS_NOT_INCLUDED_IMAGE),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("비회원인 경우라도 단일 상품을 조회할 수 있다.")
  @Test
  void 비회원_단일_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/products/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            SuccessMessage.SUCCESS_GET_PRODUCT.getSuccessMessage()))

        .andDo(document("product/guest/select-product",
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.OK),
                fieldWithPath("message").description(SuccessMessage.SUCCESS_GET_PRODUCT),
                fieldWithPath("body.id").description("상품 아이디"),
                fieldWithPath("body.title").description("상품 제목"),
                fieldWithPath("body.content").description("상품 내용"),
                fieldWithPath("body.price").description("상품 가격"),
                fieldWithPath("body.heartCount").description("상품 좋아요 수"),
                fieldWithPath("body.createdDate").description("상품 등록 날짜"),
                fieldWithPath("body.modifiedDate").description("상품 수정 날짜"),
                fieldWithPath("body.member.id").description("상품을 등록한 회원 아이디"),
                fieldWithPath("body.member.username").description("상품을 등록한 회원 이름"),
                fieldWithPath("body.member.email").description("상품을 등록한 회원 이메일"),
                fieldWithPath("body.member.role").description("상품을 등록한 회원 권한"),
                fieldWithPath("body.category.id").description("상품을 등록한 카테고리 아이디"),
                fieldWithPath("body.category.name").description("상품을 등록한 카테고리 이름"),
                fieldWithPath("body.image.images[].id").description("상품 이미지 아이디"),
                fieldWithPath("body.image.images[].imageUrl").description("상품 이미지 URL")
            )
        ));
  }

  @DisplayName("회원이지만 자신이 등록한 상품이 아닌 경우라도 단일 상품을 조회할 수 있다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser", role = Role.USER)
  @Test
  void 자신이_등록한_상품이_이닌경우_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/products/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            SuccessMessage.SUCCESS_GET_PRODUCT.getSuccessMessage()))

        .andDo(document("product/member/select-product-is-not-writer",
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.OK),
                fieldWithPath("message").description(SuccessMessage.SUCCESS_GET_PRODUCT),
                fieldWithPath("body.id").description("상품 아이디"),
                fieldWithPath("body.title").description("상품 제목"),
                fieldWithPath("body.content").description("상품 내용"),
                fieldWithPath("body.price").description("상품 가격"),
                fieldWithPath("body.heartCount").description("상품 좋아요 수"),
                fieldWithPath("body.createdDate").description("상품 등록 날짜"),
                fieldWithPath("body.modifiedDate").description("상품 수정 날짜"),
                fieldWithPath("body.member.id").description("상품을 등록한 회원 아이디"),
                fieldWithPath("body.member.username").description("상품을 등록한 회원 이름"),
                fieldWithPath("body.member.email").description("상품을 등록한 회원 이메일"),
                fieldWithPath("body.member.role").description("상품을 등록한 회원 권한"),
                fieldWithPath("body.category.id").description("상품을 등록한 카테고리 아이디"),
                fieldWithPath("body.category.name").description("상품을 등록한 카테고리 이름"),
                fieldWithPath("body.image.images[].id").description("상품 이미지 아이디"),
                fieldWithPath("body.image.images[].imageUrl").description("상품 이미지 URL")
            )
        ));
  }

  @DisplayName("회원이면서 자신이 등록한 상품인 경우 해당 상품을 조회할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 자신이_등록한_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/products/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            SuccessMessage.SUCCESS_GET_PRODUCT.getSuccessMessage()))

        .andDo(document("product/member/select-product",
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.OK),
                fieldWithPath("message").description(SuccessMessage.SUCCESS_GET_PRODUCT),
                fieldWithPath("body.id").description("상품 아이디"),
                fieldWithPath("body.title").description("상품 제목"),
                fieldWithPath("body.content").description("상품 내용"),
                fieldWithPath("body.price").description("상품 가격"),
                fieldWithPath("body.heartCount").description("상품 좋아요 수"),
                fieldWithPath("body.createdDate").description("상품 등록 날짜"),
                fieldWithPath("body.modifiedDate").description("상품 수정 날짜"),
                fieldWithPath("body.member.id").description("상품을 등록한 회원 아이디"),
                fieldWithPath("body.member.username").description("상품을 등록한 회원 이름"),
                fieldWithPath("body.member.email").description("상품을 등록한 회원 이메일"),
                fieldWithPath("body.member.role").description("상품을 등록한 회원 권한"),
                fieldWithPath("body.category.id").description("상품을 등록한 카테고리 아이디"),
                fieldWithPath("body.category.name").description("상품을 등록한 카테고리 이름"),
                fieldWithPath("body.image.images[].id").description("상품 이미지 아이디"),
                fieldWithPath("body.image.images[].imageUrl").description("상품 이미지 URL")
            )
        ));
  }

  @DisplayName("회원일 경우 모든 상품을 조회할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 회원_모든_상품조회() throws Exception {
    // when & then
    mvc.perform(get("/api/products/")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            SuccessMessage.SUCCESS_GET_PRODUCTS.getSuccessMessage()))

        .andDo(document("product/member/select-products",
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.OK),
                fieldWithPath("message").description(SuccessMessage.SUCCESS_GET_PRODUCTS),

                fieldWithPath("body[].id").description("상품 아이디"),
                fieldWithPath("body[].title").description("상품 제목"),
                fieldWithPath("body[].content").description("상품 내용"),
                fieldWithPath("body[].price").description("상품 가격"),
                fieldWithPath("body[].heartCount").description("상품 좋아요 수"),
                fieldWithPath("body[].createdDate").description("상품 등록 날짜"),
                fieldWithPath("body[].modifiedDate").description("상품 수정 날짜"),

                fieldWithPath("body[].member.id").description("상품을 등록한 회원 아이디"),
                fieldWithPath("body[].member.username").description("상품을 등록한 회원 이름"),
                fieldWithPath("body[].member.email").description("상품을 등록한 회원 이메일"),
                fieldWithPath("body[].member.role").description("상품을 등록한 회원 권한"),

                fieldWithPath("body[].category.id").description("상품을 등록한 카테고리 아이디"),
                fieldWithPath("body[].category.name").description("상품을 등록한 카테고리 이름"),

                fieldWithPath("body[].image.images[].id").description("상품 이미지 아이디"),
                fieldWithPath("body[].image.images[].imageUrl").description("상품 이미지 URL")
            )
        ));
  }

  @DisplayName("비회원인 경우 수정 API 를 호출하면 커스텀 예외 페이지로 이동한다.")
  @Test
  void 비회원_상품_수정() throws Exception {
    UpdateProductRequest updateProductRequest = getUpdateProductRequest();

    // when & then
    mvc.perform(put("/api/products/1")
            .with(csrf())
            .content(mapper.writeValueAsString(updateProductRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }

  @DisplayName("회원일 경우 자신이 등록한 상품을 수정할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 회원_상품_수정() throws Exception {
    // given
    UpdateProductRequest updateProductRequest = getUpdateProductRequest();

    // when & then
    mvc.perform(put("/api/products/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updateProductRequest)))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            SuccessMessage.SUCCESS_POST_UPDATE_PRODUCT.getSuccessMessage()))

        .andDo(document("product/member/update-product",
            requestFields(
                fieldWithPath("title").description("수정 상품 제목"),
                fieldWithPath("content").description("수정 상품 내용"),
                fieldWithPath("price").description("수정 상품 가격")
            ),
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.OK),
                fieldWithPath("message").description(
                    SuccessMessage.SUCCESS_POST_UPDATE_PRODUCT),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("회원이지만 자신이 등록한 상품이 아닌 경우 수정 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser", role = Role.USER)
  @Test
  void 자신이_등록한_상품이_아닌경우_상품수정_400() throws Exception {
    // given
    UpdateProductRequest updateProductRequest = getUpdateProductRequest();

    //when & then
    mvc.perform(put("/api/products/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updateProductRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.IS_NOT_WRITER.getErrorMessage()))

        .andDo(document("product/member/update-product-is-not-writer",
            responseFields(
                fieldWithPath("code").description("응답 실패 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.BAD_REQUEST),
                fieldWithPath("message").description(ExceptionMessage.IS_NOT_WRITER),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("비회원인 경우 삭제 API 를 호출하면 커스텀 예외 페이지로 이동한다.")
  @Test
  void 비회원_상품_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/products/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().is3xxRedirection());
  }

  @DisplayName("회원일 경우 자신이 등록한 상품을 삭제할 수 있다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 회원_상품_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/products/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())

        .andExpect(jsonPath("code").value(1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("message").value(
            SuccessMessage.SUCCESS_DELETE_PRODUCT.getSuccessMessage()))

        .andDo(document("product/member/delete-product",
            responseFields(
                fieldWithPath("code").description("응답 성공 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.OK),
                fieldWithPath("message").description(SuccessMessage.SUCCESS_DELETE_PRODUCT),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("회원이지만 자신이 등록한 상품이 아닌 경우 삭제 호출 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 2, username = "anotherUser", role = Role.USER)
  @Test
  void 자신이_등록한_상품이_아닌경우_상품삭제_400() throws Exception {
    // when & then
    mvc.perform(delete("/api/products/1")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.IS_NOT_WRITER.getErrorMessage()))

        .andDo(document("product/member/delete-product-is-not-writer",
            responseFields(
                fieldWithPath("code").description("응답 실패 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.BAD_REQUEST),
                fieldWithPath("message").description(ExceptionMessage.IS_NOT_WRITER),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("존재하지 않는 상품 조회 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 존재하지_않는_상품_단일_조회() throws Exception {
    // when & then
    mvc.perform(get("/api/products/2")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.NOT_FOUND_PRODUCT.getErrorMessage()))

        .andDo(document("product/common/select-product",
            responseFields(
                fieldWithPath("code").description("응답 실패 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.BAD_REQUEST),
                fieldWithPath("message").description(ExceptionMessage.NOT_FOUND_PRODUCT),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("존재하지 않는 상품 수정 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 존재하지_않는_상품_수정() throws Exception {
    // given
    UpdateProductRequest updateProductRequest = getUpdateProductRequest();

    // when & then
    mvc.perform(put("/api/products/2")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updateProductRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.NOT_FOUND_PRODUCT.getErrorMessage()))

        .andDo(document("product/common/update-product",
            responseFields(
                fieldWithPath("code").description("응답 실패 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.BAD_REQUEST),
                fieldWithPath("message").description(ExceptionMessage.NOT_FOUND_PRODUCT),
                fieldWithPath("body").description("null")
            )
        ));
  }

  @DisplayName("존재하지 않는 상품 삭제 시 400 예외가 발생한다.")
  @WithMockCustomUser(userId = 1, username = "username", role = Role.USER)
  @Test
  void 존재하지_않는_상품_삭제() throws Exception {
    // when & then
    mvc.perform(delete("/api/products/2")
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isBadRequest())

        .andExpect(jsonPath("code").value(-1))
        .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("message").value(ExceptionMessage.NOT_FOUND_PRODUCT.getErrorMessage()))

        .andDo(document("product/common/delete-product",
            responseFields(
                fieldWithPath("code").description("응답 실패 코드"),
                fieldWithPath("httpStatus").description(HttpStatus.BAD_REQUEST),
                fieldWithPath("message").description(ExceptionMessage.NOT_FOUND_PRODUCT),
                fieldWithPath("body").description("null")
            )
        ));
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
