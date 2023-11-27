package kr.bb.product.domain.tag.entity;

import lombok.Builder;
import lombok.Getter;

public class TagCommand {
  @Getter
  @Builder
  public static class TagForProductList {
    private Long key;
    private String tagName;
  }
}
