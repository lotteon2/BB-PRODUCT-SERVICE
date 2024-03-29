package kr.bb.product.domain.product.infrastructure.client;

import bloomingblooms.domain.flower.StockChangeDto;
import bloomingblooms.domain.store.StoreName;
import bloomingblooms.domain.store.StorePolicy;
import bloomingblooms.response.CommonResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.bb.product.config.OpenFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "storeServiceClient",
    url = "${endpoint.store-service}",
    configuration = OpenFeignClientConfiguration.class)
public interface StoreServiceClient {
  @CircuitBreaker(name = "getStoreNamesFallback", fallbackMethod = "getStoreNamesFallback")
  @GetMapping("client/stores/store-name")
  CommonResponse<Map<Long, String>> getStoreNames(@RequestParam("storeIds") List<Long> storeIds);

  default CommonResponse<Map<Long, String>> getStoreNamesFallback(
      @RequestParam("storeIds") List<Long> storeIds, Throwable t) {
    Map<Long, String> map =
        storeIds.stream()
            .collect(
                Collectors.toMap(
                    item -> item,
                    item -> "가게명", // Replace with your actual logic to get store names
                    (existingValue, newValue) ->
                        existingValue // Resolve conflicts by keeping the existing value
                    ));
    return CommonResponse.success(map);
  }

  @CircuitBreaker(
      name = "getStoreNameOfProductDetailFallback",
      fallbackMethod = "getStoreNameOfProductDetailFallback")
  @GetMapping("client/stores/{storeId}/name")
  CommonResponse<StoreName> getStoreNameOfProductDetail(@PathVariable("storeId") Long storeId);

  @CircuitBreaker(
      name = "getCartItemProductInformationFallback",
      fallbackMethod = "getCartItemProductInformationFallback")
  @PostMapping("/client/stores/policy")
  CommonResponse<Map<Long, StorePolicy>> getCartItemProductInformation(
      @RequestBody List<Long> storeId);

  /**
   * 꽃 재고 차감 요청
   *
   * @param stockChangeDto
   */
  @PutMapping("/client/stores/flowers/stocks/subtract")
  CommonResponse flowerStockDecreaseRequest(@RequestBody List<StockChangeDto> stockChangeDto);

  @PutMapping("/client/stores/flowers/stocks/add")
  CommonResponse flowerStockIncreaseRequest(
      @RequestBody List<StockChangeDto> flowerAmountGroupByStoreId);

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
