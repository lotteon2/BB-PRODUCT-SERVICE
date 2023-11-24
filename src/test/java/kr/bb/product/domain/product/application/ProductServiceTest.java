package kr.bb.product.domain.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.api.request.ProductRequestData;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.repository.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.exception.errors.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {
  @Autowired ProductMongoRepository productMongoRepository;
  @Autowired ProductService productService;

  @BeforeEach
  void setup() {
    productMongoRepository.deleteAll();
  }

  private ProductRequestData getProductRequestData() {
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
    return product;
  }

  @Test
  @DisplayName("상품 판매 상태 변경 service logic")
  void productSaleStatusChange() {
    ProductRequestData product = getProductRequestData();
    productService.createProduct(product);
    Product product1 = productMongoRepository.findAll().get(0);
    productMongoRepository.updateProductSaleStatus(product1, ProductSaleStatus.DELETED);

    Product product2 =
        productMongoRepository
            .findByProductId(product1.getProductId())
            .orElseThrow(ProductNotFoundException::new);
    assertThat(product2.getProductSaleStatus()).isEqualTo(ProductSaleStatus.DELETED);
  }

  @Test
  @DisplayName("product service test")
  void updateProductSaleStatus() {
    ProductRequestData productRequestData = getProductRequestData();
    productService.createProduct(productRequestData);
    Product product = productMongoRepository.findAll().get(0);
    productRequestData.setProductSaleStatus(ProductSaleStatus.DELETED);
    productService.updateProductSaleStatus(product.getProductId(), productRequestData);
    Product product1 =
        productMongoRepository
            .findByProductId(product.getProductId())
            .orElseThrow(ProductNotFoundException::new);
    assertThat(product1.getIsDeleted()).isEqualTo(true);
    assertThat(product1.getProductSaleStatus()).isEqualTo(ProductSaleStatus.DISCONTINUED);
  }

  @Test
  @DisplayName("product service test discontinued")
  void updateProductSaleStatusDiscontinued() {
    ProductRequestData productRequestData = getProductRequestData();
    productService.createProduct(productRequestData);
    Product product = productMongoRepository.findAll().get(0);
    assertThat(product.getProductSaleStatus()).isEqualTo(ProductSaleStatus.SALE);
    productRequestData.setProductSaleStatus(ProductSaleStatus.DISCONTINUED);
    productService.updateProductSaleStatus(product.getProductId(), productRequestData);
    Product product1 =
        productMongoRepository
            .findByProductId(product.getProductId())
            .orElseThrow(ProductNotFoundException::new);
    assertThat(product1.getIsDeleted()).isEqualTo(false);
    assertThat(product1.getProductSaleStatus()).isEqualTo(ProductSaleStatus.DISCONTINUED);
  }
}
