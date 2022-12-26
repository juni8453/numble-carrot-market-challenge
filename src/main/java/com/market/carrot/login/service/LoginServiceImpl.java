package com.market.carrot.login.service;

import com.market.carrot.login.domain.LoginRepository;
import com.market.carrot.login.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

  private final LoginRepository loginRepository;

  @Transactional
  @Override
  public void save(Member member) {
    loginRepository.save(member);
  }
}
