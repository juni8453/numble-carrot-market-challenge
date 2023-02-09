package com.market.carrot.global.Exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class IsNotWriterException extends RuntimeException {

  private final String errorMessage;
  private final HttpStatus httpStatus;

  @Override
  public String getMessage() {
    return errorMessage;
  }

}
