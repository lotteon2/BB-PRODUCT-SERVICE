package kr.bb.product.domain.product.adapter.out.mongo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.vo.ProductFlowers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductQueryRepositoryTest {
  @Autowired ProductCommandRepository productCommandRepository;
  @Autowired ProductRepository productRepository;
  @Autowired EntityManager em;
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired private ProductMongoRepository productMongoRepository;
  @Autowired private ProductQueryRepository productQueryRepository;
  @Autowired private ProductQueryOutPort productQueryOutPort;

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

  @DisplayName("가게 사장 상품 리스트 조회")
  void findStoreProducts() {
    productMongoRepository.deleteAll();
    createProducts();
    PageRequest pageRequest = PageRequest.of(0, 5);
    Page<Product> storeProducts =
        productQueryOutPort.findStoreProducts(1L, null, 1L, null, pageRequest);
    assertThat(storeProducts.getContent().size()).isEqualTo(5);
  }

  private void createProducts() {
    ProductFlowers build1 = ProductFlowers.builder().flowerId(1L).build();
    List<ProductFlowers> list = new ArrayList<>();
    list.add(build1);
    for (int i = 0; i < 10; i++) {
      Product build =
          Product.builder()
              .productThumbnail("thumbnail")
              .productName("product name")
              .productSummary("summary")
              .category(Category.builder().categoryName("ca").categoryId(1L + i).build())
              .productDescriptionImage("description image")
              .productFlowers(list)
              .productPrice(100000L + i)
              .productSaleAmount(10L + i)
              .storeId(1L)
              .isSubscription(false)
              .build();
      productMongoRepository.save(build);
    }
  }

  @Test
  @DisplayName("베스트 셀러 10개 ")
  void findBestSellerTopTen() {
    productMongoRepository.deleteAll();
    createProducts();
    List<Product> bestSellerTopTen = productQueryRepository.findBestSellerTopTen(1L);
    assertThat(bestSellerTopTen.size()).isEqualTo(10);
    assertThat(
            bestSellerTopTen.get(0).getProductPrice() > bestSellerTopTen.get(1).getProductPrice())
        .isTrue();
  }
}
