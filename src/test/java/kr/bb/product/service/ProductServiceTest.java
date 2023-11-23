package kr.bb.product.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import kr.bb.product.dto.request.ProductRequestData;
import kr.bb.product.entity.Category;
import kr.bb.product.entity.Tag;
import kr.bb.product.errors.CategoryNotFoundException;
import kr.bb.product.mapper.ProductMapper;
import kr.bb.product.repository.jpa.CategoryRepository;
import kr.bb.product.repository.jpa.TagRepository;
import kr.bb.product.repository.mongo.ProductMongoRepository;
import kr.bb.product.vo.Flowers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ProductServiceTest {
  @Autowired ProductMongoRepository productMongoRepository;
  @Autowired TagRepository tagRepository;
  @Autowired CategoryRepository categoryRepository;
  @Autowired ProductMapper productMapper;
  @Autowired ProductService productService;

  @Test
  @DisplayName("상품 등록 service")
  void createProduct() {
    // given
    // category, tag를 따로 조회해야 함

    // flowers
    List<Flowers> list = new ArrayList<>();
    list.add(Flowers.builder().isRepresentative(true).flowerId(1L).flowerCount(2L).build());
    list.add(Flowers.builder().flowerId(3L).flowerCount(3L).build());
    list.add(Flowers.builder().flowerId(2L).flowerCount(2L).build());

    List<Long> tagList = new ArrayList<>();
    tagList.add(1L);
    tagList.add(2L);

    List<Tag> allById = tagRepository.findAllById(tagList);
    List<Category> all = categoryRepository.findAll();
    System.out.println(all.toString());

    ProductRequestData product =
        ProductRequestData.builder()
            .productName("Example Product")
            .productSummary("Product Summary")
            .productDescriptionImage("image")
            .productThumbnail("thumbnail")
            .productPrice(100L)
            .productDescriptionImage("image_url")
            .build();
    product.setStoreId(1L);
//    productMapper.entityToData(product, category, allById, list);
  }
}
