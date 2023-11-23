package kr.bb.product.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductRequestData {
  private String productName;
  private String productSummary;
  private String productDescriptionImage;
  private String productThumbnail;
  private Long productPrice;
  private Long categoryId;
  private Long storeId;
  private List<Long> productTag;
  private FlowersRequestData representativeFlower;
  private List<FlowersRequestData> flowers;

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }
}
