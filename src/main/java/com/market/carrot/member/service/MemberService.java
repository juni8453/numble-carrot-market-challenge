package com.market.carrot.member.service;

import com.market.carrot.global.GlobalResponseDto;

public interface MemberService {

  GlobalResponseDto detail(Long id);

  GlobalResponseDto delete(Long id);
}
