package com.market.carrot.global.Exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class ValidationExceptionField {

  private final String fieldName;
  private final String fieldMessage;

  @Builder
  private ValidationExceptionField(String fieldName, String fieldMessage) {
    this.fieldName = fieldName;
    this.fieldMessage = fieldMessage;
  }

  public static ValidationExceptionField createValidationField(FieldError fieldError) {
    return ValidationExceptionField.builder()
        .fieldName(fieldError.getField())
        .fieldMessage(fieldError.getDefaultMessage())
        .build();
  }
}
