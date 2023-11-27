package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductStoreInputPortTest {
  @Autowired private ProductStoreInputPort productStoreInputPort;
  @Autowired private ProductOutPort productOutPort;

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
        .categoryId(1L)
        .productTag(tagList)
        .productSaleStatus(ProductSaleStatus.DISCONTINUED)
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
   private ProductCommand.ProductUpdate updateProductDeleted() {
    List<Long> tagList = new ArrayList<>();
    tagList.add(1L);
    tagList.add(2L);

    List<ProductFlowersRequestData> list = new ArrayList<>();
    list.add(ProductFlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

    return ProductCommand.ProductUpdate.builder()
        .categoryId(1L)
        .productTag(tagList)
        .productSaleStatus(ProductSaleStatus.DELETED)
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
    List<Product> allProducts = productOutPort.findAll();
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
    productStoreInputPort.updateProductSaleStatus(product.getProductId(), productRequestData);
    Product byProductId = productOutPort.findByProductId(product.getProductId());
    assertThat(byProductId.getProductSaleStatus()).isEqualTo(ProductSaleStatus.DISCONTINUED);
  }

  @Test
  @DisplayName("Update product sale status - ProductSaleStatus NOT DELETED")
  void updateProductSaleStatusNotDeleted() {
    // Arrange
    ProductCommand.ProductUpdate productRequestData = updateProductDeleted();
    Product product = createProduct();

    // Act
    productStoreInputPort.updateProductSaleStatus(product.getProductId(), productRequestData);
    Product byProductId = productOutPort.findByProductId(product.getProductId());
    assertThat(byProductId.getProductSaleStatus()).isEqualTo(ProductSaleStatus.DISCONTINUED);
    assertThat(byProductId.getIsDeleted()).isEqualTo(true);
  }

  private Product createProduct() {
    // Create and return a mock Product object
    productStoreInputPort.createProduct(getProductRequestData());
    return productOutPort.findAll().get(0);
  }
}
