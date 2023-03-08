package com.market.carrot.global.Exception;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidationExceptionFieldResponse {

  private final List<ValidationExceptionField> fields;

  @Builder
  private ValidationExceptionFieldResponse(List<ValidationExceptionField> fields) {
    this.fields = fields;
  }

  public static ValidationExceptionFieldResponse createFieldResponse(
      List<ValidationExceptionField> fields) {
    return ValidationExceptionFieldResponse.builder()
        .fields(fields)
        .build();
  }
}
