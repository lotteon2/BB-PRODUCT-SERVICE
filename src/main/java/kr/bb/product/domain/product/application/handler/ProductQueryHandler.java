package kr.bb.product.domain.product.application.handler;

import bloomingblooms.domain.notification.order.OrderType;
import bloomingblooms.domain.order.ProcessOrderDto;
import bloomingblooms.response.CommonResponse.Result;
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
  private static final String ORDER_CREATE = "order-create";
  private final ProductQueryUseCase productQueryUseCase;
  private final StoreServiceClient storeServiceClient;
  private final ProductKafkaProcessor<Map<Long, Double>> mapProductKafkaProcessor;
  private final ProductKafkaProcessor<ProcessOrderDto> processOrderDtoProductKafkaProcessor;

  public void getStoreAverageRating() {
    mapProductKafkaProcessor.send(
        STORE_AVERAGE_RATING_UPDATE_TOPIC, productQueryUseCase.getStoreAverageRating());
  }

  public void getFlowerStockDecrease(ProcessOrderDto processOrderDto) {
    Result result = Result.SUCCESS;
    if (!processOrderDto.getOrderType().equals(OrderType.SUBSCRIBE.toString()))
      result =
          storeServiceClient
              .flowerStockDecreaseRequest(
                  productQueryUseCase.getFlowerAmountGroupByStoreId(processOrderDto))
              .getResult();
    if (result.equals(Result.SUCCESS))
      // order create request kafka
      processOrderDtoProductKafkaProcessor.send(ORDER_CREATE, processOrderDto);
  }

  public void getFlowerStockRollback(ProcessOrderDto processOrderDto) {
    Result result = Result.SUCCESS;
    if (!processOrderDto.getOrderType().equals(OrderType.SUBSCRIBE.toString()))
      result =
          storeServiceClient
              .flowerStockIncreaseRequest(
                  productQueryUseCase.getFlowerAmountGroupByStoreId(processOrderDto))
              .getResult();

    // stock decrease rollback kafka
    processOrderDtoProductKafkaProcessor.send(STOCK_DECREASE_ROLLBACK, processOrderDto);
  }
}
