package kr.bb.product.domain.product.repository.mongo;

import kr.bb.product.domain.product.entity.Product;

public interface ProductCustomMongoRepository {
  void updateProductSaleStatus(Product product);
}
