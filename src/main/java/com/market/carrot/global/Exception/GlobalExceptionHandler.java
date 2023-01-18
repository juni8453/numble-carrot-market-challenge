package com.market.carrot.global.Exception;

import com.market.carrot.global.GlobalResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(UserDuplicatedException.class)
  public GlobalResponseDto userDuplicated(UserDuplicatedException userDuplicatedException) {
    log.error("UserDuplicatedException 발생: {}", userDuplicatedException.getMessage());

    return GlobalResponseDto.builder()
        .code(-1)
        .message(userDuplicatedException.getMessage())
        .build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public GlobalResponseDto validation(MethodArgumentNotValidException validException) {
    log.error("ValidException 발생: {}", validException.getMessage());
    List<ValidationExceptionFieldResponse> responses = new ArrayList<>();

    for (FieldError fieldError : validException.getFieldErrors()) {
      ValidationExceptionField validationExceptionField = ValidationExceptionField.builder()
          .fieldName(fieldError.getField())
          .fieldMessage(fieldError.getDefaultMessage())
          .build();

      ValidationExceptionFieldResponse response = new ValidationExceptionFieldResponse(validationExceptionField);
      responses.add(response);
    }

    return GlobalResponseDto.builder()
        .code(-1)
        .message("Valid 관련 예외 발생")
        .body(responses)
        .build();
  }
}
