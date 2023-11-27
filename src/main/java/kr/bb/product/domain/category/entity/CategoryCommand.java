package kr.bb.product.domain.category.entity;

import lombok.Builder;
import lombok.Getter;

public class CategoryCommand {
  @Builder
  @Getter
  public static class CategoryForProductList {
    private Long categoryId;
    private String categoryName;
  }
}
