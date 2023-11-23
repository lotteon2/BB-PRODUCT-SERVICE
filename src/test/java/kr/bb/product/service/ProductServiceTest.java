package kr.bb.product.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import kr.bb.product.dto.request.FlowersRequestData;
import kr.bb.product.dto.request.ProductRequestData;
import kr.bb.product.entity.product.Tag;
import kr.bb.product.mapper.ProductMapper;
import kr.bb.product.repository.jpa.TagRepository;
import kr.bb.product.repository.mongo.ProductMongoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ProductServiceTest {
  @Autowired ProductMongoRepository productMongoRepository;
  @Autowired TagRepository tagRepository;
  @Autowired ProductMapper productMapper;
  @Autowired ProductService productService;

  @Test
  @DisplayName("상품 등록 service")
  void createProduct() {
    // given
    // category, tag를 따로 조회해야 함

    // flowers
    List<FlowersRequestData> list = new ArrayList<>();
    list.add(FlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());
    list.add(FlowersRequestData.builder().flowerId(3L).flowerCount(2L).build());
    list.add(FlowersRequestData.builder().flowerId(4L).flowerCount(2L).build());
    List<Long> tagList = new ArrayList<>();
    tagList.add(1L);
    tagList.add(2L);

    List<Tag> allById = tagRepository.findAllById(tagList);

    ProductRequestData product =
        ProductRequestData.builder()
            .productName("Example Product")
            .productSummary("Product Summary")
            .productDescriptionImage("image")
            .productThumbnail("thumbnail")
            .productPrice(100L)
            .productTag(tagList)
            .representativeFlower(
                FlowersRequestData.builder().flowerId(1L).flowerCount(3L).build())
            .flowers(list)
            .productDescriptionImage("image_url")
            .build();
    product.setStoreId(1L);
  }
}
