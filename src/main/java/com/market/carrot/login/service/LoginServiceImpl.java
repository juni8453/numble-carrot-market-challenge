package com.market.carrot.login.service;

import com.market.carrot.login.domain.LoginRepository;
import com.market.carrot.login.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

  private final LoginRepository loginRepository;

  @Transactional
  @Override
  public void save(User user) {
    loginRepository.save(user);
  }
}
