package kr.bb.product.exception.errors.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
  CATEGORY_NOT_FOUND("no match category", 400),
  PRODUCT_NOT_FOUND("no match product", 400),
  REVIEW_NOT_FOUND("리뷰가 존재하지 않습니다.", 400);
  private final String message;
  private final Integer errorCode;

  ErrorCode(String message, Integer errorCode) {
    this.message = message;
    this.errorCode = errorCode;
  }

  public String getErrorMsg(Object... arg) {
    return String.format(message, arg);
  }
}
