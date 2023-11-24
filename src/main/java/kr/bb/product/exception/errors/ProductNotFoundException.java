package kr.bb.product.exception.errors;

import kr.bb.product.exception.errors.common.EntityNotFoundException;

public class ProductNotFoundException extends EntityNotFoundException {

  private static final String message = "상품이 존재하지 않습니다.";

  public ProductNotFoundException() {
    super(message);
  }
}
