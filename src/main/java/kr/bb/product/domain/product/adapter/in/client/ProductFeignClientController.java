package kr.bb.product.domain.product.adapter.in.client;

import bloomingblooms.response.CommonResponse;
import kr.bb.product.common.dto.ProductThumbnail;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductFeignClientController {
  private final ProductQueryUseCase productQueryUseCase;

  @GetMapping("")
  public CommonResponse<ProductThumbnail> getProductThumbnail(
      @RequestParam("product-id") String productId) {
    return CommonResponse.success(productQueryUseCase.getProductThumbnail(productId));
  }
}
