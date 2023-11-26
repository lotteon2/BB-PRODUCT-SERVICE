package kr.bb.product.domain.product.application.port.out;

import java.util.List;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import org.jetbrains.annotations.TestOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductOutPort {
  void updateProductSaleStatus(Product product, ProductSaleStatus productSaleStatus);

  void updateProductSaleStatus(Product product);

  Product findByProductId(String productId);

  void createProduct(Product productRequestToEntity);

  Page<Product> findByCategory(Long categoryId, Pageable pageable);
@TestOnly
  void deleteAll();
@TestOnly
  Product save(Product product);
@TestOnly
  List<Product> findAll();
}
