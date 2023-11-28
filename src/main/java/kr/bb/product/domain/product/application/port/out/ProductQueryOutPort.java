package kr.bb.product.domain.product.application.port.out;

import kr.bb.product.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryOutPort {
      Page<Product> findProductByStoreId(Long storeId, Pageable pageable);
}
