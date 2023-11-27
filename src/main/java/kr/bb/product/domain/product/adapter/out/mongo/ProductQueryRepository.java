package kr.bb.product.domain.product.adapter.out.mongo;

import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository implements ProductQueryOutPort {
  private final MongoTemplate mongoTemplate;
  private final ProductMongoRepository productMongoRepository;

  @Override
  public void createProduct(Product productRequestToEntity) {
    productMongoRepository.save(productRequestToEntity);
  }
}
