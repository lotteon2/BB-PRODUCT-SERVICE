package kr.bb.product.controller.restcontroller;

import kr.bb.product.dto.request.ProductRequestData;
import kr.bb.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @PostMapping("store/{storeId}")
  private void createProduct(
      @PathVariable Long storeId, @RequestBody ProductRequestData productRequestData) {
    productRequestData.setStoreId(storeId);
    productService.createProduct(productRequestData);
  }
}
