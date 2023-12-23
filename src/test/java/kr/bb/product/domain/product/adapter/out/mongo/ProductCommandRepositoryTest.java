package kr.bb.product.domain.product.adapter.out.mongo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.bb.product.common.dto.ReviewRegisterEvent;
import kr.bb.product.common.dto.ReviewType;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductCommandRepositoryTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
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
    updatedProduct.setProductId("123");
    productCommandRepository.updateSubscriptionProduct(updatedProduct);
    List<Product> all = productMongoRepository.findAll();
    Product product = all.get(0);
    System.out.println(product.toString());
    assertThat(product.getProductSummary()).isEqualTo(updatedProduct.getProductSummary());
  }

  @Test
  @DisplayName("리뷰 작성 시 리뷰 정보 수정 ")
  void updateProductReviewData() {
    productMongoRepository.deleteAll();
    Product product = Product.builder().productId("1234").averageRating(1.0).reviewCount(5L).build();
    productMongoRepository.save(product);
    productCommandRepository.updateProductReviewData(
        ReviewRegisterEvent.builder()
            .productId("1234")
            .reviewRating(2.0)
            .reviewType(ReviewType.PICKUP)
            .build());
    Product product1 = productMongoRepository.findByProductId("1234").get();
    System.out.println(product1.getAverageRating());
    assertThat(product1.getReviewCount()).isEqualTo(product.getReviewCount() + 1);
  }
}
