package kr.bb.product.domain.product.application;

import kr.bb.product.domain.product.entity.ProductCommand;
import org.springframework.data.domain.Pageable;

public interface ProductFindUseCase {
  ProductCommand.ProductsByCategory getProductsByCategory(Long userId, Long categoryId, Pageable pageable);
}
