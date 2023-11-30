package kr.bb.product.domain.product.adapter.out.mongo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
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
  @Autowired ProductCommandRepository productCommandRepository;
  @Autowired ProductRepository productRepository;
  @Autowired private ProductMongoRepository productMongoRepository;
  @Autowired private ProductQueryRepository productQueryRepository;

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
    productCommandRepository.createProduct(build);
    List<Product> all = productMongoRepository.findAll();
    assertThat(all.get(0).getProductName()).isEqualTo(build.getProductName());
    assertThat(all.get(0).getCategory()).isNull();
  }

  @Test
  @DisplayName("가게 사장 상품 리스트 조회")
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
      productCommandRepository.createProduct(build);
    }
    PageRequest pageRequest = PageRequest.of(0, 5);
    List<Product> productByStoreId = productMongoRepository.findProductByStoreId(1L);
    assertThat(productByStoreId.size()).isEqualTo(5);
  }

  @Test
  @DisplayName("가게 사장 상품 상세 조회")
  void findStoreProductByStoreIdAndProductId() {
    productMongoRepository.deleteAll();
    Product build =
        Product.builder()
            .productId("123")
            .productName("name")
            .productSummary("summary")
            .productPrice(12L)
            .storeId(1L)
            .productDescriptionImage("images")
            .productThumbnail("thumbnail")
            .build();
    productCommandRepository.createProduct(build);
    Product storeProductByStoreIdAndProductId =
        productQueryRepository.findStoreProductByStoreIdAndProductId(1L, "123");
    assertThat(storeProductByStoreIdAndProductId.getProductSummary())
        .isEqualTo(build.getProductSummary());
  }
}
