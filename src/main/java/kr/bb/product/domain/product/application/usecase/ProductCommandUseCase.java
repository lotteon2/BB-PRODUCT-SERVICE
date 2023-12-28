package kr.bb.product.domain.product.application.usecase;

import bloomingblooms.domain.order.NewOrderEvent.ProductCount;
import bloomingblooms.domain.review.ReviewRegisterEvent;
import java.util.List;
import kr.bb.product.domain.product.mapper.ProductCommand;

public interface ProductCommandUseCase {
  void updateProductSaleStatus(String productId, ProductCommand.ProductUpdate productRequestData);

  void createProduct(ProductCommand.ProductRegister productRequestData);

  void createSubscriptionProduct(Long storeId, ProductCommand.SubscriptionProduct product);

  void updateSubscriptionProduct(
      String productId, ProductCommand.UpdateSubscriptionProduct product);

  void updateProductReviewData(ReviewRegisterEvent reviewRegisterEvent);

  void saleCountUpdate(List<ProductCount> newOrderEvent);
}
