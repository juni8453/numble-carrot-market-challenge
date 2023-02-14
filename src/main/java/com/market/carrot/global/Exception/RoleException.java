package com.market.carrot.global.Exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class RoleException extends RuntimeException {

  private final ExceptionMessage errorMessage;
  private final HttpStatus httpStatus;

  @Override
  public String getMessage() {
    return errorMessage.getErrorMessage();
  }

}
