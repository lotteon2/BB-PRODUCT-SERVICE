package kr.bb.product.infrastructure.client;

import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.product.config.OpenFeignClientConfiguration;
import kr.bb.product.domain.product.entity.ProductCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "wishlistServiceClient",
    //    url = "${endpoint.wishlist-service}",
    url = "https://4c237c9c-89f1-487a-9401-9b02f965bcd5.mock.pstmn.io",
    configuration = OpenFeignClientConfiguration.class)
public interface WishlistServiceClient {
  @PostMapping("/likes/{userId}")
  CommonResponse<List<ProductCommand.ProductByCategory>> getProductsMemberLikes(
      @PathVariable Long userId, List<ProductCommand.ProductByCategory> products);
}
