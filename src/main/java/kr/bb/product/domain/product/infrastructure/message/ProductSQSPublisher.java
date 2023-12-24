package kr.bb.product.domain.product.infrastructure.message;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bb.product.domain.product.mapper.ProductCommand.ResaleCheckRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSQSPublisher {
  private final AmazonSQS sqs;
  private final ObjectMapper objectMapper;

  @Value("${cloud.aws.sqs.product-resale-notification-check-queue.url}")
  private String productResaleNotificationCheckQueueUrl;

  public void publishProductResaleNotificationCheckQueue(String productId, String productName) {
    try {
      SendMessageRequest sendMessageRequest =
          new SendMessageRequest(
              productResaleNotificationCheckQueueUrl,
              objectMapper.writeValueAsString(
                  ResaleCheckRequest.builder()
                      .productId(productId)
                      .productName(productName)
                      .build()));
      sqs.sendMessage(sendMessageRequest);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
