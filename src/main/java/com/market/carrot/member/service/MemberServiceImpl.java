package com.market.carrot.member.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.market.carrot.global.Exception.AnotherMemberException;
import com.market.carrot.global.Exception.ExceptionMessage;
import com.market.carrot.global.Exception.NotFoundEntityException;
import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.login.domain.Member;
import com.market.carrot.member.controller.MemberApiController;
import com.market.carrot.member.domain.MemberRepository;
import com.market.carrot.member.dto.response.MemberResponse;
import com.market.carrot.member.hateoas.MemberModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  @Override
  public MemberModel readMyProfile(Long id, MemberContext memberContext) {
    Member findMember = memberRepository.findById(id)
        .orElseThrow(() -> new AnotherMemberException(ExceptionMessage.NOT_FOUND_MEMBER,
            HttpStatus.BAD_REQUEST));

    if (!findMember.checkUser(memberContext)) {
      throw new AnotherMemberException(ExceptionMessage.IS_NOT_MY_PROFILE,
          HttpStatus.BAD_REQUEST);
    }

    MemberResponse memberResponse = MemberResponse.builder()
        .id(findMember.getId())
        .username(findMember.getUsername())
        .email(findMember.getEmail())
        .build();

    MemberModel memberModel = new MemberModel(memberResponse);
    memberModel.add(linkTo(MemberApiController.class).slash(memberResponse.getId()).withSelfRel());
    memberModel.add(
        linkTo(MemberApiController.class).slash(memberResponse.getId()).withRel("member-delete"));
    memberModel.add(
        linkTo(MemberApiController.class).slash(memberResponse.getId()).withRel("member-update"));

    return memberModel;
  }

  @Transactional
  @Override
  public void delete(Long id, MemberContext memberContext) {
    Member findMember = memberRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException(ExceptionMessage.NOT_FOUND_MEMBER,
            HttpStatus.BAD_REQUEST));

    if (!findMember.checkUser(memberContext)) {
      throw new AnotherMemberException(ExceptionMessage.IS_NOT_MY_PROFILE_BY_DELETE,
          HttpStatus.OK);
    }

    memberRepository.delete(findMember);
  }
}
