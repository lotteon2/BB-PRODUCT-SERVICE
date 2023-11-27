package kr.bb.product.domain.product.adapter.in.api;

import bloomingblooms.response.CommonResponse;
import java.util.Optional;
import javax.validation.Valid;
import kr.bb.product.domain.product.application.port.in.ProductCommandInputPort;
import kr.bb.product.domain.product.application.port.in.ProductQueryInputPort;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.ProductDetail;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
  private final ProductQueryInputPort productQueryInputPort;
  private final ProductCommandInputPort productStoreInputPort;
  private final ProductCommandInputPort productCommandInputPort;

  @PostMapping("store/{storeId}/subscribe-product")
  public void createSubscriptionProduct(
      @PathVariable Long storeId, @RequestBody ProductCommand.SubscriptionProduct product) {
    productCommandInputPort.createSubscriptionProduct(storeId, product);
  }

  @GetMapping("{productId}")
  public CommonResponse<ProductCommand.ProductDetail> getProductDetail(
      @PathVariable String productId, @RequestHeader Optional<Long> userId) {
    if (userId.isPresent()) {
      return CommonResponse.<ProductDetail>builder()
          .data(productQueryInputPort.getProductDetail(userId.get(), productId))
          .message("상품 상세 정보 조회")
          .build();
    } else {
      return CommonResponse.<ProductDetail>builder()
          .data(productQueryInputPort.getProductDetail(productId))
          .message("상품 상세 정보 조회")
          .build();
    }
  }

  @GetMapping("tag/{tagId}")
  public CommonResponse<ProductList> getProductListByTag(
      @PathVariable Long tagId, Pageable pageable, @RequestHeader Optional<Long> userId) {
    if (userId.isPresent()) {
      return CommonResponse.<ProductList>builder()
          .data(productQueryInputPort.getProductsByTag(userId.get(), tagId, pageable))
          .message("select success")
          .build();
    } else {
      return CommonResponse.<ProductList>builder()
          .data(productQueryInputPort.getProductsByTag(tagId, pageable))
          .message("select success")
          .build();
    }
  }

  @GetMapping("category/{categoryId}")
  public CommonResponse<ProductList> getProductListByCategory(
      @PathVariable Long categoryId, Pageable pageable, @RequestHeader Optional<Long> userId) {
    ProductList productsByCategory;
    if (userId.isPresent()) {
      productsByCategory =
          productQueryInputPort.getProductsByCategory(userId.get(), categoryId, pageable);
    } else {
      productsByCategory = productQueryInputPort.getProductsByCategory(categoryId, pageable);
    }
    return CommonResponse.<ProductList>builder()
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
