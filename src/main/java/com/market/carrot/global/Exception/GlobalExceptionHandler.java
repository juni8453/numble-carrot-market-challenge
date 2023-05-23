package com.market.carrot.global.Exception;

import com.market.carrot.global.GlobalResponseDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.FOUND)
  @ExceptionHandler(GuestException.class)
  public GlobalResponseDto redirect(GuestException guestException) {
    log.error("guestException 발생: {}", guestException.getMessage());

    return GlobalResponseDto.builder()
        .code(-1)
        .httpStatus(HttpStatus.FOUND)
        .message(guestException.getMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(CustomException.class)
  public GlobalResponseDto incorrectRole(CustomException customException) {
    log.error("customException 발생: {}", customException.getMessage());

    return GlobalResponseDto.builder()
        .code(-1)
        .httpStatus(HttpStatus.BAD_REQUEST)
        .message(customException.getMessage())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public GlobalResponseDto validation(
      MethodArgumentNotValidException validException) {

    log.error("ValidException 발생: {}", validException.getMessage());

    List<ValidationExceptionField> fields = new ArrayList<>();

    for (FieldError fieldError : validException.getFieldErrors()) {
      ValidationExceptionField validationField =
          ValidationExceptionField.createValidationField(fieldError);

      fields.add(validationField);
    }

    ValidationExceptionFieldResponse fieldResponse =
        ValidationExceptionFieldResponse.createFieldResponse(fields);

    return GlobalResponseDto.builder()
        .code(-1)
        .httpStatus(HttpStatus.BAD_REQUEST)
        .message("Valid 관련 예외 발생")
        .body(fieldResponse)
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(SQLException.class)
  public GlobalResponseDto sqlException(SQLException sqlException) {
    log.error("SQLException 발생: {}", sqlException.getMessage());

    return GlobalResponseDto.builder()
        .code(-1)
        .httpStatus(HttpStatus.BAD_REQUEST)
        .message(sqlException.getMessage())
        .build();
  }
}
