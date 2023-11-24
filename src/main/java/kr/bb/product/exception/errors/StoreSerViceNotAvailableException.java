package kr.bb.product.exception.errors;

import kr.bb.product.exception.errors.common.DomainException;
import kr.bb.product.exception.errors.common.ErrorCode;

public class StoreSerViceNotAvailableException extends DomainException {
  private static final String message = "store service is not available";

  public StoreSerViceNotAvailableException() {
    super(message, ErrorCode.SERVICE_NOT_AVAILABLE);
  }
}
