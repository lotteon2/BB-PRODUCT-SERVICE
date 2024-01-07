package kr.bb.product.domain.product.infrastructure.event;

import bloomingblooms.domain.order.ProcessOrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bb.product.domain.product.application.handler.ProductQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductKafkaListener {
  private final ProductQueryHandler productQueryHandler;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "stock-decrease", groupId = "stock-decrease")
  public void stockDecreaseRequestListener(String message) throws JsonProcessingException {
    ProcessOrderDto processOrderDto = objectMapper.readValue(message, ProcessOrderDto.class);
    productQueryHandler.getFlowerStockDecrease(processOrderDto);
  }

  @KafkaListener(topics = "order-create-rollback", groupId = "order-rollback")
  public void orderRollbackRequestListener(String message) throws JsonProcessingException {
    ProcessOrderDto processOrderDto = objectMapper.readValue(message, ProcessOrderDto.class);
    productQueryHandler.getFlowerStockRollback(processOrderDto);
  }
}
