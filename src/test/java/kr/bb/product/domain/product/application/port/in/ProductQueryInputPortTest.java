package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import bloomingblooms.domain.flower.StockChangeDto;
import bloomingblooms.domain.notification.order.OrderType;
import bloomingblooms.domain.order.ProcessOrderDto;
import bloomingblooms.domain.product.IsProductPriceValid;
import bloomingblooms.domain.product.ProductInfoDto;
import bloomingblooms.domain.product.ProductInformation;
import bloomingblooms.domain.product.ProductThumbnail;
import bloomingblooms.domain.product.StoreSubscriptionProductId;
import bloomingblooms.domain.wishlist.likes.LikedProductInfoResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import kr.bb.product.config.MockingTestConfiguration;
import kr.bb.product.config.TestEnv;
import kr.bb.product.config.mock.MockingApi;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.flower.mapper.FlowerCommand.ProductFlowers;
import kr.bb.product.domain.flower.mapper.FlowerCommand.ProductFlowersRequestData;
import kr.bb.product.domain.product.adapter.out.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.mapper.ProductCommand;
import kr.bb.product.domain.product.mapper.ProductCommand.AdminSelectOption;
import kr.bb.product.domain.product.mapper.ProductCommand.BestSellerTopTen;
import kr.bb.product.domain.product.mapper.ProductCommand.LanguageOfFlower;
import kr.bb.product.domain.product.mapper.ProductCommand.ProductList;
import kr.bb.product.domain.product.mapper.ProductCommand.ProductListItem;
import kr.bb.product.domain.product.mapper.ProductCommand.ProductRegister;
import kr.bb.product.domain.product.mapper.ProductCommand.ProductsForAdmin;
import kr.bb.product.domain.product.mapper.ProductCommand.SortOption;
import kr.bb.product.domain.product.mapper.ProductCommand.StoreProductDetail;
import kr.bb.product.domain.product.mapper.ProductCommand.StoreProductList;
import kr.bb.product.domain.product.mapper.ProductCommand.SubscriptionProductForCustomer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ContextConfiguration(classes = {MockingTestConfiguration.class})
class ProductQueryInputPortTest extends TestEnv {
  @MockBean RedissonAutoConfiguration redissonAutoConfiguration;
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired ProductCommandInputPort productCommandInputPort;
  @Autowired WireMockServer mockCacheApi;
  @Autowired private ProductMongoRepository productMongoRepository;
  @Autowired private ProductQueryInputPort productQueryInputPort;

