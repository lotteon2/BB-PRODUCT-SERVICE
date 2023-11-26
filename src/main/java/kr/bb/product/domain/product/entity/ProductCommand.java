package kr.bb.product.domain.product.entity;

import java.util.List;
import javax.validation.constraints.NotNull;
import kr.bb.product.domain.product.entity.mapper.ProductMapper;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import lombok.Builder;
import lombok.Getter;

public class ProductCommand {
  @Getter
  public enum SortOption {
    SALE("product_sale_amount"),
    NEW("created_at"),
    PRICE("product_price"),
    REVIEW("review_count"),
    RATING("average_rating");

    private final String sortOption;

    SortOption(String sortOption) {
      this.sortOption = sortOption;
    }

  }

  @Builder
  @Getter
  public static class ProductRegister {
    @NotNull private String productName;
    @NotNull private String productSummary;
    @NotNull private String productDescriptionImage;
    @NotNull private String productThumbnail;
    @NotNull private Long productPrice;
    private Long categoryId;
    private Long storeId;
    private List<Long> productTag;
    @NotNull private ProductFlowersRequestData representativeFlower;
    private List<ProductFlowersRequestData> flowers;

    public void setStoreId(Long storeId) {
      this.storeId = storeId;
    }
  }

  @Builder
  @Getter
  public static class ProductUpdate {
    @NotNull private String productName;
    @NotNull private String productSummary;
    @NotNull private String productDescriptionImage;
    @NotNull private String productThumbnail;
    @NotNull private Long productPrice;
    private ProductSaleStatus productSaleStatus;
    private Long categoryId;
    private List<Long> productTag;
    @NotNull private ProductFlowersRequestData representativeFlower;
    private List<ProductFlowersRequestData> flowers;
  }

  @Getter
  @Builder
  public static class ProductListItem {
    @Builder.Default private Boolean isLiked = false;
    private String productId;
    private String productName;
    private String productSummary;
    private String productThumbnail;
    private Long productPrice;
    private Long salesCount;
    private Long reviewCount;
  }

  @Builder
  @Getter
  public static class ProductList {
    private List<ProductListItem> products;
    private int totalCnt;

    public static List<ProductListItem> fromEntity(List<Product> products) {
      return ProductMapper.INSTANCE.entityToProductsByCategory(products);
    }

    public static ProductList getData(List<ProductListItem> products, int totalCnt) {
      return ProductList.builder().products(products).totalCnt(totalCnt).build();
    }
  }
}
