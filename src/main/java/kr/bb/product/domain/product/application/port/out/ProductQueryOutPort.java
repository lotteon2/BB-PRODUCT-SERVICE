package kr.bb.product.domain.product.application.port.out;

import kr.bb.product.domain.product.entity.Product;

public interface ProductQueryOutPort {
  void createProduct(Product product);
}
