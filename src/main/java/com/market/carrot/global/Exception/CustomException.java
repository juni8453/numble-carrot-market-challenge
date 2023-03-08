package com.market.carrot.global.Exception;

import com.market.carrot.global.Exception.ResponseMessage.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class CustomException extends RuntimeException {

  private final ExceptionMessage errorMessage;
  private final HttpStatus httpStatus;

  @Override
  public String getMessage() {
    return errorMessage.getErrorMessage();
  }
}

