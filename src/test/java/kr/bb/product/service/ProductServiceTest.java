package kr.bb.product.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.transaction.Transactional;
import kr.bb.product.dto.category.Category;
import kr.bb.product.dto.request.ProductRequestData;
import kr.bb.product.dto.tag.Tag;
import kr.bb.product.entity.Flowers;
import kr.bb.product.entity.Product;
import kr.bb.product.entity.ProductSaleStatus;
import kr.bb.product.mapper.ProductMapper;
import kr.bb.product.repository.ProductMongoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ProductServiceTest {
  @Autowired ProductMongoRepository productMongoRepository;
  @Autowired ProductMapper productMapper;
  @Autowired ProductService productService;

  @Test
  @DisplayName("상품 등록 service")
  void createProduct() {
    ProductRequestData product =
        ProductRequestData.builder()
            .category(Category.builder().categoryName("category").categoryId(1L).build())
            .productName("Example Product")
            .productSummary("Product Summary")
            .productPrice(100L)
            .productSaleStatus(ProductSaleStatus.SALE)
            .tag(Tag.builder().tagName("tagname").tagId(1L).build())
            .productFlowers(Flowers.builder().flowerName("flower1").stock(3L).flowerId(1L).build())
            .productDescriptionImage("image_url")
            .reviewCount(5L)
            .productSaleAmount(50L)
            .averageRating(4.5)
            .storeId(1L)
            .build();
    Product product1 = productMapper.entityToData(product);
    Product save = productMongoRepository.save(product1);
    assertThat(product1.getProductId()).isEqualTo(save.getProductId());
  }

  @Test
  @DisplayName("상품 등록 태그 null case")
  void createProductIfTagIsNull() {
    ProductRequestData product =
        ProductRequestData.builder()
            .category(Category.builder().categoryName("category").categoryId(1L).build())
            .productName("Example Product")
            .productSummary("Product Summary")
            .productPrice(100L)
            .productSaleStatus(ProductSaleStatus.SALE)
            .productFlowers(Flowers.builder().flowerName("flower1").stock(3L).flowerId(1L).build())
            .productDescriptionImage("image_url")
            .reviewCount(5L)
            .productSaleAmount(50L)
            .averageRating(4.5)
            .storeId(1L)
            .build();
    productService.createProduct(product);
  }

  @Test
  @DisplayName("상품 등록 필수 값 null인 경우 에러 반환")
  void createProductIfNotNullTest() {
    ProductRequestData product =
        ProductRequestData.builder()
            .category(Category.builder().categoryName("category").categoryId(1L).build())
            .productSummary("Product Summary")
            .productPrice(100L)
            .productSaleStatus(ProductSaleStatus.SALE)
            .productFlowers(Flowers.builder().flowerName("flower1").stock(3L).flowerId(1L).build())
            .productDescriptionImage("image_url")
            .reviewCount(5L)
            .productSaleAmount(50L)
            .averageRating(4.5)
            .storeId(1L)
            .build();
    Product product1 = productMapper.entityToData(product);
    Product save = productMongoRepository.save(product1);
    assertThat(save.getProductName()).isNotNull();
  }
}
