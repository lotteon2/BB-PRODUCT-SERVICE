package kr.bb.product.domain.product.application.usecase;

import kr.bb.product.domain.product.entity.ProductCommand;

public interface ProductCommandUseCased {
  void updateProductSaleStatus(String productId, ProductCommand.ProductUpdate productRequestData);

  void createProduct(ProductCommand.ProductRegister productRequestData);

  void createSubscriptionProduct(ProductCommand.SubscriptionProduct product);
}
