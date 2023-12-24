package kr.bb.product.domain.flower.mapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class FlowerCommand {
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @ToString
  public static class ProductFlowers {
    private Long flowerId;
    private Long flowerCount;
    private String flowerName;
    @Builder.Default private Boolean isRepresentative = false;
  }

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  public static class ProductFlowersRequestData {
    private Long flowerId;
    private Long flowerCount;
  }
}
