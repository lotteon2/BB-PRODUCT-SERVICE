package kr.bb.product.domain.product.application.port.out;

import java.util.List;
import kr.bb.product.domain.product.entity.Product;

public interface ProductQueryOutPort {
  List<Product> findProductByStoreId(Long storeId);

  List<Product> findStoreProducts(Long soreId);
}
