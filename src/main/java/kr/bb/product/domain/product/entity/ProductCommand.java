package kr.bb.product.domain.product.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.domain.category.entity.CategoryCommand.CategoryForProductList;
import kr.bb.product.domain.flower.entity.Flower;
import kr.bb.product.domain.product.entity.mapper.ProductMapper;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.tag.entity.Tag;
import kr.bb.product.domain.tag.entity.TagCommand.TagForProductList;
import lombok.Builder;
import lombok.Getter;

public class ProductCommand {
  @Getter
  public enum SortOption {
    SALE("productSaleAmount"),
    NEW("createdAt"),
    LOW("productPrice"),
    HIGH("productPrice"),
    REVIEW("reviewCount"),
    RATING("averageRating");

    private final String sortOption;

    SortOption(String sortOption) {
      this.sortOption = sortOption;
    }
  }

  @Getter
  public enum SelectOption {
    RECOMMEND("productSaleAmount"),
    NEW_ARRIVAL("createdAt"),
    RATING("averageRating");
    private final String selectOption;

    SelectOption(String selectOption) {
      this.selectOption = selectOption;
    }
  }

  @Getter
  @Builder
  public static class ResaleCheckRequest {
    private String productId;
    private String productName;
  }

  @Getter
  @Builder
  public static class StoreManagerSubscriptionProduct {
    private String productId;
    private Double averageRating;
    private String productName;
    private String productSummary;
    private Long productPrice;
    private String productDescriptionImage;
    private String productThumbnail;

    public static StoreManagerSubscriptionProduct getData(Product subscriptionProductByStoreId) {
      return ProductMapper.INSTANCE.getStoreSubscriptionProduct(subscriptionProductByStoreId);
    }
  }

  @Builder
  @Getter
  public static class ProductRegister {
    private String productName;
    private String productSummary;
    private String productDescriptionImage;
    private String productThumbnail;
    private Long productPrice;
    private Long categoryId;
    private Long storeId;
    private List<Long> productTag;
    private ProductFlowersRequestData representativeFlower;
    private List<ProductFlowersRequestData> flowers;

    public void setStoreId(Long storeId) {
      this.storeId = storeId;
    }
  }

  @Builder
  @Getter
  public static class ProductUpdate {
    private String productName;
    private String productSummary;
    private String productDescriptionImage;
    private String productThumbnail;
    private Long productPrice;
    private ProductSaleStatus productSaleStatus;
    private Long categoryId;
    private List<Long> productTag;
    private ProductFlowersRequestData representativeFlower;
    private List<ProductFlowersRequestData> flowers;
  }

  @Getter
  @Builder
  public static class ProductListItem {
    @Builder.Default private Boolean isLiked = false;
    private String key;
    private String productName;
    private String productSummary;
    private String productThumbnail;
    private Long productPrice;
    private Long salesCount;
    private Long reviewCount;
    private Long averageRating;

    public static List<String> getProductIds(List<ProductListItem> productListItem) {
      return productListItem.stream().map(ProductListItem::getKey).collect(Collectors.toList());
    }

    public void setLiked(Boolean liked) {
      isLiked = liked;
    }
  }

  @Builder
  @Getter
  public static class ProductList {
    private List<ProductListItem> products;
    private int totalCnt;

    public static List<ProductListItem> fromEntity(List<Product> products) {
      return ProductMapper.INSTANCE.entityToList(products);
    }

    public static ProductList getData(
        List<ProductListItem> productListItem, List<String> data, int totalPages) {
      for (ProductListItem p : productListItem) {
        if (data.contains(p.key)) p.setLiked(true);
      }
      return ProductList.builder().products(productListItem).totalCnt(totalPages).build();
    }

    public static ProductList getData(List<ProductListItem> productListItem, int totalPages) {
      return ProductList.builder().products(productListItem).totalCnt(totalPages).build();
    }
  }

  @Getter
  @Builder
  public static class ProductsGroupByCategory {
    @Builder.Default private Map<Long, ProductList> products = new HashMap<>();

    public void setProducts(Long categoryId, ProductList productList) {
      this.products.put(categoryId, productList);
    }
  }

  @Builder
  @Getter
  public static class ProductDetail {
    private String productId;
    private String productName;
    private String productDescription;
    private String productThumbnail;
    private String productDetailImage;
    private Long productPrice;
    private ProductSaleStatus productSaleStatus;
    private Long salesCount;
    private Double averageRating;
    private String storeName;
    private Long storeId;
    private Long reviewCount;
    @Builder.Default private Boolean isLiked = false;
    private CategoryForProductList category;
    private List<TagForProductList> tag;

    public static ProductDetail fromEntity(Product product) {
      return ProductMapper.INSTANCE.entityToDetail(product);
    }

    public void setStoreName(String storeName) {
      this.storeName = storeName;
    }

    public void setLiked(Boolean liked) {
      isLiked = liked;
    }
  }

  @Getter
  @Builder
  public static class StoreName {
    private String storeName;
  }

  @Getter
  @Builder
  public static class ProductDetailLike {
    private Boolean isLiked;
  }

  @Getter
  @Builder
  public static class SubscriptionProduct {
    private String productName;
    private String productSummary;
    private Long productPrice;
    private String productDescriptionImage;
    private String productThumbnail;
    private Boolean isSubscription;

    public static Product toEntity(SubscriptionProduct subscriptionProduct, Long storeId) {
      subscriptionProduct.isSubscription = true;
      return ProductMapper.INSTANCE.subscriptionToEntity(subscriptionProduct, storeId);
    }
  }

