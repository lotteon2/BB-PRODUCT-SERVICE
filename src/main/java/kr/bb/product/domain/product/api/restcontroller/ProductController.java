package kr.bb.product.domain.product.api.restcontroller;

import javax.validation.Valid;
import kr.bb.product.domain.product.api.request.ProductRequestData;
import kr.bb.product.domain.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @PostMapping("store/{storeId}")
  public void createProduct(
      @PathVariable Long storeId, final @Valid @RequestBody ProductRequestData productRequestData) {
    productRequestData.setStoreId(storeId);
    productService.createProduct(productRequestData);
  }

  @PutMapping("{productId}")
  public void updateProductSaleStatus(
      @PathVariable String productId, @RequestBody ProductRequestData productRequestData) {
    productService.updateProductSaleStatus(productId, productRequestData);
  }
}
