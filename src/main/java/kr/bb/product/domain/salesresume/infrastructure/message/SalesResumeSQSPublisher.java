package kr.bb.product.domain.salesresume.infrastructure.message;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.NotificationKind;
import bloomingblooms.domain.notification.PublishNotificationInformation;
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
    try {
      SendMessageRequest sendMessageRequest =
          new SendMessageRequest(
              productResaleNotificationQueueUrl,
              objectMapper.writeValueAsString(
                  NotificationData.builder()
                      .whoToNotify(resaleNotifications)
                      .publishInformation(
                          PublishNotificationInformation.builder()
                              .message(resaleNotifications.getMessage())
                              .notificationUrl("/products/" + resaleNotifications.getProductId())
                              .notificationKind(NotificationKind.RESTORE)
                              .build())
                      .build()));
      sqs.sendMessage(sendMessageRequest);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
