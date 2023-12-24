package kr.bb.product.domain.product.infrastructure.client;

import bloomingblooms.domain.store.StoreName;
import bloomingblooms.response.CommonResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.common.dto.StorePolicy;
import kr.bb.product.config.OpenFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "storeServiceClient",
    url = "${endpoint.store-service}",
    configuration = OpenFeignClientConfiguration.class)
public interface StoreServiceClient {
  @CircuitBreaker(
      name = "getStoreNameOfProductDetailFallback",
      fallbackMethod = "getStoreNameOfProductDetailFallback")
  @GetMapping("client/stores/{storeId}/name")
  CommonResponse<StoreName> getStoreNameOfProductDetail(@PathVariable Long storeId);

  @CircuitBreaker(
      name = "getCartItemProductInformationFallback",
      fallbackMethod = "getCartItemProductInformationFallback")
  @PostMapping("/client/store/policy")
  CommonResponse<Map<Long, StorePolicy>> getCartItemProductInformation(
      @RequestBody List<Long> storeId);

  default CommonResponse<StoreName> getStoreNameOfProductDetailFallback(Long storeId, Throwable t) {
    return CommonResponse.success(StoreName.builder().storeName("가게명").build());
  }

  default CommonResponse<Map<Long, StorePolicy>> getCartItemProductInformationFallback(
      List<Long> storeId, Throwable t) {

    return CommonResponse.success(
        storeId.stream()
            .collect(
                Collectors.toMap(
                    // Key mapper: The storeId itself
                    id -> id,
                    // Value mapper: Creating a new StorePolicy for each storeId
                    id -> StorePolicy.builder().build(),
                    // Merge function in case of key collision (not needed here, can be null)
                    (existing, replacement) -> existing)));
  }
}
