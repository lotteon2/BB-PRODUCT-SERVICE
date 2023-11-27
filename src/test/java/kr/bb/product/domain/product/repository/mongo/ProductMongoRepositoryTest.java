package kr.bb.product.domain.product.repository.mongo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.tag.entity.Tag;
import kr.bb.product.exception.errors.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductMongoRepositoryTest {
  @Autowired ProductMongoRepository productMongoRepository;
  @Autowired MongoTemplate mongoTemplate;

  @Test
  @DisplayName("상품 판매 상태를 DISCONTINUED로 변경")
  void updateSaleStatus() {
    List<Tag> tagList = new ArrayList<>();
    tagList.add(Tag.builder().tagId(1L).tagName("tagname").build());

    List<ProductFlowers> list = new ArrayList<>();
    list.add(ProductFlowers.builder().isRepresentative(true).flowerId(2L).flowerCount(2L).build());
    list.add(ProductFlowers.builder().flowerId(3L).flowerCount(2L).build());
    list.add(ProductFlowers.builder().flowerId(4L).flowerCount(2L).build());
    Product product =
        Product.builder()
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

    productMongoRepository.updateProductSaleStatus(save, ProductSaleStatus.DISCONTINUED);

    Product product1 =
        productMongoRepository
            .findByProductId(save.getProductId())
            .orElseThrow(ProductNotFoundException::new);

    System.out.println("FIND " + product1);

    Product updatedProduct =
        productMongoRepository
            .findByProductId(save.getProductId())
            .orElseThrow(ProductNotFoundException::new);

    assertThat(updatedProduct.getProductSaleStatus()).isEqualTo(ProductSaleStatus.DISCONTINUED);
  }
}
