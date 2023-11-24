package kr.bb.product.domain.product.repository.mongo;

import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.salesresume.entity.ProductSaleStatus;

public interface ProductCustomMongoRepository {
  void updateProductSaleStatus(Product product, ProductSaleStatus productSaleStatus);
}
