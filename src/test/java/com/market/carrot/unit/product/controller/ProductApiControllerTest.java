package com.market.carrot.unit.product.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.carrot.login.config.SecurityConfig;
import com.market.carrot.product.controller.ProductApiController;
import com.market.carrot.product.domain.ProductImage;
import com.market.carrot.product.dto.request.CreateProductRequest;
import com.market.carrot.product.service.ProductService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@MockBean(classes = JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@WebMvcTest(controllers = ProductApiController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    })
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
  @WithMockUser
  @Test
  void 상품_생성() throws Exception {

    Long categoryId = 1L;
    String title = "Product Title";
    String content = "Product Content";
    int price = 10_000;

    List<ProductImage> imagesUrl = List.of(
        ProductImage.testConstructor(1L, "URL1"),
        ProductImage.testConstructor(2L, "URL2"));

    CreateProductRequest request = CreateProductRequest.testConstructor(categoryId,
        title, content, price, imagesUrl);

    // when & then
    mvc.perform(post("/api/product")
            .with(csrf())
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