  @Getter
  @Builder
  public static class UpdateSubscriptionProduct {
    private String productName;
    private String productSummary;
    private Long productPrice;
    private String productDescriptionImage;
    private String productThumbnail;
    private String productId;

    public void setProductId(String productId) {
      this.productId = productId;
    }
  }

  @Getter
  @Builder
  public static class ProductDetailFlower {
    private Long flowerId;
    private String flowerName;
    private Long flowerCount;

    public static ProductDetailFlower getData(ProductFlowers item) {
      return ProductDetailFlower.builder()
          .flowerId(item.getFlowerId())
          .flowerCount(item.getFlowerCount())
          .build();
    }

    public void setFlowerName(String flowerName) {
      this.flowerName = flowerName;
    }
  }

  @Getter
  @Builder
  public static class StoreProductDetail {
    private String productId;
    private String productThumbnail;
    private String productName;
    private String productSummary;
    private Long productPrice;
    private String category;
    private List<String> tag;
    private String productDescriptionImage;
    private Long productSaleAmount;
    private Double averageRating;
    private String productSaleStatus;
    private ProductDetailFlower representativeFlower;
    private List<ProductDetailFlower> flowers;

    public static StoreProductDetail fromEntity(Product product, List<Flower> flowers) {
      List<String> tagNames =
          product.getTag().stream().map(Tag::getTagName).collect(Collectors.toList());
      Map<Long, String> flowerName =
          flowers.stream().collect(Collectors.toMap(Flower::getId, Flower::getFlowerName));

      List<ProductDetailFlower> flowerList = new ArrayList<>();
      ProductDetailFlower representativeFlower = null;

      for (ProductFlowers item : product.getProductFlowers()) {
        ProductDetailFlower data = ProductDetailFlower.getData(item);
        data.setFlowerName(flowerName.get(item.getFlowerId()));

        if (item.getIsRepresentative()) representativeFlower = data;
        else flowerList.add(data);
      }

      return StoreProductDetail.builder()
          .productId(product.getProductId())
          .productThumbnail(product.getProductThumbnail())
          .productName(product.getProductName())
          .productSummary(product.getProductSummary())
          .productPrice(product.getProductPrice())
          .category(product.getCategory().getCategoryName())
          .tag(tagNames)
          .productDescriptionImage(product.getProductDescriptionImage())
          .productSaleAmount(product.getProductSaleAmount())
          .averageRating(product.getAverageRating())
          .productSaleStatus(product.getProductSaleStatus().getMessage())
          .representativeFlower(representativeFlower)
          .flowers(flowerList)
          .build();
    }
  }

  @Getter
  @Builder
  public static class StoreProduct {
    private String key;
    private String productThumbnail;
    private String productName;
    private String representativeFlower;
    private String category;
    private Long productPrice;
    private Long productSaleAmount;
    private Double averageRating;
    private String productSaleStatus;

    public static StoreProduct fromEntity(Product product, String representativeFlower) {
      return StoreProduct.builder()
          .averageRating(product.getAverageRating())
          .category(product.getCategory().getCategoryName())
          .key(product.getProductId())
          .productName(product.getProductName())
          .productPrice(product.getProductPrice())
          .productSaleAmount(product.getProductSaleAmount())
          .productSaleStatus(product.getProductSaleStatus().getMessage())
          .productThumbnail(product.getProductThumbnail())
          .representativeFlower(representativeFlower)
          .build();
    }
  }

  @Getter
  @Builder
  public static class StoreProductList {
    private List<StoreProduct> products;
    private int totalCnt;
  }

  @Getter
  @Builder
  public static class BestSellerTopTenItem {
    private String productName;
    private List<Long> data;
  }

  @Getter
  @Builder
  public static class BestSellerTopTen {
    private List<BestSellerTopTenItem> products;

    public static BestSellerTopTen getData(List<Product> bestSellerTopTen) {
      return BestSellerTopTen.builder()
          .products(
              bestSellerTopTen.stream()
                  .map(
                      item ->
                          BestSellerTopTenItem.builder()
                              .productName(item.getProductName())
                              .data(List.of(item.getProductSaleAmount()))
                              .build())
                  .collect(Collectors.toList()))
          .build();
    }
  }

  @Getter
  @Builder
  public static class MainPageProductItem {
    @Builder.Default private Boolean isLiked = false;
    private String key;
    private String productName;
    private String productSummary;
    private String productThumbnail;
    private Long productPrice;
    private Long productAverageRating;

    public void setLiked(Boolean liked) {
      isLiked = liked;
    }
  }

  @Getter
  @Builder
  public static class MainPageProductItems {
    private List<MainPageProductItem> products;

    public static MainPageProductItems getData(
        List<Product> mainPageProducts, List<String> productsIsLiked) {
      List<MainPageProductItem> items = getMainPageProductItems(mainPageProducts);
      return MainPageProductItems.builder()
          .products(
              items.stream()
                  .peek(item -> item.setLiked(productsIsLiked.contains(item.getKey())))
                  .collect(Collectors.toList()))
          .build();
    }

    public static MainPageProductItems getData(List<Product> mainPageProducts) {
      List<MainPageProductItem> items = getMainPageProductItems(mainPageProducts);
      return MainPageProductItems.builder().products(items).build();
    }

    private static List<MainPageProductItem> getMainPageProductItems(
        List<Product> mainPageProducts) {
      return ProductMapper.INSTANCE.getMainPageProducts(mainPageProducts);
    }
  }
}
