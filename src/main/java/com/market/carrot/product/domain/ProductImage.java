package com.market.carrot.product.domain;

import com.market.carrot.product.dto.request.UpdateProductImageRequest;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String imageUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  public void updateProductImage(UpdateProductImageRequest imageRequest) {
    this.imageUrl = imageRequest.getImageUrl();
  }

  public void addProduct(Product product) {
    this.product = product;
  }

  /**
   * Test 용 생성자 및 팩토리 메서드
   */
  private ProductImage(Long id, String imageUrl) {
    this.id = id;
    this.imageUrl = imageUrl;
  }

  public static ProductImage testConstructor(Long id, String imageUrl) {
    return new ProductImage(id, imageUrl);
  }
}
