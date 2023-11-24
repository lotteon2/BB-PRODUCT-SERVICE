package kr.bb.product.domain.product.api.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductRequestData {
  @NotNull private String productName;
  @NotNull private String productSummary;
  @NotNull private String productDescriptionImage;
  @NotNull private String productThumbnail;
  @NotNull private Long productPrice;
  private ProductSaleStatus productSaleStatus;
  private Long categoryId;
  private Long storeId;
  private List<Long> productTag;
  @NotNull private ProductFlowersRequestData representativeFlower;
  private List<ProductFlowersRequestData> flowers;

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }

  @TestOnly
  public void setProductSaleStatus(ProductSaleStatus productSaleStatus) {
    this.productSaleStatus = productSaleStatus;
  }
}
