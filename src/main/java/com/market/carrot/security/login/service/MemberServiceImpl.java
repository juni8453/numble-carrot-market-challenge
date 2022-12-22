package com.market.carrot.security.login.service;

import com.market.carrot.security.login.domain.Member;
import com.market.carrot.security.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service("userService")
public class MemberServiceImpl implements MemberService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

  @Override
  public String passwordEncoder(String password) {
    return passwordEncoder.encode(password);
  }

  @Transactional
  @Override
  public void createMember(Member member) {
    memberRepository.save(member);
  }
}
