package kr.bb.product.exception.errors;

import kr.bb.product.exception.errors.common.ErrorCode;

public class CategoryNotFoundException extends RuntimeException {

  private static final String message = ErrorCode.CATEGORY_NOT_FOUND.getMessage();

  public CategoryNotFoundException() {
    super(message);
  }
}
