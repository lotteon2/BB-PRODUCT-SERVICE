package kr.bb.product.domain.category.entity;

import lombok.Builder;
import lombok.Getter;

public class CategoryCommand {
  @Builder
  @Getter
  public static class CategoryForProductList {
    private Long key;
    private String categoryName;
  }
}