  private void extracted() {
    for (int i = 0; i < 10; i++) {
      List<Long> tagList = new ArrayList<>();
      tagList.add(1L);
      tagList.add(2L);

      List<ProductFlowersRequestData> list = new ArrayList<>();
      list.add(ProductFlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
      list.add(ProductFlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
      list.add(ProductFlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

      ProductCommand.ProductRegister product =
          ProductCommand.ProductRegister.builder()
              .categoryId(1L)
              .productTag(tagList)
              .representativeFlower(
                  ProductFlowersRequestData.builder().flowerCount(3L).flowerId(1L).build())
              .flowers(list)
              .productName("Example Product")
              .productSummary("Product Summary")
              .productDescriptionImage("image")
              .storeId(1L)
              .productThumbnail("thumbnail")
              .productPrice(100L + i)
              .productDescriptionImage("image_url")
              .build();
      productCommandInputPort.createProduct(product);
    }
  }

  @Test
  @DisplayName("가게 사장 상품 상세 조회")
  void getStoreProductDetail() {
    productMongoRepository.deleteAll();
    extracted();
    Product product = productMongoRepository.findAll().get(0);

    StoreProductDetail storeProductDetail =
        productQueryInputPort.getStoreProductDetail(1L, product.getProductId());
    System.out.println(storeProductDetail.getRepresentativeFlower().getFlowerName());
    assertThat(storeProductDetail.getRepresentativeFlower().getFlowerName()).isNotNull();
  }

  @Test
  @DisplayName("가게 사장 상품 리스트 조회 ")
  void getStoreProducts() {
    productMongoRepository.deleteAll();
    ProductFlowers build1 = ProductFlowers.builder().flowerId(1L).isRepresentative(true).build();
    ProductFlowersRequestData flowers =
        ProductFlowersRequestData.builder().flowerId(1L).flowerCount(4L).build();
    List<Long> list = new ArrayList<>();
    List<ProductFlowersRequestData> lst = new ArrayList<>();
    lst.add(flowers);
    list.add(1L);
    for (int i = 0; i < 10; i++) {
      ProductRegister build =
          ProductRegister.builder()
              .productName("name")
              .storeId(1L)
              .categoryId(1L)
              .flowers(lst)
              .productPrice(1L + i)
              .productTag(list)
              .representativeFlower(
                  ProductFlowersRequestData.builder().flowerCount(3L).flowerId(1L).build())
              .build();
      productCommandInputPort.createProduct(build);
    }
    productMongoRepository
        .findAll()
        .forEach(item -> System.out.println(item.getProductFlowers().toString()));

    PageRequest pageRequest = PageRequest.of(0, 5);

    ProductList productsByCategory =
        productQueryInputPort.getProductsByCategory(1L, 1L, 1L, SortOption.TOP_SALE, pageRequest);
    StoreProductList storeProducts =
        productQueryInputPort.getStoreProducts(1L, 1L, null, ProductSaleStatus.SALE, pageRequest);
    assertThat(storeProducts.getProducts().size()).isGreaterThan(0);
  }

  @Test
  @DisplayName("카테고리별 상품 리스트 조회")
  void getProductsByCategory() {
    productMongoRepository.deleteAll();
    extracted();
    PageRequest pageRequest = PageRequest.of(0, 5);
    ProductList productsByCategory =
        productQueryInputPort.getProductsByCategory(1L, null, SortOption.LOW, pageRequest);
    List<ProductListItem> products = productsByCategory.getProducts();
    assertThat(products.get(0).getProductPrice() < products.get(1).getProductPrice()).isTrue();
  }

  @Test
  @DisplayName("카테고리별 상품 리스트 조회 - login ")
  void getProductsByCategoryLogin() {
    productMongoRepository.deleteAll();
    extracted();
    PageRequest pageRequest = PageRequest.of(0, 5);
    ProductList productsByCategory =
        productQueryInputPort.getProductsByCategory(1L, 1L, SortOption.LOW, pageRequest);
    List<ProductListItem> products = productsByCategory.getProducts();
    assertThat(products.get(0).getProductPrice() < products.get(1).getProductPrice()).isTrue();
  }

  @Test
  @DisplayName("태그별 상품 리스트 조회 - login ")
  void getProductsByTagLogin() {
    productMongoRepository.deleteAll();
    extracted();
    PageRequest pageRequest = PageRequest.of(0, 5);
    MockingApi.setUpProductsLikes(mockCacheApi);
    ProductList productsByTag =
        productQueryInputPort.getProductsByTag(1L, 1L, 1L, SortOption.TOP_SALE, pageRequest);
    assertThat(productsByTag.getProducts().size()).isEqualTo(5);
    assertThat(productsByTag.getProducts().size()).isEqualTo(5);
  }

  @Test
  @DisplayName("태그별 상품 리스트 조회 - 비로그인  ")
  void getProductsByTag() {
    productMongoRepository.deleteAll();
    extracted();
    PageRequest pageRequest = PageRequest.of(0, 5);
    ProductList productsByTag =
        productQueryInputPort.getProductsByTag(null, 1L, 1L, SortOption.TOP_SALE, pageRequest);
    assertThat(productsByTag.getProducts().size()).isEqualTo(5);
    assertThat(productsByTag.getProducts().size()).isEqualTo(5);
  }

  @Test
  @DisplayName("태그별 상품 리스트 조회 - 비로그인  ")
  void getProductsByTagNotLogin() {
    productMongoRepository.deleteAll();
    extracted();
    PageRequest pageRequest = PageRequest.of(0, 5);
    ProductList productsByTag =
        productQueryInputPort.getProductsByTag(1L, 1L, SortOption.TOP_SALE, pageRequest);
    assertThat(productsByTag.getProducts().size()).isEqualTo(5);
    assertThat(productsByTag.getProducts().size()).isEqualTo(5);
  }

  @DisplayName("베스트 셀러 10개 정보")
  void getBestSellerTopTen() {
    productMongoRepository.deleteAll();
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
    BestSellerTopTen bestSellerTopTen = productQueryInputPort.getBestSellerTopTen(1L);
    assertThat(bestSellerTopTen.getProducts().size()).isEqualTo(10);
    assertThat(bestSellerTopTen.getProducts().get(0).getData().get(0)).isEqualTo(19L);
  }

  @Test
  @DisplayName("가게 사장 구독 상품 조회")
  void getSubscriptionProductByStoreId() {
    productMongoRepository.deleteAll();
    Product build = Product.builder().isSubscription(true).storeId(1L).productName("name").build();
    productMongoRepository.save(build);
    List<Product> all = productMongoRepository.findAll();
    assertThat(all.get(0).getIsSubscription()).isTrue();
    assertThat(all.get(0).getProductName()).isEqualTo(build.getProductName());
  }

  //  @Test
  //  @DisplayName("메인 페이지 상품 조회 - 로그인 ")
  //  void getMainPageProducts() {
  //    productMongoRepository.deleteAll();
  //    extracted();
  //    MainPageProductItems mainPageProducts =
  //        productQueryInputPort.getMainPageProducts(1L, SelectOption.RATING);
  //    assertThat(mainPageProducts.getProducts().size()).isEqualTo(4);
  //  }

  //  @Test
  //  @DisplayName("메인 페이지 상품 조회 - 비 로그인 ")
  //  void getMainPageProductsNotLogin() {
  //    productMongoRepository.deleteAll();
  //    extracted();
  //    MainPageProductItems mainPageProducts =
  //        productQueryInputPort.getMainPageProducts(SelectOption.RATING);
  //    assertThat(mainPageProducts.getProducts().size()).isEqualTo(4);
  //  }

  @Test
  @DisplayName("구독 상품 상세 - 로그인 ")
  void getSubscriptionProductDetail() {
    productMongoRepository.deleteAll();
    Product build =
        Product.builder()
            .productId("123")
            .isSubscription(true)
            .storeId(1L)
            .productName("name")
            .build();
    productMongoRepository.save(build);
    // mocking
    MockingApi.setUpProductDetailLikes(mockCacheApi);

    SubscriptionProductForCustomer subscriptionProductDetail =
        productQueryInputPort.getSubscriptionProductDetail(1L, 1L);
    assertThat(subscriptionProductDetail.getProductName()).isEqualTo(build.getProductName());
    assertThat(subscriptionProductDetail.getIsLiked()).isTrue();
  }

  @Test
  @DisplayName("구독 상품 상세 - 비 로그인 ")
  void getSubscriptionProductDetailNotLogin() {
    productMongoRepository.deleteAll();
    Product build =
        Product.builder()
            .productId("123")
            .isSubscription(true)
            .storeId(1L)
            .productName("name")
            .build();
    productMongoRepository.save(build);

    SubscriptionProductForCustomer subscriptionProductDetail =
        productQueryInputPort.getSubscriptionProductDetail(1L);
    assertThat(subscriptionProductDetail.getProductName()).isEqualTo(build.getProductName());
    assertThat(subscriptionProductDetail.getIsLiked()).isFalse();
  }

  @Test
  @DisplayName("상품 썸네일 조회")
  void getProductThumbnail() {
    Product product = Product.builder().productThumbnail("thumbnail").productId("123").build();
    productMongoRepository.save(product);
    ProductThumbnail productThumbnail = productQueryInputPort.getProductThumbnail("123");
    assertThat(productThumbnail.getProductThumbnail()).isEqualTo(product.getProductThumbnail());
  }

  @Test
  @DisplayName("상품 정보 요청 feign ")
  void getProductInformation() {
    List<String> productIds = new ArrayList<>();
    List<Product> list = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productIds.add(i + "i");
      Product build =
          Product.builder()
              .productName("name")
              .productThumbnail("image")
              .productId(i + "i")
              .build();
      list.add(build);
      productMongoRepository.save(build);
    }
    List<ProductInformation> productInformation =
        productQueryInputPort.getProductInformation(productIds);
    List<String> collect =
        list.stream().map(Product::getProductThumbnail).collect(Collectors.toList());

    assertThat(productInformation.size()).isEqualTo(3);
    assertThat(productInformation.get(0).getProductThumbnail()).contains(collect);
  }

  @Test
  @DisplayName("상품 가격 유효성 검사")
  void getProductPriceValidation() {
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
    productQueryInputPort.getProductPriceValidation(productPriceValids);
  }

  @Test
  @DisplayName("구독 상품 id 조회 ")
  void getStoreSubscriptionProductId() {
    productMongoRepository.deleteAll();
    Product product = Product.builder().productId("123").storeId(1L).isSubscription(true).build();
    productMongoRepository.save(product);

    StoreSubscriptionProductId storeSubscriptionProductId =
        productQueryInputPort.getStoreSubscriptionProductId(1L);
    assertThat(storeSubscriptionProductId.getSubscriptionProductId())
        .isEqualTo(product.getProductId());
  }

  @Test
  @DisplayName("구독 상품 정보 요청")
  void getSubscriptionProductInformation() {
    productMongoRepository.deleteAll();
    Product product =
        Product.builder()
            .productName("name")
            .productThumbnail("thumbnail")
            .productPrice(123L)
            .storeId(1L)
            .isSubscription(true)
            .productId("123")
            .build();
    productMongoRepository.save(product);
    ProductInfoDto subscriptionProductInformation =
        productQueryInputPort.getSubscriptionProductInformation("123");
    assertThat(subscriptionProductInformation.getUnitPrice()).isEqualTo(product.getProductPrice());
  }

  @Test
  @DisplayName("꽃말 조회")
  void testGetLanguageOfFlower() {
    productMongoRepository.deleteAll();
    ProductFlowers productFlowers =
        ProductFlowers.builder().isRepresentative(true).flowerName("sdf").flowerId(1L).build();
    Product product =
        Product.builder().productId("123").productFlowers(List.of(productFlowers)).build();
    productMongoRepository.save(product);
    LanguageOfFlower languageOfFlower = productQueryInputPort.getLanguageOfFlower("123");
    assertThat(languageOfFlower.getLanguageOfFlower()).isEqualTo("장미 꽃말");
  }

  @Test
  @DisplayName("찜 상품 정보 조회 ")
  void getProductInformationForLikes() {
    productMongoRepository.deleteAll();
    List<String> productIds = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      productIds.add(i + "1");
      Product product =
          Product.builder()
              .productId(i + "1")
              .productName("name" + i)
              .productSummary("summary" + i)
              .averageRating(0.1 + i)
              .productPrice(1L + i)
              .productThumbnail("thumbnail" + i)
              .build();
      productMongoRepository.save(product);
    }
    List<LikedProductInfoResponse> productInformationForLikes =
        productQueryInputPort.getProductInformationForLikes(productIds);
    assertThat(productInformationForLikes.size()).isEqualTo(4);
  }

  @Test
  @DisplayName("꽃 재고 차감 요청 데이터")
  void getFlowerAmountGroupByStoreId() {
    productMongoRepository.deleteAll();
    Map<String, Long> map = new HashMap<>();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 4; j++) {
        map.put("i" + i + j, 3L);
        Product product =
            Product.builder()
                .productId("i" + i + j)
                .storeId(i + 1L)
                .productFlowers(
                    List.of(ProductFlowers.builder().flowerId(j + i + 1L).flowerCount(3L).build()))
                .build();
        productMongoRepository.save(product);
      }
    }

