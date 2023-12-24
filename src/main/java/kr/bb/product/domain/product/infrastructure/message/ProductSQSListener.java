package kr.bb.product.domain.product.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.bb.product.common.dto.ReviewRegisterEvent;
import kr.bb.product.domain.product.application.handler.ProductCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSQSListener {
  private final ObjectMapper objectMapper;
  private final ProductCommandHandler productHandler;

  @SqsListener(
      value = "${cloud.aws.sqs.product-review-data-update-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeProductResaleNotificationCheckQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    String messageFromSNS = getMessageFromSNS(message);

    ReviewRegisterEvent reviewRegisterEvent =
        objectMapper.readValue(messageFromSNS, ReviewRegisterEvent.class);

    productHandler.updateReviewData(reviewRegisterEvent);
    ack.acknowledge();
  }

  private String getMessageFromSNS(String message) throws JsonProcessingException {
    JsonNode jsonNode = objectMapper.readTree(message);
    return jsonNode.get("Message").asText();
  }
}
