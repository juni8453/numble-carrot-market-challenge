package com.market.carrot.Exception;

import com.market.carrot.GlobalResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundEntityException.class)
  public GlobalResponseDto notFoundEntity(NotFoundEntityException notFoundEntityException) {
    log.error("NotFoundEntityException 발생: {}", notFoundEntityException.getMessage());

    return GlobalResponseDto.builder()
        .code(-1)
        .message(notFoundEntityException.getMessage())
        .build();
  }
}
