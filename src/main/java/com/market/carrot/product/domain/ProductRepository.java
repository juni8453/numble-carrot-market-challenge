package com.market.carrot.product.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT p FROM Product p JOIN FETCH p.member JOIN FETCH p.category")
  List<Product> readAll();

  @Query("SELECT p FROM Product p JOIN FETCH p.member JOIN FETCH p.category WHERE p.id = :id")
  Optional<Product> findById(@Param("id") Long id);
}
