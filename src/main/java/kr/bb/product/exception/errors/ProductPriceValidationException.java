package kr.bb.product.exception.errors;

import bloomingblooms.errors.BaseException;
import bloomingblooms.errors.ErrorCode;

public class ProductPriceValidationException extends BaseException {
  private static final String message = "상품 가격이 올바르지 않습니다";

  public ProductPriceValidationException() {
    super(message, ErrorCode.COMMON_INVALID_PARAMETER);
  }
}
