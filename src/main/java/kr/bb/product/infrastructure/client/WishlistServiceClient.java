package kr.bb.product.infrastructure.client;

import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.product.config.OpenFeignClientConfiguration;
import kr.bb.product.domain.product.entity.ProductCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "wishlistServiceClient",
    url = "${endpoint.wishlist-service}",
    configuration = OpenFeignClientConfiguration.class)
public interface WishlistServiceClient {
  @GetMapping("/likes/{userId}")
  CommonResponse<List<ProductCommand.ProductByCategory>> getProductsMemberLikes(
      @PathVariable Long userId, List<ProductCommand.ProductByCategory> products);
}
