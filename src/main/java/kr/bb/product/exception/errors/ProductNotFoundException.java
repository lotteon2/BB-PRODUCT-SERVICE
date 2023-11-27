package kr.bb.product.exception.errors;

import bloomingblooms.errors.BaseException;
import bloomingblooms.errors.ErrorCode;

public class ProductNotFoundException extends BaseException {

  private static final String message = "상품이 존재하지 않습니다.";

  public ProductNotFoundException() {
    super(ErrorCode.COMMON_ENTITY_NOT_FOUND);
  }
}
