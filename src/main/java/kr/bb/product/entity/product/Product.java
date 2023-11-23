package kr.bb.product.entity.product;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import kr.bb.product.entity.ProductSaleStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Document(collation = "product")
public class Product {
  @Id
  @NotBlank
  @Field(name = "product_id")
  private String productId;

  private @Field(name = "category") Category category;

  @NotBlank
  private @Field(name = "product_name") String productName;

  @NotBlank
  private @Field(name = "product_summary") String productSummary;

  @NotBlank
  private @Field(name = "product_price") Long productPrice;

  @Builder.Default
  @NotBlank
  private @Field(name = "product_sale_status") ProductSaleStatus productSaleStatus =
      ProductSaleStatus.SALE;

  private @Field(name = "tag") List<Tag> tag;
  private @Field(name = "product_flowers") List<Flowers> productFlowers;

  @NotBlank
  private @Field(name = "product_description_image") String productDescriptionImage;

  @NotBlank
  private @Field(name = "review_count") Long reviewCount;

  @Builder.Default
  @NotBlank
  @Field(name = "product_sale_amount")
  private Long productSaleAmount = 0L;

  @Builder.Default
  @NotBlank
  private @Field(name = "average_rating") Double averageRating = 0.0;

  @NotBlank
  private @Field(name = "store_id") Long storeId;

  @NotBlank
  @CreatedDate
  @Field(name = "created_at")
  private LocalDateTime createdAt;

  @NotBlank
  @LastModifiedDate
  @Field(name = "updated_at")
  private LocalDateTime updatedAt;

  @NotBlank
  @Builder.Default
  @Field(name = "is_deleted")
  private Boolean isDeleted = false;
}
