package kr.bb.product.domain.product.application.port.in;

import bloomingblooms.domain.product.IsProductPriceValid;
import bloomingblooms.domain.product.ProductInformation;
import bloomingblooms.domain.product.ProductThumbnail;
import bloomingblooms.domain.product.StoreSubscriptionProductId;
import bloomingblooms.domain.product.SubscriptionProductInformation;
import bloomingblooms.domain.wishlist.cart.GetUserCartItemsResponse;
import bloomingblooms.errors.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.common.dto.StorePolicy;
import kr.bb.product.domain.flower.adapter.out.jpa.FlowerJpaRepository;
import kr.bb.product.domain.flower.application.port.out.FlowerQueryOutPort;
import kr.bb.product.domain.flower.entity.Flower;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.BestSellerTopTen;
import kr.bb.product.domain.product.entity.ProductCommand.LanguageOfFlower;
import kr.bb.product.domain.product.entity.ProductCommand.MainPageProductItems;
import kr.bb.product.domain.product.entity.ProductCommand.ProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.ProductDetailLike;
import kr.bb.product.domain.product.entity.ProductCommand.ProductInformationForLikes;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
import kr.bb.product.domain.product.entity.ProductCommand.ProductListItem;
import kr.bb.product.domain.product.entity.ProductCommand.ProductsGroupByCategory;
import kr.bb.product.domain.product.entity.ProductCommand.RepresentativeFlowerId;
import kr.bb.product.domain.product.entity.ProductCommand.SelectOption;
import kr.bb.product.domain.product.entity.ProductCommand.SortOption;
import kr.bb.product.domain.product.entity.ProductCommand.StoreManagerSubscriptionProduct;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProduct;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductList;
import kr.bb.product.domain.product.entity.ProductCommand.SubscriptionProductForCustomer;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.infrastructure.client.StoreServiceClient;
import kr.bb.product.domain.product.infrastructure.client.WishlistServiceClient;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
import kr.bb.product.exception.errors.ProductPriceValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryInputPort implements ProductQueryUseCase {
  private final WishlistServiceClient wishlistServiceClient;
  private final StoreServiceClient storeServiceClient;

  private final FlowerJpaRepository flowerJpaRepository;

  private final ProductQueryOutPort productQueryOutPort;
  private final FlowerQueryOutPort flowerQueryOutPort;
  private final ReviewQueryOutPort reviewQueryOutPort;
  private final ObjectMapper objectMapper;

  @NotNull
  private static Pageable getPageable(Pageable pageable, ProductCommand.SortOption sortOption) {
    Direction direction = Direction.DESC;
    if (SortOption.LOW.equals(sortOption)) direction = Direction.ASC;
    return PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        Sort.by(direction, sortOption.getSortOption()));
  }

  private static List<ProductListItem> getProduct(Page<Product> byCategory) {
    return ProductList.fromEntity(byCategory.getContent());
  }

  private static ProductDetail getProductDetail(Product byProductId) {
    return ProductDetail.fromEntity(byProductId);
  }

  private static List<String> getProductIdsFromProducts(List<Product> mainPageProducts) {
    return Product.getProductsIds(mainPageProducts);
  }

  private Page<Product> getProductsByCategoryId(Long categoryId, Long storeId, Pageable pageable) {
    return productQueryOutPort.findProductsByCategory(categoryId, storeId, pageable);
  }

  private String getProductDetailStoreName(Product byProductId) {
    return storeServiceClient
        .getStoreNameOfProductDetail(byProductId.getStoreId())
        .getData()
        .getStoreName();
  }

  private Flower getFlowerById(Long flowerId) {
    return flowerJpaRepository.findById(flowerId).orElseThrow(EntityNotFoundException::new);
  }

  private Long getReviewCnt(String productId) {
    return reviewQueryOutPort.findReviewCountByProductId(productId);
  }

  private List<String> getProductsIsLiked(Long userId, List<String> ids) {

    return wishlistServiceClient.getProductsMemberLikes(userId, ids).getData();
  }

  /**
   * 상품 상세 조회 - 로그인 시
   *
   * @param userId
   * @param productId
   * @return
   */
  @Override
  public ProductDetail getProductDetail(Long userId, String productId) {
    Long reviewCnt = getReviewCnt(productId);
    Product byProductId = productQueryOutPort.findByProductId(productId);
    ProductDetail productDetail = getProductDetail(byProductId);
    ProductCommand.ProductDetailLike isLiked =
        wishlistServiceClient.getProductDetailLikes(productId, userId).getData();
    String storeName = getProductDetailStoreName(byProductId);
    return ProductDetail.getData(productDetail, storeName, reviewCnt, isLiked);
  }

  /**
   * 상품 상세 정보 - 비 로그인 시
   *
   * @param productId
   * @return
   */
  @Override
  public ProductDetail getProductDetail(String productId) {
    Long reviewCnt = getReviewCnt(productId);
    Product byProductId = productQueryOutPort.findByProductId(productId);
    ProductDetail productDetail = getProductDetail(byProductId);
    String storeName = getProductDetailStoreName(byProductId);
    return ProductDetail.getData(productDetail, storeName, reviewCnt);
  }

  /**
   * 가게 사장 상품 상세 조회
   *
   * @param storeId
   * @param productId
   * @return
   */
  @Override
  public StoreProductDetail getStoreProductDetail(Long storeId, String productId) {
    Product storeProductByStoreIdAndProductId =
        productQueryOutPort.findStoreProductByStoreIdAndProductId(storeId, productId);
    List<Flower> productDetailFlower =
        flowerQueryOutPort.findProductDetailFlower(
            Product.getFlowerIds(storeProductByStoreIdAndProductId));
    return StoreProductDetail.fromEntity(storeProductByStoreIdAndProductId, productDetailFlower);
  }

  /**
   * 가게 사장 상품
   *
   * @param storeId
   * @return
   */
  @Override
  public StoreProductList getStoreProducts(
      Long storeId,
      Long categoryId,
      Long flowerId,
      ProductSaleStatus saleStatus,
      Pageable pageable) {
    Page<Product> productByStoreId =
        productQueryOutPort.findStoreProducts(storeId, categoryId, flowerId, saleStatus, pageable);

    return StoreProductList.builder()
        .products(
            productByStoreId.getContent().stream()
                .map(
                    product -> {
                      String flowerName =
                          product.getProductFlowers().stream()
                              .filter(ProductFlowers::getIsRepresentative)
                              .findFirst()
                              .map(flowers -> getFlowerById(flowers.getFlowerId()).getFlowerName())
                              .orElse("대표꽃이 없습니다.");
                      return StoreProduct.fromEntity(product, flowerName);
                    })
                .collect(Collectors.toList()))
        .totalCnt(productByStoreId.getTotalPages())
        .build();
  }

  @Override
  public BestSellerTopTen getBestSellerTopTen(Long storeId) {
    List<Product> bestSellerTopTen = productQueryOutPort.findBestSellerTopTen(storeId);
    return BestSellerTopTen.getData(bestSellerTopTen);
  }

  @Override
  public StoreManagerSubscriptionProduct getSubscriptionProductByStoreId(Long storeId) {
    return StoreManagerSubscriptionProduct.getData(
        productQueryOutPort.findSubscriptionProductByStoreId(storeId));
  }

  /**
   * 메인 페이지 상품 리스트 조회 4개 - 비로그인
   *
   * @param selectOption
   * @return
   */
  @Override
  public MainPageProductItems getMainPageProducts(SelectOption selectOption) {
    List<Product> mainPageProducts = productQueryOutPort.findMainPageProducts(selectOption);
    return MainPageProductItems.getData(mainPageProducts);
  }

  /**
   * 메인 페이지 상품 리스트 조회 4개 - 로그인
   *
   * @param userId
   * @param selectOption
   * @return
   */
  @Override
  public MainPageProductItems getMainPageProducts(Long userId, SelectOption selectOption) {
    List<Product> mainPageProducts = productQueryOutPort.findMainPageProducts(selectOption);
    return MainPageProductItems.getData(
        mainPageProducts, getProductsIsLiked(userId, getProductIdsFromProducts(mainPageProducts)));
  }

  /**
   * 구독 상품 상세 조회 - 구매자
   *
   * @param userId
   * @param storeId
   * @return
   */
  @Override
  public SubscriptionProductForCustomer getSubscriptionProductDetail(Long userId, Long storeId) {
    Product subscriptionProductByStoreId =
        productQueryOutPort.findSubscriptionProductByStoreId(storeId);
    ProductDetailLike isLiked =
        wishlistServiceClient
            .getProductDetailLikes(subscriptionProductByStoreId.getProductId(), userId)
            .getData();
    return SubscriptionProductForCustomer.getData(isLiked, subscriptionProductByStoreId);
  }

  /**
   * 구독 상품 상세 조회 - 구매자 비로그인
   *
   * @param storeId
   * @return
   */
  @Override
  public SubscriptionProductForCustomer getSubscriptionProductDetail(Long storeId) {
    Product subscriptionProductByStoreId =
        productQueryOutPort.findSubscriptionProductByStoreId(storeId);
    return SubscriptionProductForCustomer.getData(subscriptionProductByStoreId);
  }

  @Override
  public ProductThumbnail getProductThumbnail(String productId) {
    return ProductCommand.getProductThumbnailData(productQueryOutPort.findByProductId(productId));
  }

  @Override
  public List<ProductInformation> getProductInformation(List<String> productIds) {
    List<Product> productByProductIds = productQueryOutPort.findProductByProductIds(productIds);
    return ProductCommand.getProductInformationListData(productByProductIds);
  }

  @Override
  public void getProductPriceValidation(List<IsProductPriceValid> productPriceValids) {
    if (!productQueryOutPort.findProductPriceValid(productPriceValids))
      throw new ProductPriceValidationException();
  }

  @Override
  public StoreSubscriptionProductId getStoreSubscriptionProductId(Long storeId) {
    return ProductCommand.getStoreSubscriptionProductIdData(
        productQueryOutPort.findSubscriptionProductByStoreId(storeId));
  }

  @Override
  public SubscriptionProductInformation getSubscriptionProductInformation(String productId) {
    return ProductCommand.getSubscriptionProductInformationData(
        productQueryOutPort.findByProductId(productId));
  }

  @Override
  public LanguageOfFlower getLanguageOfFlower(String productId) {
    RepresentativeFlowerId representativeFlower =
        productQueryOutPort.findRepresentativeFlower(productId);
    return LanguageOfFlower.getData(
        flowerQueryOutPort.findLanguageOfFlowerByFlowerId(representativeFlower.getFlowerId()));
  }

  @Override
  public List<ProductInformationForLikes> getProductInformationForLikes(List<String> productIds) {
    return ProductCommand.getProductInformationForLikesData(
        productQueryOutPort.findProductByProductIds(productIds));
  }

  @Override
  public GetUserCartItemsResponse getCartItemProductInformations(Map<String, Long> productIds) {
    Map<Long, List<Product>> productsByProductIdsForCartItem =
        productQueryOutPort.findProductsByProductIdsForCartItem(
            new ArrayList<>(productIds.keySet()));

    Map<Long, StorePolicy> storePolicies =
        storeServiceClient
            .getCartItemProductInformation(
                new ArrayList<>(productsByProductIdsForCartItem.keySet()))
            .getData();

    return ProductCommand.getUserCartItemResponse(
        productIds, productsByProductIdsForCartItem, storePolicies);
  }

  /**
   * 카테고리별 상품 리스트 조회 - 로그인
   *
   * @param userId
   * @param categoryId
   * @param pageable
   * @return
   */
  @Override
  public ProductList getProductsByCategory(
      Long userId,
      Long categoryId,
      Long storeId,
      ProductCommand.SortOption sortOption,
      Pageable pageable) {
    Pageable pageRequest = getPageable(pageable, sortOption);
    Page<Product> byCategory = getProductsByCategoryId(categoryId, storeId, pageRequest);
    List<String> ids = getProductIdsFromProducts(byCategory.getContent());

    List<ProductListItem> productByCategories = getProduct(byCategory);
    List<String> data = getProductsIsLiked(userId, ids);
    return ProductList.getData(productByCategories, data, byCategory.getTotalPages());
  }

  /**
   * 카테고리별 상품 리스트 조회 - 비로그인
   *
   * @param categoryId
   * @param storeId
   * @param sortOption
   * @param pageable
   * @return
   */
  @Override
  public ProductList getProductsByCategory(
      Long categoryId, Long storeId, SortOption sortOption, Pageable pageable) {
    Pageable pageRequest = getPageable(pageable, sortOption);
    Page<Product> byCategory = getProductsByCategoryId(categoryId, storeId, pageRequest);
    List<ProductListItem> productByCategories = getProduct(byCategory);
    return ProductList.getData(productByCategories, byCategory.getTotalPages());
  }

  /**
   * 태그별 상품 리스트 조회 - 로그인 group by category
   *
   * @param userId
   * @param categoryId
   * @param tagId
   * @param sortOption
   * @param pageable
   * @return
   */
  @Override
  public ProductCommand.ProductsGroupByCategory getProductsByTag(
      Long userId, Long tagId, Long categoryId, SortOption sortOption, Pageable pageable) {
    return getProductsGroupByCategory(userId, tagId, categoryId, sortOption, pageable);
  }

  // 태그 조회 카테고리별 묶음
  @NotNull
  private ProductsGroupByCategory getProductsGroupByCategory(
      Long userId, Long tagId, Long categoryId, SortOption sortOption, Pageable pageable) {
    ProductsGroupByCategory productsGroupByCategory = ProductsGroupByCategory.builder().build();
    for (int i = 0; i < 5; i++) {
      ProductList productWithLikes =
          getProductListByTagId(userId, categoryId, tagId, sortOption, pageable);
      productsGroupByCategory.setProducts(categoryId, productWithLikes);
      categoryId++;
    }
    return productsGroupByCategory;
  }

  // 태그별 조회 응답 생성 - 찜 포함
  private ProductList getProductListByTagId(
      Long userId, Long categoryId, Long tagId, SortOption sortOption, Pageable pageable) {
    Pageable pageRequest = getPageable(pageable, sortOption);
    Page<Product> productsByTag =
        productQueryOutPort.findProductsByTag(tagId, categoryId, pageRequest);
    List<ProductListItem> product = getProduct(productsByTag);

    if (userId == null) return ProductList.getData(product, productsByTag.getTotalPages());

    List<String> ids = ProductListItem.getProductIds(product);
    List<String> data = getProductsIsLiked(userId, ids);
    return ProductList.getData(product, data, productsByTag.getTotalPages());
  }

  /**
   * 태그별 상품 리스트 조회 - 비로그인
   *
   * @param tagId
   * @param categoryId
   * @param sortOption
   * @param pageable
   * @return
   */
  @Override
  public ProductCommand.ProductsGroupByCategory getProductsByTag(
      Long tagId, Long categoryId, SortOption sortOption, Pageable pageable) {
    return getProductsGroupByCategory(null, tagId, categoryId, sortOption, pageable);
  }
}
