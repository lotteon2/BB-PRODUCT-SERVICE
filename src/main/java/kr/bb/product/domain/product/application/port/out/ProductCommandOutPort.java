package kr.bb.product.domain.product.application.port.out;

import kr.bb.product.common.dto.ReviewRegisterEvent;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.mapper.ProductCommand;

public interface ProductCommandOutPort {
  void createProduct(Product product);

  void updateSubscriptionProduct(ProductCommand.UpdateSubscriptionProduct product);

    void updateProductReviewData(ReviewRegisterEvent reviewRegisterEvent);
}
