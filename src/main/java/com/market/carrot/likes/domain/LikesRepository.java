package com.market.carrot.likes.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Long> {

  @Query("SELECT l FROM Likes l JOIN FETCH l.member JOIN FETCH l.product where l.member.username = :username and l.product.id = :id")
  Likes findByUsernameAndProductId(
      @Param("username") String username,
      @Param("id") Long id);
}
