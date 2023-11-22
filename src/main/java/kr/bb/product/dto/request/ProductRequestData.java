package kr.bb.product.dto.request;

import javax.validation.constraints.NotBlank;
import kr.bb.product.dto.category.Category;
import kr.bb.product.dto.tag.Tag;
import kr.bb.product.entity.Flowers;
import kr.bb.product.entity.ProductSaleStatus;
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
  private Category category;
  @NotBlank private String productName;
  @NotBlank private String productSummary;
  @NotBlank private Long productPrice;
  @NotBlank private ProductSaleStatus productSaleStatus;
  private Tag tag;
  private Flowers productFlowers;
  @NotBlank private String productDescriptionImage;
  @NotBlank private Long reviewCount;
  @Builder.Default @NotBlank private Long productSaleAmount = 0L;
  @NotBlank private Double averageRating;
  @NotBlank private Long storeId;
}
