package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.amazonaws.services.sqs.model.AmazonSQSException;
import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.flower.mapper.FlowerCommand.ProductFlowersRequestData;
import kr.bb.product.domain.product.adapter.out.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.mapper.ProductCommand;
import kr.bb.product.domain.product.mapper.ProductCommand.ProductUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductStoreInputPortTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired private ProductCommandInputPort productStoreInputPort;
  @Autowired private ProductMongoRepository productMongoRepository;
  @Autowired private ProductQueryOutPort productQueryOutPort;

  private ProductCommand.ProductRegister getProductRequestData() {
    List<Long> tagList = new ArrayList<>();
    tagList.add(1L);
    tagList.add(2L);

    List<ProductFlowersRequestData> list = new ArrayList<>();
    list.add(ProductFlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

    return ProductCommand.ProductRegister.builder()
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
  }

  private ProductCommand.ProductUpdate updateProductDiscontinued() {
    List<Long> tagList = new ArrayList<>();
    tagList.add(1L);
    tagList.add(2L);

    List<ProductFlowersRequestData> list = new ArrayList<>();
    list.add(ProductFlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

    return ProductCommand.ProductUpdate.builder()
        .productTag(tagList)
        .productSaleStatus(ProductSaleStatus.DISCONTINUED)
        .productName("Example Product")
        .productSummary("Product Summary")
        .productDescriptionImage("image")
        .productThumbnail("thumbnail")
        .productPrice(100L)
        .productDescriptionImage("image_url")
        .build();
  }

  private ProductCommand.ProductUpdate updateProductDeleted() {
    List<Long> tagList = new ArrayList<>();
    tagList.add(1L);
    tagList.add(2L);

    List<ProductFlowersRequestData> list = new ArrayList<>();
    list.add(ProductFlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

    return ProductCommand.ProductUpdate.builder()
        .productTag(tagList)
        .productSaleStatus(ProductSaleStatus.DELETED)
        .productName("Example Product")
        .productSummary("Product Summary")
        .productDescriptionImage("image")
        .productThumbnail("thumbnail")
        .productPrice(100L)
        .productDescriptionImage("image_url")
        .build();
  }

  @Test
  @DisplayName("상품 추가 input port")
  void createProductPort() {
    // Arrange
    ProductCommand.ProductRegister productRequestData = getProductRequestData();

    // Mock dependencies
    // Use Mockito to mock external dependencies (productStoreInputPort and productOutPort)

    // Act
    productStoreInputPort.createProduct(productRequestData);

    // Assert
    // Verify that the product is added
    List<Product> allProducts = productMongoRepository.findAll();
    assertThat(allProducts.size()).isGreaterThan(0);

    // Add more assertions based on your specific requirements and edge cases
  }

  @Test
  @DisplayName("Update product sale status - ProductSaleStatus DELETED")
  void updateProductSaleStatusDeleted() {
    // Arrange
    ProductCommand.ProductUpdate productRequestData = updateProductDiscontinued();
    Product product = createProduct();

    // Act
    productStoreInputPort.updateProduct(product.getProductId(), productRequestData);
    Product byProductId = productQueryOutPort.findByProductId(product.getProductId());
    assertThat(byProductId.getProductSaleStatus()).isEqualTo(ProductSaleStatus.DISCONTINUED);
  }

  @Test
  @DisplayName("Update product sale status - ProductSaleStatus NOT DELETED")
  void updateProductSaleStatusNotDeleted() {
    // Arrange
    ProductCommand.ProductUpdate productRequestData = updateProductDeleted();
    Product product = createProduct();

    // Act
    productStoreInputPort.updateProduct(product.getProductId(), productRequestData);
    Product byProductId = productQueryOutPort.findByProductId(product.getProductId());
    assertThat(byProductId.getProductSaleStatus()).isEqualTo(ProductSaleStatus.DELETED);
    assertThat(byProductId.getIsDeleted()).isEqualTo(true);
  }

  @Test
  @DisplayName("Update product sale status - ProductSaleStatus SALE")
  void updateProductSaleStatusSALE() {
    // Arrange

    ProductUpdate build =
        ProductUpdate.builder()
            .productTag(List.of(1L))
            .productSaleStatus(ProductSaleStatus.SALE)
            .build();
    Product product = createProduct();

    // Act
    assertThrows(
        AmazonSQSException.class,
        () -> productStoreInputPort.updateProduct(product.getProductId(), build));
  }

  private Product createProduct() {
    // Create and return a mock Product object
    productStoreInputPort.createProduct(getProductRequestData());
    return productMongoRepository.findAll().get(0);
  }
}
