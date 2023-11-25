package kr.bb.product.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import kr.bb.product.domain.category.entity.Category;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.adapter.out.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.vo.ProductFlowers;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import kr.bb.product.domain.tag.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
@Transactional
class ProductMongoRepositoryTest {

  private static final String CONNECTION_STRING = "mongodb://%s:%d";
  @Autowired ProductMongoRepository productMongoRepository;
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
            MongoClients.create(String.format(CONNECTION_STRING, ip, port)), "local");
  }

  @Test
  @DisplayName("상품 등록")
  void createProduct() {
    List<Tag> tagList = new ArrayList<>();
    tagList.add(Tag.builder().tagId(1L).tagName("tagname").build());

    List<ProductFlowers> list = new ArrayList<>();
    list.add(ProductFlowers.builder().isRepresentative(true).flowerId(2L).flowerCount(2L).build());
    list.add(ProductFlowers.builder().flowerId(3L).flowerCount(2L).build());
    list.add(ProductFlowers.builder().flowerId(4L).flowerCount(2L).build());
    Product product =
        Product.builder()
            .productId("111")
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
