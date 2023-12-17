package kr.bb.product.domain.product.infrastructure.client;

import bloomingblooms.response.CommonResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kr.bb.product.config.OpenFeignClientConfiguration;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.StoreName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "storeServiceClient",
    url = "${endpoint.wishlist-service}",
    configuration = OpenFeignClientConfiguration.class)
public interface StoreServiceClient {
  @CircuitBreaker(
      name = "getStoreNameOfProductDetailFallback",
      fallbackMethod = "getStoreNameOfProductDetailFallback")
  @GetMapping("/stores/{storeId}/name")
  CommonResponse<ProductCommand.StoreName> getStoreNameOfProductDetail(@PathVariable Long storeId);

  default CommonResponse<ProductCommand.StoreName> getStoreNameOfProductDetailFallback(
      Long storeId, Throwable t) {
    return CommonResponse.success(StoreName.builder().storeName("가게명").build());
  }
}
