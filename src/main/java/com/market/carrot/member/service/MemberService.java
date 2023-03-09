package com.market.carrot.member.service;

import com.market.carrot.login.config.customAuthentication.common.MemberContext;
import com.market.carrot.member.hateoas.MemberModel;

public interface MemberService {

  MemberModel readDetail(Long id, MemberContext memberContext);

  void delete(Long id, MemberContext memberContext);
}
