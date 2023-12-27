package kr.bb.product.domain.product.adapter.out.mongo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import bloomingblooms.domain.product.IsProductPriceValid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.flower.mapper.FlowerCommand.ProductFlowers;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.mapper.ProductCommand.RepresentativeFlowerId;
import kr.bb.product.domain.product.mapper.ProductCommand.SelectOption;
import kr.bb.product.domain.tag.entity.Tag;
import kr.bb.product.exception.errors.ProductNotFoundException;
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
    extracted();
    PageRequest pageRequest = PageRequest.of(0, 5);
    Page<Product> storeProducts =
        productQueryOutPort.findStoreProducts(1L, null, 1L, null, pageRequest);
    assertThat(storeProducts.getContent().size()).isEqualTo(5);
  }

  private void extracted() {
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
    List<Tag> tagList = new ArrayList<>();
    tagList.add(Tag.builder().tagId(1L).build());
    tagList.add(Tag.builder().tagId(2L).build());
    for (int i = 0; i < 10; i++) {
      Product build =
          Product.builder()
              .productThumbnail("thumbnail")
              .productName("product name")
              .productSummary("summary")
              .category(Category.builder().categoryName("ca").categoryId(1L + i).build())
              .productDescriptionImage("description image")
              .productFlowers(list)
              .tag(tagList)
              .averageRating(1.0 + i)
              .productSaleStatus(ProductSaleStatus.SALE)
              .productPrice(100000L + i)
              .productSaleAmount(10L + i)
              .storeId(1L)
              .createdAt(LocalDateTime.now())
              .isSubscription(false)
              .build();
      productMongoRepository.save(build);
    }
  }

  @Test
  @DisplayName("태그별 상품 리스트 조회")
  void findProductsByTag() {
    extracted();
    PageRequest pageRequest = PageRequest.of(0, 5);
    Page<Product> storeProducts = productQueryOutPort.findProductsByTag(1L, 1L, pageRequest);
    assertThat(storeProducts.getContent().size()).isEqualTo(1);
  }

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

  @Test
  @DisplayName("가게 사장 구독 상품 조회")
  void findSubscriptionProductByStoreId() {
    productMongoRepository.deleteAll();
    Product build = Product.builder().productName("name").isSubscription(true).storeId(1L).build();
    productMongoRepository.save(build);
    Product subscriptionProductByStoreId =
        productMongoRepository.findSubscriptionProductByStoreId(1L);
    assertThat(subscriptionProductByStoreId.getProductName()).isEqualTo(build.getProductName());
  }

  @Test
  @DisplayName("메인 페이지 상품 조회 ")
  void findMainPageProductsByRating() {
    productMongoRepository.deleteAll();
    createProducts();
    List<Product> mainPageProducts =
        productQueryRepository.findMainPageProducts(SelectOption.RATING);
    assertThat(
            mainPageProducts.get(0).getAverageRating() > mainPageProducts.get(1).getAverageRating())
        .isTrue();
  }

  @Test
  @DisplayName("상품 정보 조회 요청 repo test")
  void findProductByProductIds() {
    List<String> productIds = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      productIds.add("i" + i);
      Product product =
          Product.builder()
              .productId("i" + i)
              .productName("product" + i)
              .productThumbnail("thumbnail" + i)
              .build();
      productMongoRepository.save(product);
    }
    productQueryOutPort.findProductByProductIds(productIds);
  }

  @Test
  @DisplayName("상품 가격 유효성 검사 ")
  void findProductPriceValid() {
    for (int i = 0; i < 4; i++) {
      Product product = Product.builder().productId("1" + i).productPrice(1L + i).build();
      productMongoRepository.save(product);
    }
    List<IsProductPriceValid> productPriceValids = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      IsProductPriceValid isProductPriceValid =
          IsProductPriceValid.builder().price(1L + i).productId("1" + i).build();
      productPriceValids.add(isProductPriceValid);
    }
    boolean productPriceValid = productQueryOutPort.findProductPriceValid(productPriceValids);
    assertThat(productPriceValid).isTrue();
  }

  @Test
  @DisplayName("대표꽃 id 조회")
  void findRepresentativeFlower() {
    productMongoRepository.deleteAll();
    ProductFlowers productFlowers =
        ProductFlowers.builder().isRepresentative(true).flowerName("sdf").flowerId(1L).build();
    Product product =
        Product.builder().productId("123").productFlowers(List.of(productFlowers)).build();
    productMongoRepository.save(product);
    Product product1 =
        productMongoRepository.findByProductId("123").orElseThrow(ProductNotFoundException::new);
    assertThat(product1.getProductId()).isEqualTo(product.getProductId());
  }

  @Test
  void testFindRepresentativeFlower() {
    productMongoRepository.deleteAll();
    ProductFlowers productFlowers =
        ProductFlowers.builder().isRepresentative(true).flowerName("sdf").flowerId(1L).build();
    Product product =
        Product.builder().productId("123").productFlowers(List.of(productFlowers)).build();
    productMongoRepository.save(product);
    Product product1 =
        productMongoRepository.findByProductId("123").orElseThrow(ProductNotFoundException::new);
    RepresentativeFlowerId representativeFlower =
        productQueryOutPort.findRepresentativeFlower(product1.getProductId());
    Long flowerId = product1.getProductFlowers().get(0).getFlowerId();
    assertThat(representativeFlower.getFlowerId()).isNotNull();
    assertThat(representativeFlower.getFlowerId()).isEqualTo(flowerId);
  }

  @Test
  @DisplayName("상품 조회 storeId 기준 map")
  void findProductsByProductIds() {
    productMongoRepository.deleteAll();
    Long storeId = 1L;
    Long productId = 1L;
    for (int j = 0; j < 3; j++) {
      Product product =
          Product.builder()
              .productId("id" + productId)
              .productName("name" + j)
              .storeId(storeId)
              .build();
      productId++;
      productMongoRepository.save(product);
    }

    List<Product> all = productMongoRepository.findAll();
    assertThat(all.size()).isEqualTo(3);
    List<String> productIds = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productIds.add("id" + i);
    }
    Map<Long, List<Product>> productsByProductIds =
        productQueryOutPort.findProductsByProductIdsForCartItem(productIds);
    System.out.println(productsByProductIds.keySet());
    assertThat(productsByProductIds.size()).isEqualTo(1);
    assertThat(productsByProductIds.get(1L).size()).isEqualTo(2);
  }

  @Test
  @DisplayName("가게별 평균 평점 업데이트")
  void findStoreAverageRating() {
    productMongoRepository.deleteAll();
    for (int i = 0; i < 5; i++) {
      Product product =
          Product.builder().productId("i" + i).averageRating(1.0 + i).storeId(1L).build();
      productMongoRepository.save(product);
    }

    List<Product> all = productMongoRepository.findAll();
    for (Product product : all) {
      System.out.println(product.getStoreId() + " : " + product.getAverageRating());
    }

    Map<Long, Double> storeAverageRating = productQueryOutPort.findStoreAverageRating();
    for (Long key : storeAverageRating.keySet()) {
      System.out.println(key);
    }

    //        assertThat(storeAverageRating.keySet().size()).isEqualTo(3);
  }
}
