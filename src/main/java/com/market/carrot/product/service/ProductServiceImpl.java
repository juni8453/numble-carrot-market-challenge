package com.market.carrot.product.service;

import com.market.carrot.category.domain.Category;
import com.market.carrot.category.domain.CategoryRepository;
import com.market.carrot.global.Exception.CustomException;
import com.market.carrot.global.Exception.ResponseMessage.ExceptionMessage;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.product.controller.dto.request.CreateProductRequest;
import com.market.carrot.product.controller.dto.request.ProductImageRequest;
import com.market.carrot.product.controller.dto.request.UpdateProductRequest;
import com.market.carrot.product.controller.dto.response.CategoryByProductResponse;
import com.market.carrot.product.controller.dto.response.ImagesResponse;
import com.market.carrot.product.controller.dto.response.MemberByProductResponse;
import com.market.carrot.product.controller.dto.response.ProductResponse;
import com.market.carrot.product.domain.Product;
import com.market.carrot.product.domain.ProductImage;
import com.market.carrot.product.domain.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  @Override
  public List<ProductResponse> readAll(MemberContext memberContext) {
    List<Product> products = productRepository.readAll();

    return products.stream()
        .map(product -> ProductResponse.builder()
            .id(product.getId())
            .title(product.getTitle())
            .content(product.getContent())
            .price(product.getPrice())
            .heartCount(product.getHeartCount())
            .createdDate(product.getCreatedDate())
            .modifiedDate(product.getModifiedDate())
            .member(MemberByProductResponse.from(product))
            .category(CategoryByProductResponse.from(product))
            .image(ImagesResponse.from(product))
            .build())
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public ProductResponse readDetail(Long id, MemberContext memberContext) {
    Product findProduct = productRepository.findById(id).orElseThrow(
        () -> new CustomException(ExceptionMessage.NOT_FOUND_PRODUCT,
            HttpStatus.BAD_REQUEST));

    return ProductResponse.builder()
        .id(findProduct.getId())
        .title(findProduct.getTitle())
        .content(findProduct.getContent())
        .price(findProduct.getPrice())
        .heartCount(findProduct.getHeartCount())
        .createdDate(findProduct.getCreatedDate())
        .modifiedDate(findProduct.getModifiedDate())
        .member(MemberByProductResponse.from(findProduct))
        .category(CategoryByProductResponse.from(findProduct))
        .image(ImagesResponse.from(findProduct))
        .build();
  }

  @Transactional
  @Override
  public void save(CreateProductRequest productRequest, MemberContext memberContext) {
    String title = productRequest.getTitle();
    String content = productRequest.getContent();
    int price = productRequest.getPrice();
    Long categoryId = productRequest.getCategoryId();
    List<ProductImageRequest> imagesUrl = productRequest.getImagesUrl();

    Product saveProduct = Product.createProduct(title, content, price);

    Category findCategory = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_CATEGORY,
            HttpStatus.BAD_REQUEST));

    // 연관관계에 의한 Member, Category, Image 값 셋팅
    saveProduct.addMember(memberContext.getMember());
    saveProduct.addCategory(findCategory);

    if (imagesUrl == null || imagesUrl.isEmpty()) {
      throw new CustomException(ExceptionMessage.IS_NOT_INCLUDED_IMAGE,
          HttpStatus.BAD_REQUEST);
    }

    List<ProductImage> imageUrls = imagesUrl.stream()
        .map(imageUrl -> ProductImage.createConstructor(imageUrl.getImageUrl()))
        .collect(Collectors.toList());

    // ProductImage FK 인 product_id 값을 셋팅하기 위해 각 ProductImage 객체에 addProduct() 호출
    for (ProductImage imageUrl : imageUrls) {
      imageUrl.addProduct(saveProduct);
      saveProduct.addImages(imageUrl);
    }

    // 연관관계 셋팅 후 Product 저장
    productRepository.save(saveProduct);
  }

  @Transactional
  @Override
  public void update(Long id, UpdateProductRequest productRequest, MemberContext memberContext) {
    Product findProduct = productRepository.findById(id)
        .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PRODUCT,
            HttpStatus.BAD_REQUEST));

    String updateTitle = productRequest.getTitle();
    String updateContent = productRequest.getContent();
    int updatePrice = productRequest.getPrice();

    findProduct.updateProduct(updateTitle, updateContent, updatePrice, memberContext.getMember());
  }

  @Transactional
  @Override
  public void delete(Long id, MemberContext memberContext) {
    Product findProduct = productRepository.findById(id)
        .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PRODUCT,
            HttpStatus.BAD_REQUEST));

    if (!findProduct.checkUser(memberContext.getMember())) {
      throw new CustomException(ExceptionMessage.IS_NOT_WRITER, HttpStatus.BAD_REQUEST);
    }

    productRepository.delete(findProduct);
  }
}
