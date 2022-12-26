package com.market.carrot.Exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class NotFoundEntityException extends RuntimeException {

  private final String errorMessage;
  private final HttpStatus httpStatus;

  @Override
  public String getMessage() {
    return errorMessage;
  }
}
