package kr.bb.product.handler;

import bloomingblooms.response.ErrorResponse;
import kr.bb.product.errors.CategoryNotFoundException;
import kr.bb.product.errors.common.ErrorCode;
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
  protected ResponseEntity<ErrorResponse> categoryNotFound() {
    ErrorCode errorCode = ErrorCode.CATEGORY_NOT_FOUND;
    return ResponseEntity.ok()
        .body(
            ErrorResponse.builder()
                .code(String.valueOf(errorCode.getCode()))
                .message(errorCode.getMessage())
                .build());
  }
}
