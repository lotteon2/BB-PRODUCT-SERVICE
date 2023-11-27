package kr.bb.product.domain.product.adapter.out.mongo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.bb.product.domain.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductQueryRepositoryTest {
  @Autowired ProductQueryRepository productQueryRepository;
  @Autowired ProductRepository productRepository;

  @Test
  void createProduct() {
    Product build =
        Product.builder()
            .productName("name")
            .productSummary("summary")
            .productPrice(12L)
            .productDescriptionImage("images")
            .productThumbnail("thumbnail")
            .build();
    productQueryRepository.createProduct(build);
    List<Product> all = productRepository.findAll();
    assertThat(all.get(0).getProductName()).isEqualTo(build.getProductName());
    assertThat(all.get(0).getCategory()).isNull();
  }
}
