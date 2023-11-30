package kr.bb.product.domain.product.adapter.in.api;

import bloomingblooms.response.CommonResponse;
import java.util.Optional;
import javax.validation.Valid;
import kr.bb.product.domain.product.application.usecase.ProductCommandUseCase;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.ProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
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
  private final ProductCommandUseCase productCommandUseCased;

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

  @PutMapping("{productId}/subscribe-product")
  public void updateSubscriptionProduct(
      @PathVariable String productId,
      @RequestBody ProductCommand.UpdateSubscriptionProduct product) {
    productCommandUseCased.updateSubscriptionProduct(productId, product);
  }

  @PostMapping("store/{storeId}/subscribe-product")
  public void createSubscriptionProduct(
      @PathVariable Long storeId, @RequestBody ProductCommand.SubscriptionProduct product) {
    productCommandUseCased.createSubscriptionProduct(storeId, product);
  }

  @GetMapping("{productId}")
  public CommonResponse<ProductCommand.ProductDetail> getProductDetail(
      @PathVariable String productId, @RequestHeader Optional<Long> userId) {
    if (userId.isPresent()) {
      return CommonResponse.<ProductDetail>builder()
          .data(productQueryUseCase.getProductDetail(userId.get(), productId))
          .message("상품 상세 정보 조회")
          .build();
    } else {
      return CommonResponse.<ProductDetail>builder()
          .data(productQueryUseCase.getProductDetail(productId))
          .message("상품 상세 정보 조회")
          .build();
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
    productCommandUseCased.createProduct(productRequestData);
  }

  @PutMapping("{productId}")
  public void updateProductSaleStatus(
      @PathVariable String productId,
      @RequestBody ProductCommand.ProductUpdate productRequestData) {
    productCommandUseCased.updateProductSaleStatus(productId, productRequestData);
  }
}
