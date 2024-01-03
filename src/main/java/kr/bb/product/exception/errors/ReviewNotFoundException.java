package kr.bb.product.exception.errors;

import bloomingblooms.errors.BaseException;
import bloomingblooms.errors.ErrorCode;


public class ReviewNotFoundException extends BaseException {
  private static final String message = ErrorCode.COMMON_ENTITY_NOT_FOUND.getMessage();

  public ReviewNotFoundException() {
    super(message, ErrorCode.COMMON_ENTITY_NOT_FOUND);
  }
}
