package kr.bb.product.domain.product.adapter.in.client;

import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.product.common.dto.IsProductPriceValid;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductFeignClientController {
  private final ProductQueryUseCase productQueryUseCase;

  @GetMapping("/products/validate-price")
  public CommonResponse getProductPriceValidation(
      @RequestBody List<IsProductPriceValid> productPriceValids) {
    productQueryUseCase.getProductPriceValidation(productPriceValids);
    return CommonResponse.success("success");
  }
}
