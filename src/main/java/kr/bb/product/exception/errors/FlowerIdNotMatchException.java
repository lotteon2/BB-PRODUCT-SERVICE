package kr.bb.product.exception.errors;

import bloomingblooms.errors.BaseException;
import bloomingblooms.errors.ErrorCode;

public class FlowerIdNotMatchException extends BaseException {

  private static final String message = "잘못된 꽃 id 요청입니다.";

  public FlowerIdNotMatchException() {
    super(message, ErrorCode.COMMON_ENTITY_NOT_FOUND);
  }
}
