package kr.bb.product.common.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FlowerInformation {
  private Long flowerId;
  private String flowerName;
}
