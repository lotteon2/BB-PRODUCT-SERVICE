package kr.bb.product.exception.errors.handler;

import bloomingblooms.response.CommonResponse;
import com.amazonaws.services.sns.model.AmazonSNSException;
import java.util.HashMap;
import java.util.Map;
import kr.bb.product.exception.errors.FlowerIdNotMatchException;
import kr.bb.product.exception.errors.ProductNotFoundException;
import kr.bb.product.exception.errors.ProductPriceValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestControllerErrorHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(FlowerIdNotMatchException.class)
  protected CommonResponse productPriceValidationException(
      FlowerIdNotMatchException productPriceValidationException) {
    return CommonResponse.fail(productPriceValidationException.getMessage(), "EFC01");
  }

  @ExceptionHandler(ProductPriceValidationException.class)
  protected CommonResponse productPriceValidationException(
      ProductPriceValidationException productPriceValidationException) {
    return CommonResponse.fail(productPriceValidationException.getMessage(), "EPC01");
  }

  @ExceptionHandler(AmazonSNSException.class)
  protected CommonResponse resaleSubscribeException(AmazonSNSException amazonSNSException) {
    return CommonResponse.fail(amazonSNSException.getMessage(), "EA01");
  }

  @ExceptionHandler(ProductNotFoundException.class)
  protected CommonResponse productNotFoundException(
      ProductNotFoundException productNotFoundException) {
    return CommonResponse.fail(productNotFoundException.getMessage(), "EP02");
  }

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
