package kr.bb.product.entity;

import java.time.LocalDateTime;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import kr.bb.product.dto.category.Category;
import kr.bb.product.dto.tag.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Document(collation = "product")
public class Product {
  @Id @NotBlank private String productId;

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

  @NotBlank @CreatedDate private LocalDateTime createdAt;

  @NotBlank @LastModifiedDate private LocalDateTime updatedAt;

  @NotBlank private Boolean isDeleted;
}
