package com.market.carrot.global;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalResponseDto {

  private final Integer code;
  private final HttpStatus httpStatus;
  private final String message;
  private final Object body;

  @Builder
  public GlobalResponseDto(Integer code, HttpStatus httpStatus, String message, Object body) {
    this.code = code;
    this.httpStatus = httpStatus;
    this.message = message;
    this.body = body;
  }
}
