package kr.bb.product.domain.product.infrastructure.client;

import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.product.config.OpenFeignClientConfiguration;
import kr.bb.product.domain.product.entity.ProductCommand.ProductDetailLike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "wishlistServiceClient",
    url = "${endpoint.wishlist-service}",
    configuration = OpenFeignClientConfiguration.class)
public interface WishlistServiceClient {
  @PostMapping("/likes/{userId}")
  CommonResponse<List<String>> getProductsMemberLikes(
      @PathVariable Long userId, List<String> productIds);

  @GetMapping("/likes/{userId}/product/{productId}")
  CommonResponse<ProductDetailLike> getProductDetailLikes(
      @PathVariable String productId, @PathVariable Long userId);
}
