package kr.bb.product.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductInformation {
  private String productId;
  private String productName;
  private String productThumbnail;
}
