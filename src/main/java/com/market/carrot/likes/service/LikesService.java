package com.market.carrot.likes.service;

import com.market.carrot.login.config.customAuthentication.common.MemberContext;

public interface LikesService {

  void like(Long id, MemberContext member);
}
