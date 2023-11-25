package kr.bb.product.domain.product.application.port.out;

import java.util.List;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import org.springframework.data.domain.Pageable;

public interface ProductOutPort {
  void updateProductSaleStatus(Product product, ProductSaleStatus productSaleStatus);

  void updateProductSaleStatus(Product product);

  Product findByProductId(String productId);

  void createProduct(Product productRequestToEntity);

  List<Product> findByCategory(Long categoryId, Pageable pageable);
}
