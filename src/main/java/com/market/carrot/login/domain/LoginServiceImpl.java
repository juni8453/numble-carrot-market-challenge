package com.market.carrot.login.domain;

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
