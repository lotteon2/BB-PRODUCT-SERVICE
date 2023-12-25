package kr.bb.product.domain.product.application.handler;

import kr.bb.product.common.dto.NewOrderEvent;
import kr.bb.product.common.dto.ReviewRegisterEvent;
import kr.bb.product.domain.product.application.usecase.ProductCommandUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCommandHandler {
  private final ProductCommandUseCase productCommandUseCase;

  public void updateReviewData(ReviewRegisterEvent reviewRegisterEvent) {
    productCommandUseCase.updateProductReviewData(reviewRegisterEvent);
  }

  public void saleCountUpdate(NewOrderEvent newOrderEvent) {
    productCommandUseCase.saleCountUpdate(
        NewOrderEvent.getProductDataForUpdateSaleCount(newOrderEvent));
  }
}
