package com.market.carrot.member.service;

import com.market.carrot.global.Exception.ExceptionMessage;
import com.market.carrot.global.Exception.NotFoundEntityException;
import com.market.carrot.global.GlobalResponseDto;
import com.market.carrot.global.GlobalResponseMessage;
import com.market.carrot.login.domain.Member;
import com.market.carrot.member.domain.MemberRepository;
import com.market.carrot.member.domain.ResponseMemberDetail;
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
  public GlobalResponseDto detail(Long id) {
    Member findMember = memberRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException(ExceptionMessage.NOT_FOUND_MEMBER, HttpStatus.BAD_REQUEST));

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(GlobalResponseMessage.SUCCESS_GET_MEMBER.getSuccessMessage())
        .body(ResponseMemberDetail.of(findMember))
        .build();
  }

  @Transactional
  @Override
  public GlobalResponseDto delete(Long id) {
    Member findMember = memberRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException(ExceptionMessage.NOT_FOUND_MEMBER, HttpStatus.BAD_REQUEST));

    memberRepository.delete(findMember);

    return GlobalResponseDto.builder()
        .code(1)
        .httpStatus(HttpStatus.OK)
        .message(GlobalResponseMessage.SUCCESS_DELETE_MEMBER.getSuccessMessage())
        .build();
  }
}
