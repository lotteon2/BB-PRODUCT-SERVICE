package kr.bb.product.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import kr.bb.product.dto.request.ProductRequestData;
import kr.bb.product.entity.Category;
import kr.bb.product.entity.Product;
import kr.bb.product.entity.Tag;
import kr.bb.product.errors.CategoryNotFoundException;
import kr.bb.product.mapper.ProductMapper;
import kr.bb.product.repository.jpa.CategoryRepository;
import kr.bb.product.repository.jpa.TagRepository;
import kr.bb.product.repository.mongo.ProductMongoRepository;
import kr.bb.product.vo.Flowers;
import kr.bb.product.vo.FlowersRequestData;
import org.assertj.core.api.Assertions;
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
  @DisplayName("상품 등록 service logic")
  void createProductServiceLogic() {
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
    Category category = categoryRepository.findById(1L).orElseThrow(CategoryNotFoundException::new);

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
    Product product1 = productMapper.entityToData(product, category, allById, list);
    Product save = productMongoRepository.save(product1);
    System.out.println(save.toString());
    Assertions.assertThat(save).isNotNull();
  }

  @Test
  @DisplayName("상품 등록 service")
  void createProductService() {
    List<Long> tagList = new ArrayList<>();
    tagList.add(1L);
    tagList.add(2L);

    List<FlowersRequestData> list = new ArrayList<>();
    list.add(FlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
    list.add(FlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
    list.add(FlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

    ProductRequestData product =
        ProductRequestData.builder()
            .categoryId(1L)
            .productTag(tagList)
            .representativeFlower(FlowersRequestData.builder().flowerCount(3L).flowerId(1L).build())
            .flowers(list)
            .productName("Example Product")
            .productSummary("Product Summary")
            .productDescriptionImage("image")
            .productThumbnail("thumbnail")
            .productPrice(100L)
            .productDescriptionImage("image_url")
            .build();
    productService.createProduct(product);
    List<Product> all = productMongoRepository.findAll();
    System.out.println(all.toString());
    Assertions.assertThat(all.size()).isGreaterThan(0);
  }
}
