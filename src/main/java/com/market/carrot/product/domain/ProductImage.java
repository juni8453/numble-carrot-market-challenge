package com.market.carrot.product.domain;

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

  public void updateProductImage(String updateImageUrl) {
    this.imageUrl = updateImageUrl;
  }

  public void addProduct(Product product) {
    this.product = product;
  }

  private ProductImage(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public static ProductImage createConstructor(String imageUrl) {
    return new ProductImage(imageUrl);
  }
}
