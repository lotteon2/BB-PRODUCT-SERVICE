package kr.bb.product.dto.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import kr.bb.product.vo.FlowersRequestData;
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
  @NotBlank private String productName;
  @NotBlank private String productSummary;
  @NotBlank private String productDescriptionImage;
  @NotBlank private String productThumbnail;
  @NotBlank private Long productPrice;
  private Long categoryId;
  @NotBlank private Long storeId;
  private List<Long> productTag;
  @NotBlank private FlowersRequestData representativeFlower;
  private List<FlowersRequestData> flowers;

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }
}
