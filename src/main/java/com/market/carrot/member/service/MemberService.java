package com.market.carrot.member.service;

import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.member.controller.dto.response.MemberResponse;

public interface MemberService {

  MemberResponse readDetail(Long id, MemberContext memberContext);

  void delete(Long id, MemberContext memberContext);
}
