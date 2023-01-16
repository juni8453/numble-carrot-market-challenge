package com.market.carrot.product.domain;

import com.market.carrot.BaseTime;
import com.market.carrot.category.domain.Category;
import com.market.carrot.login.domain.Member;
import com.market.carrot.product.dto.request.UpdateProductRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String title;

  @Column(length = 800, nullable = false)
  private String content;

  @Column(nullable = false)
  private int price;

  @ColumnDefault(value = "0")
  @Column(nullable = false)
  private int heartCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProductImage> productImages = new ArrayList<>();

  @Builder
  private Product(String title, String content, int price) {
    this.title = title;
    this.content = content;
    this.price = price;
  }

  public static Product createProduct(String title, String content, int price) {
    return new Product(title, content, price);
  }

  public void updateProduct(UpdateProductRequest productRequest) {
    this.title = productRequest.getTitle();
    this.content = productRequest.getContent();
    this.price = productRequest.getPrice();
    this.productImages.clear();

    for (ProductImage productImage : productRequest.getImagesUrl()) {
      this.productImages.add(productImage);
      productImage.addProduct(this);
    }
  }

  public void addMember(Member findMember) {
    this.member = findMember;
  }

  public void addCategory(Category findCategory) {
    this.category = findCategory;
  }

  public void addImages(ProductImage productImage) {
    this.productImages.add(productImage);
  }
}
