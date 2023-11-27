package kr.bb.product.domain.product.infrastructure.client;

import bloomingblooms.response.CommonResponse;
import kr.bb.product.config.OpenFeignClientConfiguration;
import kr.bb.product.domain.product.entity.ProductCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "storeServiceClient",
    //    url = "${endpoint.wishlist-service}",
    url = "https://4c237c9c-89f1-487a-9401-9b02f965bcd5.mock.pstmn.io",
    configuration = OpenFeignClientConfiguration.class)
public interface StoreServiceClient {
  @GetMapping("/store/{storeId}/name")
  CommonResponse<ProductCommand.StoreName> getStoreNameOfProductDetail(@PathVariable Long storeId);
}
