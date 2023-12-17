package kr.bb.product.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IsProductPriceValid {
  private String productId;
  private Long productPrice;
}
