package com.market.carrot.member.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.market.carrot.global.Exception.CustomException;
import com.market.carrot.global.Exception.ResponseMessage.ExceptionMessage;
import com.market.carrot.likes.domain.Likes;
import com.market.carrot.likes.domain.LikesRepository;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.member.controller.MemberApiController;
import com.market.carrot.member.controller.dto.response.MemberResponse;
import com.market.carrot.member.domain.Member;
import com.market.carrot.member.domain.MemberRepository;
import com.market.carrot.member.hateoas.MemberModel;
import com.market.carrot.product.domain.Product;
import com.market.carrot.product.domain.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;
  private final LikesRepository likesRepository;

  @Transactional(readOnly = true)
  @Override
  public MemberResponse readDetail(Long id, MemberContext memberContext) {
    Member findMember = memberRepository.findById(id)
        .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MEMBER,
            HttpStatus.BAD_REQUEST));

    if (!findMember.checkUser(memberContext)) {
      throw new CustomException(ExceptionMessage.IS_NOT_MY_PROFILE,
          HttpStatus.BAD_REQUEST);
    }

    return MemberResponse.builder()
        .id(findMember.getId())
        .username(findMember.getUsername())
        .email(findMember.getEmail())
        .build();
  }

  @Transactional
  @Override
  public void delete(Long id, MemberContext memberContext) {
    Member findMember = memberRepository.findById(id)
        .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MEMBER,
            HttpStatus.BAD_REQUEST));

    if (!findMember.checkUser(memberContext)) {
      throw new CustomException(ExceptionMessage.IS_NOT_MY_PROFILE_BY_DELETE,
          HttpStatus.OK);
    }

    List<Product> findProducts = productRepository.findAll().stream()
        .filter(product -> product.getMember().getId().equals(findMember.getId()))
        .collect(Collectors.toList());

    List<Likes> findLikes = likesRepository.findAll().stream()
        .filter(likes -> likes.getMember().getId().equals(findMember.getId()))
        .collect(Collectors.toList());

    productRepository.deleteAll(findProducts);
    likesRepository.deleteAll(findLikes);
    memberRepository.delete(findMember);
  }
}
