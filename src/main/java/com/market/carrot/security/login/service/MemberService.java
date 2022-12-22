package com.market.carrot.security.login.service;

import com.market.carrot.security.login.domain.Member;

public interface MemberService {

  String passwordEncoder(String password);

  void createMember(Member member);
}
