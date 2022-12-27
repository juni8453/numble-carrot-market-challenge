package com.market.carrot.global;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GlobalResponseDto {

  private final Integer code;
  private final String message;
  private final Object body;

  @Builder
  public GlobalResponseDto(Integer code, String message, Object body) {
    this.code = code;
    this.message = message;
    this.body = body;
  }

  @Builder
  public GlobalResponseDto(Integer code, String message) {
    this(code, message, null);
  }
}
