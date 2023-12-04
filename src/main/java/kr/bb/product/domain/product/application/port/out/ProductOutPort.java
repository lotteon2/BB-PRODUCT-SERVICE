package kr.bb.product.domain.product.application.port.out;

import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductSaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductOutPort {
  void updateProductSaleStatus(Product product, ProductSaleStatus productSaleStatus);

  void updateProductSaleStatus(Product product);

  void createProduct(Product productRequestToEntity);

  Page<Product> findByCategory(Long categoryId, Pageable pageable);

  Page<Product> findProductsByTagId(Long tagId, Pageable pageable);
}
