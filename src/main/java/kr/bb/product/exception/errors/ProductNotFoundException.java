package kr.bb.product.exception.errors;

import kr.bb.product.exception.errors.common.ErrorCode;

public class ProductNotFoundException extends RuntimeException {

  private static final String message = ErrorCode.PRODUCT_NOT_FOUND.getMessage();

  public ProductNotFoundException() {
    super(message);
  }
}
