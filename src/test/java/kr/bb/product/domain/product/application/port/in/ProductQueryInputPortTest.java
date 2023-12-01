package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.adapter.out.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.StoreProductDetail;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductQueryInputPortTest {
  @Autowired private ProductCommandInputPort productStoreInputPort;
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
              .productPrice(100L)
              .productDescriptionImage("image_url")
              .build();
      productStoreInputPort.createProduct(product);
    }
  }

  @Test
  @DisplayName("가게 사장 상품 상세 조회")
  void getStoreProductDetail() {
    extracted();
    Product product = productMongoRepository.findAll().get(0);

    StoreProductDetail storeProductDetail =
        productQueryInputPort.getStoreProductDetail(1L, product.getProductId());
    System.out.println(storeProductDetail.getRepresentativeFlower().getFlowerName());
    assertThat(storeProductDetail.getRepresentativeFlower().getFlowerName()).isNotNull();
  }
}
