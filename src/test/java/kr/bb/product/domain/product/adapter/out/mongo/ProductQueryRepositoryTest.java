package kr.bb.product.domain.product.adapter.out.mongo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import javax.persistence.EntityManager;
import kr.bb.product.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductQueryRepositoryTest {
  @Autowired ProductCommandRepository productQueryRepository;
  @Autowired ProductRepository productRepository;
  @Autowired EntityManager em;
  @Autowired private ProductMongoRepository productMongoRepository;

  @Test
  void createProduct() {
    productMongoRepository.deleteAll();
    Product build =
        Product.builder()
            .productName("name")
            .productSummary("summary")
            .productPrice(12L)
            .productDescriptionImage("images")
            .productThumbnail("thumbnail")
            .build();
    productQueryRepository.createProduct(build);
    List<Product> all = productMongoRepository.findAll();
    assertThat(all.get(0).getProductName()).isEqualTo(build.getProductName());
    assertThat(all.get(0).getCategory()).isNull();
  }

  @Test
  void findProductByStoreId() {
    productMongoRepository.deleteAll();
    for (int i = 0; i < 5; i++) {
      Product build =
          Product.builder()
              .productName("name")
              .productSummary("summary")
              .productPrice(12L)
              .storeId(1L)
              .productDescriptionImage("images")
              .productThumbnail("thumbnail")
              .build();
      productQueryRepository.createProduct(build);
    }
    PageRequest pageRequest = PageRequest.of(0, 5);
    List<Product> productByStoreId = productMongoRepository.findProductByStoreId(1L);
    assertThat(productByStoreId.size()).isEqualTo(5);
  }

  @Test
  @DisplayName("가게 사장 상품 리스트 조회")
  void testFindProductByStoreId() {
    productMongoRepository.deleteAll();
    for (int i = 0; i < 10; i++) {
      Product build =
          Product.builder()
              .productThumbnail("thumbnail")
              .productName("product name")
              .productSummary("summary")
              .productDescriptionImage("description image")
              .productPrice(100000L)
              .storeId(i % 2 == 0 ? i : 1L)
              .isSubscription(true)
              .build();
      productMongoRepository.save(build);
    }

    List<Product> productsByStoreId = productMongoRepository.findProductsByStoreId(1L);
    assertThat(productsByStoreId.size()).isEqualTo(5);
  }
}
