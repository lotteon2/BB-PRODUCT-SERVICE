package kr.bb.product.service;

import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.category.repository.jpa.CategoryRepository;
import kr.bb.product.domain.product.api.request.ProductRequestData;
import kr.bb.product.domain.product.application.ProductService;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.mapper.ProductMapper;
import kr.bb.product.domain.product.repository.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.tag.entity.Tag;
import kr.bb.product.domain.tag.repository.jpa.TagRepository;
import kr.bb.product.exception.errors.CategoryNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
@Transactional
class ProductServiceTest {
  private static final String CONNECTION_STRING = "mongodb://%s:%d";
  @Autowired ProductMongoRepository productMongoRepository;
  @Autowired TagRepository tagRepository;
  @Autowired CategoryRepository categoryRepository;
  @Autowired ProductMapper productMapper;
  @Autowired ProductService productService;
  private MongodExecutable mongodExecutable;
  private MongoTemplate mongoTemplate;

  @AfterEach
  void clean() {
    mongodExecutable.stop();
  }

  @BeforeEach
  void setup() throws Exception {
    String ip = "localhost";
    int port = 27017;

    ImmutableMongodConfig mongodConfig =
        MongodConfig.builder()
            .version(Version.Main.PRODUCTION)
            .net(new Net(ip, port, Network.localhostIsIPv6()))
            .build();

    MongodStarter starter = MongodStarter.getDefaultInstance();
    mongodExecutable = starter.prepare(mongodConfig);
    mongodExecutable.start();
    mongoTemplate =
        new MongoTemplate(
            MongoClients.create(String.format(CONNECTION_STRING, ip, port)), "bb-product");
  }

  @Test
  @DisplayName("상품 등록 service logic")
  void createProductServiceLogic() {
    // given
    // category, tag를 따로 조회해야 함

    // flowers
    List<ProductFlowers> list = new ArrayList<>();
    list.add(ProductFlowers.builder().isRepresentative(true).flowerId(1L).flowerCount(2L).build());
    list.add(ProductFlowers.builder().flowerId(3L).flowerCount(3L).build());
    list.add(ProductFlowers.builder().flowerId(2L).flowerCount(2L).build());

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

    List<ProductFlowersRequestData> list = new ArrayList<>();
    list.add(ProductFlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

    ProductRequestData product =
        ProductRequestData.builder()
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
    productService.createProduct(product);
    List<Product> all = productMongoRepository.findAll();
    System.out.println(all.toString());
    Assertions.assertThat(all.size()).isGreaterThan(0);
  }
}
