package kr.bb.product.domain.salesresume.infrastructure.message;

import bloomingblooms.domain.resale.ResaleNotificationList;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalesResumeSQSPublisher {
  private final AmazonSQS sqs;
  private final ObjectMapper objectMapper;

  @Value("${cloud.aws.sqs.product-resale-notification-queue.url}")
  private String productResaleNotificationQueueUrl;

  public void publishProductResaleNotificationQueueUrl(ResaleNotificationList resaleNotifications) {
    // TODO: publish v2 고려해보기
    try {
      SendMessageRequest sendMessageRequest =
          new SendMessageRequest(
              productResaleNotificationQueueUrl,
              objectMapper.writeValueAsString(
                  ResaleNotificationList.builder()
                      .resaleNotificationData(resaleNotifications.getResaleNotificationData())
                      .message(resaleNotifications.getMessage())
                      .build()));
      sqs.sendMessage(sendMessageRequest);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
