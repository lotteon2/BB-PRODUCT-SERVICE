package kr.bb.product.domain.product.adapter.in.client;

import bloomingblooms.response.CommonResponse;
import kr.bb.product.common.dto.StoreSubscriptionProductId;
import kr.bb.product.common.dto.SubscriptionProductInformation;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductFeignClientController {
  private final ProductQueryUseCase productQueryUseCase;

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
