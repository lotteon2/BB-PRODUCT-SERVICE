package kr.bb.product.domain.product.adapter.out.mongo;

import kr.bb.product.domain.product.application.port.out.ProductCommandOutPort;
import kr.bb.product.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductCommandRepository implements ProductCommandOutPort {
  private final ProductMongoRepository productMongoRepository;

  @Override
  public void createProduct(Product productRequestToEntity) {
    productMongoRepository.save(productRequestToEntity);
  }
}
