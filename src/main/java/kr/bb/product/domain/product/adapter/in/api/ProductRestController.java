package kr.bb.product.domain.product.adapter.in.api;

import bloomingblooms.response.CommonResponse;
import java.util.Optional;
import javax.validation.Valid;
import kr.bb.product.domain.product.application.usecase.ProductCommandUseCase;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.BestSellerTopTen;
import kr.bb.product.domain.product.entity.ProductCommand.SelectOption;
import kr.bb.product.domain.product.entity.ProductCommand.SortOption;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductList;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductRestController {
  private final ProductQueryUseCase productQueryUseCase;
  private final ProductCommandUseCase productCommandUseCase;

  @GetMapping("store/{storeId}")
  public CommonResponse<StoreProductList> getStoreManagerProducts(
      @PathVariable Long storeId,
      @RequestParam("category") Optional<Long> categoryId,
      @RequestParam("flower") Optional<Long> flowerId,
      @RequestParam("status") Optional<ProductSaleStatus> saleStatus,
      @PageableDefault(
              page = 0,
              size = 10,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    Long categoryParam = categoryId.orElse(null);
    Long flowerParam = flowerId.orElse(null);
    ProductSaleStatus productSaleStatus = saleStatus.orElse(null);

    StoreProductList storeProducts =
        productQueryUseCase.getStoreProducts(
            storeId, categoryParam, flowerParam, productSaleStatus, pageable);
    return CommonResponse.<StoreProductList>builder()
        .data(storeProducts)
        .message("가게 상품 리스트 조회")
        .build();
  }

  @GetMapping("{productId}/store/{storeId}")
  public CommonResponse<StoreProductDetail> getStoreProductDetail(
      @PathVariable String productId, @PathVariable Long storeId) {
    return CommonResponse.success(
        productQueryUseCase.getStoreProductDetail(storeId, productId), "가게 사장 상품 상세 조회");
  }

  @PutMapping("{productId}/subscribe-product")
  public void updateSubscriptionProduct(
      @PathVariable String productId,
      @RequestBody ProductCommand.UpdateSubscriptionProduct product) {
    productCommandUseCase.updateSubscriptionProduct(productId, product);
  }

  @PostMapping("store/{storeId}/subscribe-product")
  public void createSubscriptionProduct(
      @PathVariable Long storeId, @RequestBody ProductCommand.SubscriptionProduct product) {
    productCommandUseCase.createSubscriptionProduct(storeId, product);
  }

  @GetMapping("{productId}")
  public CommonResponse<ProductCommand.ProductDetail> getProductDetail(
      @PathVariable String productId, @RequestHeader Optional<Long> userId) {
    if (userId.isPresent()) {
      return CommonResponse.success(
          productQueryUseCase.getProductDetail(userId.get(), productId), "상품 상세 정보 조회");
    } else {
      return CommonResponse.success(productQueryUseCase.getProductDetail(productId), "상품 상세 정보 조회");
    }
  }

  @PostMapping("store/{storeId}")
  public void createProduct(
      @PathVariable Long storeId,
      final @Valid @RequestBody ProductCommand.ProductRegister productRequestData) {
    productRequestData.setStoreId(storeId);
    productCommandUseCase.createProduct(productRequestData);
  }

  @PutMapping("{productId}")
  public void updateProductSaleStatus(
      @PathVariable String productId,
      @RequestBody ProductCommand.ProductUpdate productRequestData) {
    productCommandUseCase.updateProductSaleStatus(productId, productRequestData);
  }

  /**
   * 카테고리별 상품 리스트 조회
   *
   * @param userId
   * @param categoryId
   * @param sortOption
   * @param storeId
   * @param pageable
   * @return
   */
  @GetMapping("category/{categoryId}")
  public CommonResponse<ProductCommand.ProductList> getProductByCategory(
      @RequestHeader Optional<Long> userId,
      @PathVariable Optional<Long> categoryId,
      @RequestParam("sort-option") Optional<ProductCommand.SortOption> sortOption,
      @RequestParam("store-id") Optional<Long> storeId,
      @PageableDefault(
              page = 0,
              size = 10,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    Long categoryIdParam = categoryId.orElse(null);
    ProductCommand.SortOption sortOptionParam = sortOption.orElse(null);
    Long storeIdParam = storeId.orElse(null);
    if (userId.isPresent()) {
      return CommonResponse.<ProductCommand.ProductList>success(
          productQueryUseCase.getProductsByCategory(
              userId.get(), categoryIdParam, storeIdParam, sortOptionParam, pageable),
          "카테고리별 상품 리스트 조회");
    } else {
      return CommonResponse.<ProductCommand.ProductList>success(
          productQueryUseCase.getProductsByCategory(
              categoryIdParam, storeIdParam, sortOptionParam, pageable),
          "카테고리별 상품 리스트 조회");
    }
  }

  @GetMapping("tag/{tagId}")
  public CommonResponse<ProductCommand.ProductsGroupByCategory> getProductsByTag(
      @PathVariable Long tagId,
      @RequestHeader Optional<Long> userId,
      @RequestParam("sort-option") Optional<ProductCommand.SortOption> sortOption,
      @PageableDefault(
              page = 0,
              size = 10,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    SortOption sortOptionParam = sortOption.orElse(null);
    if (userId.isPresent()) {
      return CommonResponse.success(
          productQueryUseCase.getProductsByTag(userId.get(), tagId, 0L, sortOptionParam, pageable),
          "태그별 상품 리스트 조회");
    } else {
      return CommonResponse.success(
          productQueryUseCase.getProductsByTag(tagId, 0L, sortOptionParam, pageable),
          "태그별 상품 리스트 조회");
    }
  }

  @GetMapping("store/{storeId}/best-top-ten")
  public CommonResponse<BestSellerTopTen> getBestSellerTopTen(@PathVariable Long storeId) {
    return CommonResponse.success(
        productQueryUseCase.getBestSellerTopTen(storeId), "베스트 셀러 top 10 상품 조회");
  }

  @GetMapping("store/{storeId}/subscribe-product")
  public CommonResponse<ProductCommand.StoreManagerSubscriptionProduct>
      getStoreManagerSubscriptionProduct(@PathVariable Long storeId) {
    return CommonResponse.success(productQueryUseCase.getSubscriptionProductByStoreId(storeId));
  }

  @GetMapping("main/recommend")
  public CommonResponse<ProductCommand.MainPageProductItems> getMainPageProductsRecommend(
      @RequestHeader Optional<Long> userId) {
    if (userId.isPresent())
      return CommonResponse.success(
          productQueryUseCase.getMainPageProducts(userId.get(), SelectOption.RECOMMEND));
    else
      return CommonResponse.success(
          productQueryUseCase.getMainPageProducts(SelectOption.RECOMMEND));
  }

  @GetMapping("main/new-arrival")
  public CommonResponse<ProductCommand.MainPageProductItems> getMainPageProductsNewArrival(
      @RequestHeader Optional<Long> userId) {
    if (userId.isPresent())
      return CommonResponse.success(
          productQueryUseCase.getMainPageProducts(userId.get(), SelectOption.NEW_ARRIVAL));
    else
      return CommonResponse.success(
          productQueryUseCase.getMainPageProducts(SelectOption.NEW_ARRIVAL));
  }

  @GetMapping("main/rating")
  public CommonResponse<ProductCommand.MainPageProductItems> getMainPageProductsRating(
      @RequestHeader Optional<Long> userId) {
    if (userId.isPresent())
      return CommonResponse.success(
          productQueryUseCase.getMainPageProducts(userId.get(), SelectOption.RATING));
    else
      return CommonResponse.success(productQueryUseCase.getMainPageProducts(SelectOption.RATING));
  }

  @GetMapping("subscription/{storeId}")
  public CommonResponse<ProductCommand.SubscriptionProductForCustomer> getSubscriptionProductDetail(
      @PathVariable Long storeId, @RequestHeader Optional<Long> userId) {
    if (userId.isPresent())
      return CommonResponse.success(
          productQueryUseCase.getSubscriptionProductDetail(userId.get(), storeId));
    else return CommonResponse.success(productQueryUseCase.getSubscriptionProductDetail(storeId));
  }
}
