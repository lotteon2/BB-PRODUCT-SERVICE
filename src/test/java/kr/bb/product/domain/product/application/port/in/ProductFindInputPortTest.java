package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.adapter.out.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
import kr.bb.product.domain.product.entity.ProductCommand.ProductListItem;
import kr.bb.product.domain.product.entity.ProductCommand.ProductsGroupByCategory;
import kr.bb.product.domain.product.entity.ProductCommand.SortOption;
import kr.bb.product.domain.product.infrastructure.client.WishlistServiceClient;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
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
class ProductFindInputPortTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired WishlistServiceClient wishlistServiceClient;
  @Autowired private ProductOutPort productOutPort;
  @Autowired private ProductCommandInputPort productStoreInputPort;
  @Autowired private ProductQueryInputPort productFindInputPort;
  @Autowired private ProductMongoRepository productMongoRepository;

  @Test
  @DisplayName("상품 카테고리 조회 페이징")
  void getProductsByCategory() {
    // given
    extracted();

    // when
    PageRequest pageRequest = PageRequest.of(0, 2);
    Page<Product> byCategory = productOutPort.findByCategory(1L, pageRequest);

    // 반환 객체로 변환
    List<ProductListItem> productByCategories = ProductList.fromEntity(byCategory.getContent());
    ProductList data = ProductList.getData(productByCategories, byCategory.getTotalPages());

    assertThat(data.getTotalCnt()).isEqualTo(5);
    // 찜 생략
    //    List<ProductByCategory> data1 =
    //        wishlistServiceClient.getProductsMemberLikes(1L, productByCategories).getData();
    //    System.out.println(data1.toString());
  }

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
              .productThumbnail("thumbnail")
              .productPrice(100L)
              .productDescriptionImage("image_url")
              .build();
      productStoreInputPort.createProduct(product);
    }
  }

  @Test
  @DisplayName("태그별 상품 리스트 조회")
  void getProductListByTagId() {
    productMongoRepository.deleteAll();
    extracted();
    PageRequest pageRequest = PageRequest.of(0, 3);
    Page<Product> productsByTagId = productOutPort.findProductsByTagId(1L, pageRequest);
    ProductsGroupByCategory productsByTag =
        productFindInputPort.getProductsByTag(1L, 1L, SortOption.LOW, pageRequest);
    assertThat(productsByTag.getProducts().size()).isEqualTo(5);
  }
}
