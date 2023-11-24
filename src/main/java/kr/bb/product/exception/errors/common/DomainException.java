package kr.bb.product.exception.errors.common;

public class DomainException extends BaseException {

  public DomainException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }
}
