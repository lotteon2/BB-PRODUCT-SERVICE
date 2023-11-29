package kr.bb.product.domain.product.application.port.out;

import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;

public interface ProductCommandOutPort {
  void createProduct(Product product);

  void updateSubscriptionProduct(ProductCommand.UpdateSubscriptionProduct product);
}
