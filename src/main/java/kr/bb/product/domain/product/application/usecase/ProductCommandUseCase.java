package kr.bb.product.domain.product.application.usecase;

import kr.bb.product.domain.product.entity.ProductCommand;

public interface ProductCommandUseCase {
  void updateProductSaleStatus(String productId, ProductCommand.ProductUpdate productRequestData);

  void createProduct(ProductCommand.ProductRegister productRequestData);

  void createSubscriptionProduct(Long storeId, ProductCommand.SubscriptionProduct product);

  void updateSubscriptionProduct(String productId, ProductCommand.UpdateSubscriptionProduct product);
}
