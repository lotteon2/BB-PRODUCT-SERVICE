package kr.bb.product.domain.product.adapter.in.api;

import bloomingblooms.response.CommonResponse;
import java.util.Optional;
import javax.validation.Valid;
import kr.bb.product.domain.product.application.usecase.ProductCommandUseCase;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.BestSellerTopTen;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductList;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
public class ProductRestController {
  private final ProductQueryUseCase productQueryUseCase;
  private final ProductCommandUseCase productCommandUseCase;

  @GetMapping("store/{storeId}")
  public CommonResponse<StoreProductList> getStoreManagerProducts(
      @PathVariable Long storeId,
      @RequestParam("category") Long categoryId,
      @RequestParam("flower") Long flowerId,
      @RequestParam("status") ProductSaleStatus saleStatus,
      @PageableDefault(
              page = 0,
              size = 10,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {

    StoreProductList storeProducts =
        productQueryUseCase.getStoreProducts(storeId, categoryId, flowerId, saleStatus, pageable);
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

  @GetMapping("tag/{tagId}")
  public CommonResponse<ProductList> getProductListByTag(
      @PathVariable Long tagId, Pageable pageable, @RequestHeader Optional<Long> userId) {
    if (userId.isPresent()) {
      return CommonResponse.<ProductList>builder()
          .data(productQueryUseCase.getProductsByTag(userId.get(), tagId, pageable))
          .message("select success")
          .build();
    } else {
      return CommonResponse.<ProductList>builder()
          .data(productQueryUseCase.getProductsByTag(tagId, pageable))
          .message("select success")
          .build();
    }
  }

  @GetMapping("category/{categoryId}")
  public CommonResponse<ProductList> getProductListByCategory(
      @PathVariable Long categoryId, Pageable pageable, @RequestHeader Optional<Long> userId) {
    ProductList productsByCategory;
    if (userId.isPresent()) {
      productsByCategory =
          productQueryUseCase.getProductsByCategory(userId.get(), categoryId, pageable);
    } else {
      productsByCategory = productQueryUseCase.getProductsByCategory(categoryId, pageable);
    }
    return CommonResponse.<ProductList>builder()
        .data(productsByCategory)
        .message("select success")
        .build();
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

  @GetMapping("store/{storeId}/best-top-ten")
  public CommonResponse<BestSellerTopTen> getBestSellerTopTen(@PathVariable Long storeId) {
    return CommonResponse.success(
        productQueryUseCase.getBestSellerTopTen(storeId), "베스트 셀러 top 10 상품 조회");
  }
}
