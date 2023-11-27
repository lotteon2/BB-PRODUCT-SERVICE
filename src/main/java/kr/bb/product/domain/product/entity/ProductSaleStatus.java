package kr.bb.product.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProductSaleStatus {
  SALE("판매 중"),
  DISCONTINUED("판매 중지"),
  DELETED("상품 삭제");
  private final String message;

  ProductSaleStatus(String message) {
    this.message = message;
  }
}
