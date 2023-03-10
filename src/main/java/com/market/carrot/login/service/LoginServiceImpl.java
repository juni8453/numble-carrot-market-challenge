package com.market.carrot.login.service;

import com.market.carrot.global.Exception.CustomException;
import com.market.carrot.global.Exception.ResponseMessage.ExceptionMessage;
import com.market.carrot.login.domain.LoginRepository;
import com.market.carrot.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

  private final LoginRepository loginRepository;

  @Transactional
  @Override
  public void save(Member member) {
    if (loginRepository.findByUsername(member.getUsername()).isPresent()) {
      throw new CustomException(ExceptionMessage.USER_DUPLICATED, HttpStatus.BAD_REQUEST);
    }

    loginRepository.save(member);
  }
}
