package kr.bb.product.domain.product.infrastructure.message;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSQSPublisher {
  private final AmazonSQS sqs;
  private final ObjectMapper objectMapper;

  @Value("${aws.sqs.product-resale-notification-check-queue.url}")
  private String productResaleNotificationCheckQueueUrl;

  public void publishProductResaleNotificationCheckQueue(String productId) {
    try {
      SendMessageRequest sendMessageRequest =
          new SendMessageRequest(
                  productResaleNotificationCheckQueueUrl, objectMapper.writeValueAsString(productId));
      sqs.sendMessage(sendMessageRequest);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
