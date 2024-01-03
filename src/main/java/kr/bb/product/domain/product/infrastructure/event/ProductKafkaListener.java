package kr.bb.product.domain.product.infrastructure.event;

import bloomingblooms.domain.order.ProcessOrderDto;
import kr.bb.product.domain.product.application.handler.ProductQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductKafkaListener {
  private final ProductQueryHandler productQueryHandler;

  @KafkaListener(topics = "stock-decrease", groupId = "stock-decrease")
  public void stockDecreaseRequestListener(ProcessOrderDto processOrderDto) {
    productQueryHandler.getFlowerStockDecrease(processOrderDto);
  }

  @KafkaListener(topics = "order-create-rollback", groupId = "order-rollback")
  public void orderRollbackRequestListener(ProcessOrderDto processOrderDto) {
    productQueryHandler.getFlowerStockRollback(processOrderDto);
  }
}
