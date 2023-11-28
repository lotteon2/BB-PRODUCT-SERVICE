package kr.bb.product.domain.product.adapter.out.mongo;

import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository implements ProductQueryOutPort {
  private final ProductMongoRepository productMongoRepository;

  @Override
  public Page<Product> findProductByStoreId(Long storeId, Pageable pageable) {
    return productMongoRepository.findProductByStoreId(storeId, pageable);
  }
}
