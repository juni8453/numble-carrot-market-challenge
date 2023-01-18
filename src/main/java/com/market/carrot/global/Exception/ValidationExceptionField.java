package com.market.carrot.global.Exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidationExceptionField {

  private final String fieldName;
  private final String fieldMessage;

  @Builder
  public ValidationExceptionField(String fieldName, String fieldMessage) {
    this.fieldName = fieldName;
    this.fieldMessage = fieldMessage;
  }
}
