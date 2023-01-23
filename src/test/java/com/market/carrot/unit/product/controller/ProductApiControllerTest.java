package com.market.carrot.unit.product.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.carrot.config.WithMockCustomUser;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Role;
import com.market.carrot.product.controller.ProductApiController;
import com.market.carrot.product.domain.ProductImage;
import com.market.carrot.product.dto.request.CreateProductRequest;
import com.market.carrot.product.dto.response.CategoryByProductResponse;
import com.market.carrot.product.dto.response.ImageResponse;
import com.market.carrot.product.dto.response.ImagesResponse;
import com.market.carrot.product.dto.response.MemberByProductResponse;
import com.market.carrot.product.dto.response.ProductResponse;
import com.market.carrot.product.service.ProductService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

// @WebMvcTest 사용 시 JPA 관련 Bean 을 찾지 못해서 JpaMetamodelMappingContext 를 Mock 으로 등록
@MockBean(classes = JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@WebMvcTest(controllers = ProductApiController.class)
public class ProductApiControllerTest {

  @MockBean
  private ProductService productService;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private MockMvc mvc;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ObjectMapper mapper;

  @DisplayName("CreateProductRequest DTO 를 받아 상품을 생성할 수 있다.")
  @WithMockCustomUser
  @Test
  void 상품_생성() throws Exception {
    // given
    CreateProductRequest request = getCreateProductRequest();
    MemberContext member = (MemberContext) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();

    willDoNothing().given(productService).save(request, member);

    // when & then
    mvc.perform(post("/api/product")
            .with(csrf())
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @DisplayName("상품 아이디를 통해 단일 상품을 조회할 수 있다.")
  @WithMockUser
  @Test
  void 단일_상품_조회() throws Exception {
    // given
    CreateProductRequest request = getCreateProductRequest();
    ProductResponse response = getProductResponse(request);

    given(productService.detail(request.getCategoryId())).willReturn(response);

    // when & then
    mvc.perform(get("/api/product/" + request.getCategoryId())
            .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk());
  }

  private CreateProductRequest getCreateProductRequest() {
    Long categoryId = 1L;
    String title = "Product Title";
    String content = "Product Content";
    int price = 10_000;

    List<ProductImage> imagesUrl = List.of(
        ProductImage.testConstructor(1L, "URL1"),
        ProductImage.testConstructor(2L, "URL2"));

    return CreateProductRequest.testConstructor(categoryId, title, content, price, imagesUrl);
  }

  private ProductResponse getProductResponse(CreateProductRequest request) {
    MemberByProductResponse member = MemberByProductResponse.builder()
        .id(1L)
        .username("user")
        .role(Role.USER)
        .build();

    CategoryByProductResponse category = CategoryByProductResponse.builder()
        .id(1L)
        .name("Test Category")
        .build();

    ImageResponse url1 = ImageResponse.builder()
        .imageUrl("URL1")
        .build();

    ImageResponse url2 = ImageResponse.builder()
        .imageUrl("URL2")
        .build();

    ImagesResponse imagesResponse = ImagesResponse.builder()
        .images(List.of(url1, url2))
        .build();

    return ProductResponse.builder()
        .id(request.getCategoryId())
        .title(request.getTitle())
        .content(request.getContent())
        .price(request.getPrice())
        .heartCount(0)
        .createdDate(LocalDateTime.now())
        .modifiedDate(LocalDateTime.now())
        .member(member)
        .category(category)
        .images(imagesResponse)
        .build();
  }
}
