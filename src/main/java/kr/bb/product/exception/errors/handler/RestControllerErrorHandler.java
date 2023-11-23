package kr.bb.product.exception.errors.handler;

import bloomingblooms.response.ErrorResponse;
import kr.bb.product.exception.errors.CategoryNotFoundException;
import kr.bb.product.exception.errors.common.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestControllerErrorHandler extends ResponseEntityExceptionHandler {

  @NotNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NotNull MethodArgumentNotValidException ex,
      @NotNull HttpHeaders headers,
      @NotNull HttpStatus status,
      @NotNull WebRequest request) {
    ErrorCode validationError = ErrorCode.VALIDATION_ERROR;
    return ResponseEntity.status(validationError.getCode())
        .body(
            ErrorResponse.builder()
                .code(String.valueOf(validationError.getCode()))
                .message(validationError.getMessage()));
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  protected ResponseEntity<ErrorResponse> categoryNotFound(
      CategoryNotFoundException categoryNotFoundException) {
    return ResponseEntity.ok()
        .body(
            ErrorResponse.builder()
                .code(categoryNotFoundException.getMessage())
                .message(categoryNotFoundException.getMessage())
                .build());
  }
}
