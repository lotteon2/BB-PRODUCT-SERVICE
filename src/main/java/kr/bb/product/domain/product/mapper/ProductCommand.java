package kr.bb.product.domain.product.mapper;

import bloomingblooms.domain.flower.StockChangeDto;
import bloomingblooms.domain.flower.StockDto;
import bloomingblooms.domain.order.ProcessOrderDto;
import bloomingblooms.domain.product.ProductInfoDto;
import bloomingblooms.domain.product.ProductInformation;
import bloomingblooms.domain.product.ProductThumbnail;
import bloomingblooms.domain.product.StoreSubscriptionProductId;
import bloomingblooms.domain.store.StorePolicy;
import bloomingblooms.domain.wishlist.cart.CartProductItemInfo;
import bloomingblooms.domain.wishlist.cart.GetUserCartItemsResponse;
import bloomingblooms.domain.wishlist.likes.LikedProductInfoResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.domain.category.entity.CategoryCommand;
import kr.bb.product.domain.category.entity.CategoryCommand.CategoryDetail;
import kr.bb.product.domain.flower.entity.Flower;
import kr.bb.product.domain.flower.mapper.FlowerCommand;
import kr.bb.product.domain.flower.mapper.FlowerCommand.ProductFlowers;
import kr.bb.product.domain.flower.mapper.FlowerCommand.ProductFlowersRequestData;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.mapper.mapper.ProductMapper;
import kr.bb.product.domain.tag.entity.TagCommand;
import kr.bb.product.domain.tag.entity.TagCommand.TagForProductList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductCommand {

  public static List<ProductInformation> getProductInformationListData(
      List<Product> productByProductIds) {
    return productByProductIds.stream()
        .map(
            item ->
                ProductInformation.builder()
                    .productId(item.getProductId())
                    .productName(item.getProductName())
                    .productThumbnail(item.getProductThumbnail())
                    .build())
        .collect(Collectors.toList());
  }

  public static ProductThumbnail getProductThumbnailData(Product byProductId) {
    return ProductThumbnail.builder().productThumbnail(byProductId.getProductThumbnail()).build();
  }

  public static StoreSubscriptionProductId getStoreSubscriptionProductIdData(
      Product subscriptionProductByStoreId) {
    return StoreSubscriptionProductId.builder()
        .subscriptionProductId(subscriptionProductByStoreId.getProductId())
        .build();
  }

  public static ProductInfoDto getSubscriptionProductInformationData(Product product) {
    return ProductInfoDto.builder()
        .productName(product.getProductName())
        .productThumbnail(product.getProductThumbnail())
        .unitPrice(product.getProductPrice())
        .storeId(product.getStoreId())
        .build();
  }

  public static List<LikedProductInfoResponse> getProductInformationForLikesData(
      List<Product> productByProductIds) {
    return productByProductIds.stream()
        .map(
            item ->
                LikedProductInfoResponse.builder()
                    .productName(item.getProductName())
                    .productId(item.getProductId())
                    .averageRating(item.getAverageRating().floatValue())
                    .productThumbnail(item.getProductThumbnail())
                    .productPrice(item.getProductPrice())
                    .productSummary(item.getProductSummary())
                    .build())
        .collect(Collectors.toList());
  }

  public static List<Long> getStoreIds(List<Product> productByProductIds) {
    return productByProductIds.stream().map(Product::getStoreId).collect(Collectors.toList());
  }

  public static GetUserCartItemsResponse getUserCartItemResponse(
      Map<String, Long> productIds,
      Map<Long, List<Product>> productsByProductIdsForCartItem,
      Map<Long, StorePolicy> storePolicies) {
    List<CartProductItemInfo> collect =
        productsByProductIdsForCartItem.keySet().stream()
            .map(
                item ->
                    CartProductItemInfo.builder()
                        .storeId(item)
                        .storeName(storePolicies.get(item).getStoreName())
                        .freeDeliveryMinCost(storePolicies.get(item).getFreeDeliveryMinCost())
                        .deliveryCost(storePolicies.get(item).getDeliveryCost())
                        .productInfoList(
                            ProductCommand.getProductInfoDtoForCart(
                                productsByProductIdsForCartItem.get(item), productIds))
                        .build())
            .collect(Collectors.toList());
    return GetUserCartItemsResponse.builder().cartProductItemInfoList(collect).build();
  }

  private static List<bloomingblooms.domain.wishlist.cart.ProductInfoDto> getProductInfoDtoForCart(
      List<Product> products, Map<String, Long> productIds) {
    return products.stream()
        .map(
            item ->
                bloomingblooms.domain.wishlist.cart.ProductInfoDto.builder()
                    .productName(item.getProductName())
                    .productThumbnailImage(item.getProductThumbnail())
                    .price(item.getProductPrice())
                    .productId(item.getProductId())
                    .quantity(productIds.get(item.getProductId()))
                    .build())
        .collect(Collectors.toList());
  }

  public static List<StockChangeDto> getFlowerAmountOfStore(
      Map<Long, List<Product>> productsByProductsGroupByStoreId, ProcessOrderDto processOrderDto) {
    Map<Long, List<StockDto>> stockDtos = new HashMap<>();

    for (Long key : productsByProductsGroupByStoreId.keySet()) {
      Map<Long, Long> map = new HashMap<>();
      List<StockDto> list = new ArrayList<>();
      for (Product p : productsByProductsGroupByStoreId.get(key)) {
        p.getProductFlowers()
            .forEach(
                item ->
                    map.merge(
                        item.getFlowerId(),
                        item.getFlowerCount() * processOrderDto.getProducts().get(p.getProductId()),
                        Long::sum));
      }
      for (Long flower : map.keySet()) {
        list.add(StockDto.builder().flowerId(flower).stock(map.get(flower)).build());
      }
      stockDtos.put(key, list);
    }
    List<StockChangeDto> stockChangeDtos = new ArrayList<>();
    for (Long key : stockDtos.keySet()) {
      stockChangeDtos.add(
          StockChangeDto.builder()
              .phoneNumber(processOrderDto.getPhoneNumber())
              .stockDtos(stockDtos.get(key))
              .storeId(key)
              .userId(processOrderDto.getUserId())
              .build());
    }

    return stockChangeDtos;
  }

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
    private FlowerCommand.ProductFlowersRequestData representativeFlower;
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
    private List<Long> productTag;
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
    private ProductSaleStatus productSaleStatus;

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
    private long totalCnt;

    public static List<ProductListItem> fromEntity(List<Product> products) {
      return ProductMapper.INSTANCE.entityToList(products);
    }

    public static ProductList getData(
        List<ProductListItem> productListItem, List<String> data, long totalElements) {
      for (ProductListItem p : productListItem) {
        if (data.contains(p.key)) {
          p.setLiked(true);
        }
      }
      return ProductList.builder().products(productListItem).totalCnt(totalElements).build();
    }

    public static ProductList getData(List<ProductListItem> productListItem, long totalElements) {
      return ProductList.builder().products(productListItem).totalCnt(totalElements).build();
    }
  }

  @Getter
  @Builder
  public static class ProductsGroupByCategory {

    @Builder.Default private Map<Long, ProductList> products = new HashMap<>();
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
    private CategoryCommand.CategoryDetail category;
    private List<TagForProductList> tag;

    public static ProductDetail fromEntity(Product product) {
      return ProductMapper.INSTANCE.entityToDetail(product);
    }

    public static ProductDetail getData(
        ProductDetail productDetail, String storeName, Long reviewCnt) {
      return ProductMapper.INSTANCE.getProductDetail(productDetail, storeName, reviewCnt);
    }

    public static ProductDetail getData(
        ProductDetail productDetail, String storeName, Long reviewCnt, ProductDetailLike isLiked) {
      return ProductMapper.INSTANCE.getProductDetail(productDetail, storeName, reviewCnt, isLiked);
    }
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
    private CategoryDetail category;
    private List<TagForProductList> tag;
    private String productDescriptionImage;
    private Long productSaleAmount;
    private Double averageRating;
    private ProductSaleStatus productSaleStatus;
    private ProductDetailFlower representativeFlower;
    private List<ProductDetailFlower> flowers;

    public static StoreProductDetail fromEntity(Product product, List<Flower> flowers) {
      List<TagForProductList> tagList = TagCommand.entityToTagList(product.getTag());

      Map<Long, String> flowerName =
          flowers.stream().collect(Collectors.toMap(Flower::getId, Flower::getFlowerName));

      List<ProductDetailFlower> flowerList = new ArrayList<>();
      ProductDetailFlower representativeFlower = null;

      for (ProductFlowers item : product.getProductFlowers()) {
        ProductDetailFlower data = ProductDetailFlower.getData(item);
        data.setFlowerName(flowerName.get(item.getFlowerId()));

        if (item.getIsRepresentative()) {
          representativeFlower = data;
        } else {
          flowerList.add(data);
        }
      }

      return StoreProductDetail.builder()
          .productId(product.getProductId())
          .productThumbnail(product.getProductThumbnail())
          .productName(product.getProductName())
          .productSummary(product.getProductSummary())
          .productPrice(product.getProductPrice())
          .category(CategoryDetail.getData(product.getCategory()))
          .tag(tagList)
          .productDescriptionImage(product.getProductDescriptionImage())
          .productSaleAmount(product.getProductSaleAmount())
          .averageRating(product.getAverageRating())
          .productSaleStatus(product.getProductSaleStatus())
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
    private Long category;
    private Long productPrice;
    private Long productSaleAmount;
    private Double averageRating;
    private ProductSaleStatus productSaleStatus;

    public static StoreProduct fromEntity(Product product, String representativeFlower) {
      return StoreProduct.builder()
          .averageRating(product.getAverageRating())
          .category(product.getCategory().getCategoryId())
          .key(product.getProductId())
          .productName(product.getProductName())
          .productPrice(product.getProductPrice())
          .productSaleAmount(product.getProductSaleAmount())
          .productSaleStatus(product.getProductSaleStatus())
          .productThumbnail(product.getProductThumbnail())
          .representativeFlower(representativeFlower)
          .build();
    }
  }

  @Getter
  @Builder
  public static class StoreProductList {

    private List<StoreProduct> products;
    private long totalCnt;
  }

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  public static class BestSellerTopTenItem {

    private String name;
    private List<Long> data;
  }

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  public static class BestSellerTopTen {

    private List<BestSellerTopTenItem> products;

    public static BestSellerTopTen getData(List<Product> bestSellerTopTen) {
      return BestSellerTopTen.builder()
          .products(
              bestSellerTopTen.stream()
                  .map(
                      item ->
                          BestSellerTopTenItem.builder()
                              .name(item.getProductName())
                              .data(List.of(item.getProductSaleAmount()))
                              .build())
                  .collect(Collectors.toList()))
          .build();
    }
  }

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
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
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
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

  @Getter
  @Builder
  public static class SubscriptionProductForCustomer {

    private String productId;
    private String productName;
    private String productSummary;
    private String productThumbnail;
    private String productDetailImage;
    private Long productPrice;
    private ProductSaleStatus productSaleStatus;
    private Long salesCount;
    private Double averageRating;
    private String storeName;
    private Long reviewCount;
    private Long storeId;
    @Builder.Default private Boolean isLiked = false;

    public static SubscriptionProductForCustomer getData(
        ProductDetailLike data, Product subscriptionProductByStoreId) {
      SubscriptionProductForCustomer product =
          ProductMapper.INSTANCE.getSubscriptionProductDetail(subscriptionProductByStoreId);
      product.setLiked(data.getIsLiked());
      return product;
    }

    public static SubscriptionProductForCustomer getData(Product subscriptionProductByStoreId) {
      return ProductMapper.INSTANCE.getSubscriptionProductDetail(subscriptionProductByStoreId);
    }

    public void setLiked(Boolean liked) {
      isLiked = liked;
    }
  }

  @Getter
  @Builder
  public static class LanguageOfFlower {

    private String languageOfFlower;

    public static LanguageOfFlower getData(Flower languageOfFlowerByFlowerId) {
      return LanguageOfFlower.builder()
          .languageOfFlower(languageOfFlowerByFlowerId.getLanguageOfFlower())
          .build();
    }
  }

  @Getter
  @Builder
  public static class RepresentativeFlowerId {

    private Long flowerId;

    public static RepresentativeFlowerId getData(Long flowerId) {
      return RepresentativeFlowerId.builder().flowerId(flowerId).build();
    }
  }
}
