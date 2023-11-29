package kr.bb.product.domain.product.adapter.out.mongo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductCommandRepositoryTest {
  @Autowired private ProductCommandRepository productCommandRepository;
  @Autowired private ProductMongoRepository productMongoRepository;

  @Test
  @DisplayName("구독 상품 수정 repo test")
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
    updatedProduct.setStoreId(1L);
    productCommandRepository.updateSubscriptionProduct(updatedProduct);
    List<Product> all = productMongoRepository.findAll();
    Product product = all.get(0);
    System.out.println(product.toString());
    assertThat(product.getProductSummary()).isEqualTo(updatedProduct.getProductSummary());
  }
}
