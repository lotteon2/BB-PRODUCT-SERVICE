package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.bb.product.domain.product.adapter.out.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.SubscriptionProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductCommandInputPortTest {
  @Autowired private ProductCommandInputPort productCommandInputPort;
  @Autowired private ProductMongoRepository productMongoRepository;

  @Test
  void createSubscriptionProduct() {
    productMongoRepository.deleteAll();
    SubscriptionProduct build =
        SubscriptionProduct.builder()
            .productName("product name")
            .productPrice(423L)
            .productThumbnail("thumb")
            .productSummary("summary ")
            .productDescriptionImage("image ")
            .build();
    productCommandInputPort.createSubscriptionProduct(1L, build);
    List<Product> all = productMongoRepository.findAll();
    assertThat(all.get(0).getProductName()).isEqualTo(build.getProductName());
    assertThat(all.get(0).getCategory()).isNull();
    assertThat(all.get(0).getIsSubscription()).isEqualTo(true);
  }

  @Test
  void createSubscriptionProductFail() {
    productMongoRepository.deleteAll();
    SubscriptionProduct build =
        SubscriptionProduct.builder()
            .productPrice(423L)
            .productThumbnail("thumb")
            .productSummary("summary ")
            .productDescriptionImage("image ")
            .build();
    productCommandInputPort.createSubscriptionProduct(1L, build);
    List<Product> all = productMongoRepository.findAll();
    assertThat(all.get(0).getCategory()).isNull();
  }

  @Test
  @DisplayName("구독 상품 수정 service test")
  void updateSubscriptionProduct() {
    productMongoRepository.deleteAll();
    Product build =
        Product.builder()
            .productId("123")
            .productThumbnail("thumbnail")
            .productName("product name")
            .productSummary("summary")
            .productDescriptionImage("description image")
            .productPrice(100000L)
            .storeId(1L)
            .isSubscription(true)
            .build();
    productMongoRepository.save(build);
    ProductCommand.UpdateSubscriptionProduct updatedProduct =
        ProductCommand.UpdateSubscriptionProduct.builder()
            .productThumbnail("thumbnail")
            .productName("product name")
            .productSummary("summary update")
            .productDescriptionImage("description image")
            .productPrice(100000L)
            .build();
    productCommandInputPort.updateSubscriptionProduct("123", updatedProduct);
    List<Product> all = productMongoRepository.findAll();
    Product product = all.get(0);
    assertThat(product.getProductSummary()).isEqualTo(updatedProduct.getProductSummary());
  }
}
