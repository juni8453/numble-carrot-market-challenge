package com.market.carrot.global.Exception;

import lombok.Getter;

@Getter
public class ValidationExceptionFieldResponse {

  private final ValidationExceptionField field;

  public ValidationExceptionFieldResponse(ValidationExceptionField field) {
    this.field = field;
  }
}
