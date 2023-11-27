package kr.bb.product.domain.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.application.port.in.ProductQueryInputPort;
import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {
  @Autowired ProductOutPort productOutPort;
  @Autowired
  ProductQueryInputPort productStoreInputPort;

  @BeforeEach
  void setup() {
    productOutPort.deleteAll();
  }

  private ProductCommand.ProductRegister getProductRequestData() {
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
    return product;
  }

  @Test
  @DisplayName("상품 판매 상태 변경 service logic")
  void productSaleStatusChange() {
    ProductCommand.ProductRegister product = getProductRequestData();
    productStoreInputPort.createProduct(product);
    Product product1 = productOutPort.findAll().get(0);
    productOutPort.updateProductSaleStatus(product1, ProductSaleStatus.DELETED);

    Product product2 =
        productOutPort
            .findByProductId(product1.getProductId());
    assertThat(product2.getProductSaleStatus()).isEqualTo(ProductSaleStatus.DELETED);
  }
}
