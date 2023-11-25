package kr.bb.product.domain.product.application.port.in;

import java.util.List;
import kr.bb.product.domain.product.application.ProductFindUseCase;
import kr.bb.product.domain.product.application.port.out.ProductOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand.ProductsByCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductFindInputPort implements ProductFindUseCase {
  private final ProductOutPort productOutPort;

  @Override
  public ProductsByCategory getProductsByCategory(Long userId, Long categoryId, Pageable pageable) {
    Slice<Product> byCategory = productOutPort.findByCategory(categoryId, pageable);
    // isliked를 알아야 함 - 찜 feign
    return null;
  }
}
