package kr.bb.product.domain.product.adapter.in.client;

import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.product.common.dto.IsProductPriceValid;
import kr.bb.product.common.dto.ProductInformation;
import kr.bb.product.common.dto.StoreSubscriptionProductId;
import kr.bb.product.common.dto.SubscriptionProductInformation;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductFeignClientController {
  private final ProductQueryUseCase productQueryUseCase;

  @GetMapping("/products/product-info")
  public CommonResponse<List<ProductInformation>> getProductInformation(
      @RequestBody List<String> productIds) {
    return CommonResponse.success(productQueryUseCase.getProductInformation(productIds));}
  @GetMapping("/products/validate-price")
  public CommonResponse getProductPriceValidation(
      @RequestBody List<IsProductPriceValid> productPriceValids) {
    productQueryUseCase.getProductPriceValidation(productPriceValids);
    return CommonResponse.success("success");
  }

  @GetMapping("")
  public CommonResponse<StoreSubscriptionProductId> getStoreSubscriptionProductId(
      @RequestParam("store-id") Long storeId) {
    return CommonResponse.success(productQueryUseCase.getStoreSubscriptionProductId(storeId));
  }

  @GetMapping("product/{productId}")
  public CommonResponse<SubscriptionProductInformation> getSubscriptionProductInformation(
      @PathVariable String productId) {
    return CommonResponse.success(productQueryUseCase.getSubscriptionProductInformation(productId));
  }
}
