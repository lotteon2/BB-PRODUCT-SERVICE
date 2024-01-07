package kr.bb.product.domain.product.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductKafkaProcessor<T> {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public void send(String topicName, T data) {
    try {
      String message = objectMapper.writeValueAsString(data);
      kafkaTemplate.send(topicName, message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
