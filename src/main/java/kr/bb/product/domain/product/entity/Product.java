package kr.bb.product.domain.product.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.tag.entity.Tag;
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
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Document
public class Product {
  @MongoId(value = FieldType.DECIMAL128)
  @NotBlank
  @Field(name = "product_id")
  private String productId;

  @Field(name = "category")
  private Category category;

  @NotBlank
  @Field(name = "product_name")
  private String productName;

  @NotBlank
  @Field(name = "product_summary")
  private String productSummary;

  @NotBlank
  @Field(name = "product_price")
  private Long productPrice;

  @Builder.Default
  @NotBlank
  @Field(name = "product_sale_status")
  private ProductSaleStatus productSaleStatus = ProductSaleStatus.SALE;

  @Field(name = "tag")
  private List<Tag> tag;

  @Field(name = "product_flowers")
  private List<ProductFlowers> productFlowers;

  @NotNull
  @Field(name = "product_thumbnail")
  private String productThumbnail;

  @NotBlank
  @Field(name = "product_description_image")
  private String productDescriptionImage;

  @NotBlank
  private @Field(name = "review_count") Long reviewCount;

  @Builder.Default
  @NotBlank
  @Field(name = "product_sale_amount")
  private Long productSaleAmount = 0L;

  @Builder.Default
  @NotBlank
  @Field(name = "average_rating")
  private Double averageRating = 0.0;

  @NotBlank
  @Field(name = "store_id")
  private Long storeId;

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

  public void updateSaleStatus(ProductSaleStatus productSaleStatus) {
    this.productSaleStatus = productSaleStatus;
  }
}
