package kr.bb.product.entity;

import lombok.Getter;

@Getter
public enum ProductSaleStatus {
  SALE("판매 중"),
  DISCONTINUED("판매 중지");
  private final String message;

  ProductSaleStatus(String message) {
    this.message = message;
  }
}
