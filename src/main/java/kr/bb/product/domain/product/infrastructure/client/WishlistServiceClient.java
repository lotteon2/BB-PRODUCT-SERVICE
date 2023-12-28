package kr.bb.product.domain.product.infrastructure.client;

import bloomingblooms.response.CommonResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import kr.bb.product.config.OpenFeignClientConfiguration;
import kr.bb.product.domain.product.mapper.ProductCommand.ProductDetailLike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "wishlistServiceClient",
    url = "${endpoint.wishlist-service}",
    configuration = OpenFeignClientConfiguration.class)
public interface WishlistServiceClient {

  @CircuitBreaker(name = "getProductsMemberLikesFallback", fallbackMethod = "getProductsMemberLikesFallback")
  @PostMapping("/client/likes/{userId}")
  CommonResponse<List<String>> getProductsMemberLikes(
      @PathVariable("userId") Long userId, List<String> productIds);

  @CircuitBreaker(name = "getProductDetailLikesFallback", fallbackMethod = "getProductDetailLikesFallback")
  @GetMapping("/client/likes/{userId}/product/{productId}")
  CommonResponse<ProductDetailLike> getProductDetailLikes(
      @PathVariable("productId") String productId, @PathVariable Long userId);

  default CommonResponse<List<String>> getProductsMemberLikesFallback(
      Long userId, List<String> productIds, Throwable t) {
    return CommonResponse.success(List.of());
  }

  default CommonResponse<ProductDetailLike> getProductDetailLikesFallback(
      String productId, Long userId, Throwable t) {
    return CommonResponse.success(ProductDetailLike.builder().isLiked(false).build());
  }
}
