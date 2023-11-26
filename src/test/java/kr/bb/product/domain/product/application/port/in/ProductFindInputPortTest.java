package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import bloomingblooms.response.CommonResponse;
import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.api.request.ProductRequestData;
import kr.bb.product.domain.product.application.ProductService;
import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand.ProductByCategory;
import kr.bb.product.domain.product.entity.ProductCommand.ProductsByCategory;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.infrastructure.client.WishlistServiceClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductFindInputPortTest {
  @Autowired WishlistServiceClient wishlistServiceClient;
  @Autowired private ProductOutPort productOutPort;
  @Autowired private ProductService productService;

  @Test
  @DisplayName("상품 카테고리 조회 페이징")
  void getProductsByCategory() {
    // given
    for (int i = 0; i < 10; i++) {
      List<Long> tagList = new ArrayList<>();
      tagList.add(1L);
      tagList.add(2L);

      List<ProductFlowersRequestData> list = new ArrayList<>();
      list.add(ProductFlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
      list.add(ProductFlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
      list.add(ProductFlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

      ProductRequestData product =
          ProductRequestData.builder()
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
      productService.createProduct(product);
    }

    // when
    PageRequest pageRequest = PageRequest.of(0, 2);
    Page<Product> byCategory = productOutPort.findByCategory(1L, pageRequest);

    // 반환 객체로 변환
    List<ProductByCategory> productByCategories =
        ProductsByCategory.fromEntity(byCategory.getContent());
    ProductsByCategory data =
        ProductsByCategory.getData(productByCategories, byCategory.getTotalPages());

    assertThat(data.getTotalCnt()).isEqualTo(5);
    // 찜 생략
//    List<ProductByCategory> data1 =
//        wishlistServiceClient.getProductsMemberLikes(1L, productByCategories).getData();
//    System.out.println(data1.toString());
  }
}
