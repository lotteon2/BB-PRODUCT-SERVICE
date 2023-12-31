package kr.bb.product.domain.product.infrastructure.http.client;

import bloomingblooms.domain.product.IsProductPriceValid;
import bloomingblooms.domain.product.ProductInfoDto;
import bloomingblooms.domain.product.ProductInformation;
import bloomingblooms.domain.product.ProductThumbnail;
import bloomingblooms.domain.product.StoreSubscriptionProductId;
import bloomingblooms.domain.wishlist.cart.GetUserCartItemsResponse;
import bloomingblooms.domain.wishlist.likes.LikedProductInfoResponse;
import bloomingblooms.response.CommonResponse;
import java.util.List;
import java.util.Map;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ProductFeignClientController {
  private final ProductQueryUseCase productQueryUseCase;

  @GetMapping("/product")
  public CommonResponse<ProductThumbnail> getProductThumbnail(
      @RequestParam("product-id") String productId) {
    return CommonResponse.success(productQueryUseCase.getProductThumbnail(productId));
  }

  @PostMapping("/products/product-info")
  public CommonResponse<List<ProductInformation>> getProductInformation(
      @RequestBody List<String> productIds) {
    return CommonResponse.success(productQueryUseCase.getProductInformation(productIds));
  }

  @PostMapping("/products/validate-price")
  public CommonResponse getProductPriceValidation(
      @RequestBody List<IsProductPriceValid> productPriceValids) {
    productQueryUseCase.getProductPriceValidation(productPriceValids);
    return CommonResponse.success("success");
  }

  @GetMapping("store")
  public CommonResponse<StoreSubscriptionProductId> getStoreSubscriptionProductId(
      @RequestParam("store-id") Long storeId) {
    return CommonResponse.success(productQueryUseCase.getStoreSubscriptionProductId(storeId));
  }

  @GetMapping("product/{productId}")
  public CommonResponse<ProductInfoDto> getSubscriptionProductInformation(
      @PathVariable String productId) {
    return CommonResponse.success(productQueryUseCase.getSubscriptionProductInformation(productId));
  }

  @PostMapping("products/likes")
  public CommonResponse<List<LikedProductInfoResponse>> getProductInformationForLikes(
      @RequestBody List<String> productId) {
    return CommonResponse.success(productQueryUseCase.getProductInformationForLikes(productId));
  }

  @PostMapping("products/carts")
  public CommonResponse<GetUserCartItemsResponse> getUserCartItemsResponseCommonResponse(
      @RequestBody Map<String, Long> productIdWithSelectedQuantity) {
    return CommonResponse.success(
        productQueryUseCase.getCartItemProductInformations(productIdWithSelectedQuantity));
  }
}
