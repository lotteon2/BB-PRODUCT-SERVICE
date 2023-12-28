package kr.bb.product.domain.product.application.handler;

import bloomingblooms.domain.order.ProcessOrderDto;
import java.util.Map;
import kr.bb.product.domain.product.application.usecase.ProductQueryUseCase;
import kr.bb.product.domain.product.infrastructure.client.StoreServiceClient;
import kr.bb.product.domain.product.infrastructure.event.ProductKafkaProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductQueryHandler {
  private static final String STORE_AVERAGE_RATING_UPDATE_TOPIC = "store-average-rating-update";
  private static final String STOCK_DECREASE_ROLLBACK = "stock-decrease-rollback";
  private final ProductQueryUseCase productQueryUseCase;
  private final StoreServiceClient storeServiceClient;
  private final ProductKafkaProcessor<Map<Long, Double>> productKafkaProcessor;
  private final ProductKafkaProcessor<ProcessOrderDto> processOrderDtoProductKafkaProcessor;

  public void getStoreAverageRating() {
    Map<Long, Double> storeAverageRating = productQueryUseCase.getStoreAverageRating();
    productKafkaProcessor.send(STORE_AVERAGE_RATING_UPDATE_TOPIC, storeAverageRating);
  }

  public void getFlowerStockDecrease(ProcessOrderDto processOrderDto) {
    storeServiceClient.flowerStockDecreaseRequest(
        productQueryUseCase.getFlowerAmountGroupByStoreId(processOrderDto));
  }

  public void getFlowerStockRollback(ProcessOrderDto processOrderDto) {
//    storeServiceClient.flowerStockIncreaseRequest(
//        productQueryUseCase.getFlowerAmountGroupByStoreId(processOrderDto));
    processOrderDtoProductKafkaProcessor.send(STOCK_DECREASE_ROLLBACK, processOrderDto);
  }
}
