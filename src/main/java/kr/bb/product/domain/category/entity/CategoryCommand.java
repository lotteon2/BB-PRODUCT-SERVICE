package kr.bb.product.domain.category.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryCommand {
  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CategoryForProductList {
    private Long categoryId;
    private String categoryName;
  }

  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CategoryDetail {
    private Long categoryId;
    private String categoryName;

    public static CategoryDetail getData(Category category) {
      return CategoryDetail.builder()
          .categoryId(category.getCategoryId())
          .categoryName(category.getCategoryName())
          .build();
    }
  }
}
