package kr.bb.product.domain.product.infrastructure.client;

import bloomingblooms.domain.store.StoreName;
import bloomingblooms.response.CommonResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kr.bb.product.config.OpenFeignClientConfiguration;

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
  @GetMapping("client/stores/{storeId}/name")
  CommonResponse<StoreName> getStoreNameOfProductDetail(@PathVariable Long storeId);

  default CommonResponse<StoreName> getStoreNameOfProductDetailFallback(Long storeId, Throwable t) {
    return CommonResponse.success(StoreName.builder().storeName("가게명").build());
  }
}
