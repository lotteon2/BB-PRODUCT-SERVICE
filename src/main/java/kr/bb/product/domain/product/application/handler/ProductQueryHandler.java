package kr.bb.product.domain.product.application.handler;

import java.util.Map;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import kr.bb.product.domain.product.infrastructure.event.ProductKafkaProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductQueryHandler {
  private static final String STORE_AVERAGE_RATING_UPDATE_TOPIC = "store-average-rating-update";
  private final ProductQueryUseCase productQueryUseCase;
  private final ProductKafkaProcessor<Map<Long, Double>> productKafkaProcessor;

  public void getStoreAverageRating() {
    Map<Long, Double> storeAverageRating = productQueryUseCase.getStoreAverageRating();
    productKafkaProcessor.send(STORE_AVERAGE_RATING_UPDATE_TOPIC, storeAverageRating);
  }
}
