package kr.bb.product.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import kr.bb.product.entity.ProductSaleStatus;
import kr.bb.product.entity.product.Category;
import kr.bb.product.entity.product.Flowers;
import kr.bb.product.entity.product.Product;
import kr.bb.product.entity.product.Tag;
import kr.bb.product.repository.mongo.ProductMongoRepository;
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
    List<Tag> tagList = new ArrayList<>();
    tagList.add(Tag.builder().tagId(1L).tagName("tagname").build());

    List<Flowers> list = new ArrayList<>();
    list.add(Flowers.builder().isRepresentative(true).flowerId(2L).flowerCount(2L).build());
    list.add(Flowers.builder().flowerId(3L).flowerCount(2L).build());
    list.add(Flowers.builder().flowerId(4L).flowerCount(2L).build());
    Product product =
        Product.builder()
            .productId("123")
            .category(Category.builder().categoryName("category").categoryId(1L).build())
            .productName("Example Product")
            .productSummary("Product Summary")
            .productPrice(100L)
            .productSaleStatus(ProductSaleStatus.SALE)
            .tag(tagList)
            .productFlowers(list)
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
