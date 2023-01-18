package com.market.carrot.product.service;

import com.market.carrot.category.domain.Category;
import com.market.carrot.category.domain.CategoryRepository;
import com.market.carrot.global.Exception.NotFoundEntityException;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Member;
import com.market.carrot.member.domain.MemberRepository;
import com.market.carrot.product.domain.Product;
import com.market.carrot.product.domain.ProductImage;
import com.market.carrot.product.domain.ProductRepository;
import com.market.carrot.product.dto.request.CreateProductRequest;
import com.market.carrot.product.dto.request.UpdateProductRequest;
import com.market.carrot.product.dto.response.CategoryByProductResponse;
import com.market.carrot.product.dto.response.ImagesResponse;
import com.market.carrot.product.dto.response.MemberByProductResponse;
import com.market.carrot.product.dto.response.ProductResponse;
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
  private final MemberRepository memberRepository;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  @Override
  public List<ProductResponse> readAll() {

    return productRepository.readAll().stream().map(
        product -> ProductResponse.builder().id(product.getId()).title(product.getTitle())
            .content(product.getContent()).price(product.getPrice())
            .heartCount(product.getHeartCount()).createdDate(product.getCreatedDate())
            .modifiedDate(product.getModifiedDate()).member(MemberByProductResponse.from(product))
            .category(CategoryByProductResponse.from(product)).images(ImagesResponse.from(product))
            .build()).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public ProductResponse detail(Long id) {
    Product findProduct = productRepository.findById(id).orElseThrow(
        () -> new NotFoundEntityException("해당 제품이 존재하지 않습니다.", HttpStatus.BAD_REQUEST));

    return ProductResponse.builder().id(findProduct.getId()).title(findProduct.getTitle())
        .content(findProduct.getContent()).price(findProduct.getPrice())
        .heartCount(findProduct.getHeartCount()).createdDate(findProduct.getCreatedDate())
        .modifiedDate(findProduct.getModifiedDate())
        .member(MemberByProductResponse.from(findProduct))
        .category(CategoryByProductResponse.from(findProduct))
        .images(ImagesResponse.from(findProduct)).build();
  }

  @Transactional
  @Override
  public void save(CreateProductRequest productRequest, MemberContext member) {
    String title = productRequest.getTitle();
    String content = productRequest.getContent();
    int price = productRequest.getPrice();
    Long categoryId = productRequest.getCategoryId();
    List<ProductImage> imagesUrl = productRequest.getImagesUrl();

    Product saveProduct = Product.createProduct(title, content, price);

    Member findMember = memberRepository.findById(member.getMember().getId())
        .orElseThrow(() -> new NotFoundEntityException("찾을 수 없는 회원입니다.", HttpStatus.BAD_REQUEST));

    Category findCategory = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundEntityException("찾을 수 없는 카테고리입니다.", HttpStatus.BAD_REQUEST));

    // 연관관계에 의한 Member, Category, Image 값 셋팅
    saveProduct.addMember(findMember);
    saveProduct.addCategory(findCategory);

    // ProductImage FK 인 product_id 값을 셋팅하기 위해 각 ProductImage 객체에 addProduct() 호출
    for (ProductImage imageUrl : imagesUrl) {
      imageUrl.addProduct(saveProduct);
      saveProduct.addImages(imageUrl);
    }

    // 연관관계 셋팅 후 Product 저장
    productRepository.save(saveProduct);
  }

  @Transactional
  @Override
  public void update(Long id, UpdateProductRequest productRequest) {
    Product findProduct = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException("찾을 수 없는 제품입니다.", HttpStatus.BAD_REQUEST));
    findProduct.updateProduct(productRequest);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    Product findProduct = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException("찾을 수 없는 제품입니다.", HttpStatus.BAD_REQUEST));

    productRepository.delete(findProduct);
  }
}