    List<StockChangeDto> order =
        productQueryInputPort.getFlowerAmountGroupByStoreId(
            ProcessOrderDto.builder()
                .products(map)
                .orderId("order")
                .couponIds(List.of(1L))
                .orderType(OrderType.DELIVERY.getOrderType())
                .phoneNumber("")
                .build());
    order
        .get(0)
        .getStockDtos()
        .forEach(item -> System.out.println(item.getStock() + ": " + item.getFlowerId()));
    assertThat(order.size()).isEqualTo(3);
  }

  @Test
  @DisplayName("관리자 상품 리스트 조회")
  void getProductsForAdmin() throws InterruptedException {
    productMongoRepository.deleteAll();
    Random random = new Random();
    for (int i = 0; i < 10; i++) {
      Product product =
          Product.builder()
              .storeId(1L + i)
              .productPrice(1000L + random.nextInt())
              .createdAt(LocalDateTime.now())
              .build();
      Thread.sleep(100);
      productMongoRepository.save(product);
    } // 10개 상품 저장
    AdminSelectOption selectOption =
        AdminSelectOption.builder()
            .salesAmount(SortOption.BOTTOM_SALE)
            .build(); // 전체, 최신순, 상품 가격 높은 순
    PageRequest pageRequest = PageRequest.of(0, 7);
    ProductsForAdmin productsForAdmin =
        productQueryInputPort.getProductsForAdmin(selectOption, pageRequest);
    assertThat(productsForAdmin.getProducts().get(0).getProductPrice())
        .isLessThan(productsForAdmin.getProducts().get(1).getProductPrice());
  }

  @Test
  @DisplayName("구독 상품 없는 경우 null 처리 ")
  void testGetSubscriptionProductByStoreId() {
    StoreSubscriptionProductId storeSubscriptionProductId =
        productQueryInputPort.getStoreSubscriptionProductId(10L);
    assertThat(storeSubscriptionProductId.getSubscriptionProductId()).isNull();
  }
}
