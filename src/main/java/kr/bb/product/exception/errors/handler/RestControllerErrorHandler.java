package kr.bb.product.exception.errors.handler;

import bloomingblooms.response.CommonResponse;
import com.amazonaws.services.sns.model.AmazonSNSException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestControllerErrorHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(AmazonSNSException.class)
  protected CommonResponse resaleSubscribeException(AmazonSNSException amazonSNSException) {
    return CommonResponse.fail(amazonSNSException.getMessage(), "EP01");
  }

//  @ExceptionHandler(ReviewNotFoundException.class)
//  protected ResponseEntity<ErrorResponse> categoryNotFound(
//      ReviewNotFoundException reviewNotFoundException) {
//    return ResponseEntity.ok()
//        .body(
//            ErrorResponse.builder()
//                .code(reviewNotFoundException.getMessage())
//                .message(reviewNotFoundException.getMessage())
//                .build());
//  }
//
//  @ExceptionHandler(CategoryNotFoundException.class)
//  protected ResponseEntity<ErrorResponse> categoryNotFound(
//      CategoryNotFoundException categoryNotFoundException) {
//    return ResponseEntity.ok()
//        .body(
//            ErrorResponse.builder()
//                .code(categoryNotFoundException.getMessage())
//                .message(categoryNotFoundException.getMessage())
//                .build());
//  }
//
//  @ExceptionHandler(ProductNotFoundException.class)
//  protected ResponseEntity<ErrorResponse> categoryNotFound(
//      ProductNotFoundException productNotFoundException) {
//    return ResponseEntity.ok()
//        .body(
//            ErrorResponse.builder()
//                .code(String.valueOf(productNotFoundException.getErrorCode()))
//                .message(productNotFoundException.getMessage())
//                .build());
//  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errors);
  }
}
