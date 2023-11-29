package kr.bb.product.domain.product.application.port.out;

import java.util.List;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.exception.errors.ProductNotFoundException;

public interface ProductQueryOutPort {
  List<Product> findProductByStoreId(Long storeId) throws ProductNotFoundException;
}
