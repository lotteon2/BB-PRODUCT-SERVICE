package kr.bb.product.domain.product.adapter.in.api;

import bloomingblooms.response.CommonResponse;
import javax.validation.Valid;
import kr.bb.product.domain.product.application.port.in.ProductFindInputPort;
import kr.bb.product.domain.product.application.port.in.ProductStoreInputPort;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.ProductsByCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductRestController {
  private final ProductFindInputPort productFindInputPort;
  private final ProductStoreInputPort productStoreInputPort;

  @GetMapping("category/{categoryId}")
  public CommonResponse<ProductCommand.ProductsByCategory> getProductListByCategoryLogin(
      @PathVariable Long categoryId, Pageable pageable, @RequestHeader Long userId) {
    ProductsByCategory productsByCategory;
    if (userId != null) {
      productsByCategory = productFindInputPort.getProductsByCategory(userId, categoryId, pageable);
    } else {
      productsByCategory = productFindInputPort.getProductsByCategory(categoryId, pageable);
    }
    return CommonResponse.<ProductsByCategory>builder()
        .data(productsByCategory)
        .message("select success")
        .build();
  }

  @PostMapping("store/{storeId}")
  public void createProduct(
      @PathVariable Long storeId,
      final @Valid @RequestBody ProductCommand.ProductRegister productRequestData) {
    productRequestData.setStoreId(storeId);
    productStoreInputPort.createProduct(productRequestData);
  }

  @PutMapping("{productId}")
  public void updateProductSaleStatus(
      @PathVariable String productId,
      @RequestBody ProductCommand.ProductUpdate productRequestData) {
    productStoreInputPort.updateProductSaleStatus(productId, productRequestData);
  }
}
