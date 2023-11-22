package kr.bb.product.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import kr.bb.product.dto.category.Category;
import kr.bb.product.dto.tag.Tag;
import kr.bb.product.entity.Flowers;
import kr.bb.product.entity.Product;
import kr.bb.product.entity.ProductSaleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ProductMongoRepositoryTest {
  @Autowired ProductMongoRepository productMongoRepository;

  @Test
  @DisplayName("상품 등록")
  void createProduct() {
    Product product =
        Product.builder()
            .productId("123")
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
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    Product save = productMongoRepository.save(product);
    assertThat(save.getProductId()).isNotNull();

    System.out.println(save);
  }
}
