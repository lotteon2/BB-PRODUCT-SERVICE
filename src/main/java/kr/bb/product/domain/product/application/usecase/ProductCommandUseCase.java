package kr.bb.product.domain.product.application.usecase;

import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.entity.ProductCommand.ProductList;
import org.springframework.data.domain.Pageable;

public interface ProductCommandUseCase {
  ProductList getProductsByCategory(Long userId, Long categoryId, Pageable pageable);

  ProductList getProductsByCategory(Long categoryId, Pageable pageable);

  ProductList getProductsByTag(Long tagId, Pageable pageable);

  ProductList getProductsByTag(Long userId, Long tagId, Pageable pageable);

  ProductCommand.ProductDetail getProductDetail(Long userId, String productId);

  ProductCommand.ProductDetail getProductDetail(String productId);
}
