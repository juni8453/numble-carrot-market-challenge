package com.market.carrot.likes.service;

import com.market.carrot.global.Exception.NotFoundEntityException;
import com.market.carrot.likes.domain.Likes;
import com.market.carrot.likes.domain.LikesRepository;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Member;
import com.market.carrot.member.domain.MemberRepository;
import com.market.carrot.product.domain.Product;
import com.market.carrot.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikesServiceImpl implements LikesService {

  private final LikesRepository likesRepository;
  private final ProductRepository productRepository;
  private final MemberRepository memberRepository;

  @Transactional
  @Override
  public void like(Long id, MemberContext member) {
    Product findProduct = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException("찾을 수 없는 제품입니다.", HttpStatus.BAD_REQUEST));

    Member findMember = memberRepository.findById(member.getMember().getId())
        .orElseThrow(() -> new NotFoundEntityException("찾을 수 없는 회원입니다.", HttpStatus.BAD_REQUEST));

    Likes findLikes = likesRepository.findByUsernameAndProductId(findMember.getUsername(), id);

    // 자신이 좋아요를 이미 누른 제품인 경우 heartCount 를 하나 줄이고 Entity 를 DB 에서 삭제한다.
    if (findLikes != null) {
      likesRepository.delete(findLikes);
      findProduct.minusHeartCount();
      return;
    }

    // 자신이 좋아요를 누르지 않은 제품인 경우 heartCount 를 하나 올려준다.
    Likes saveLikes = Likes.createLikes();
    saveLikes.addProduct(findProduct);
    saveLikes.addMember(findMember);

    likesRepository.save(saveLikes);
    findProduct.plusHeartCount();
  }
}