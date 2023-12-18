package kr.bb.product.domain.salesresume.infrastructure.message;

import bloomingblooms.domain.notification.NotificationData;
import bloomingblooms.domain.notification.NotificationKind;
import bloomingblooms.domain.notification.NotificationURL;
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
                  NotificationData.notifyData(
                      resaleNotifications,
                      PublishNotificationInformation.getData(
                          NotificationURL.RESALE,
                          NotificationKind.RESALE,
                          resaleNotifications.getProductName() + " 상품이 재입고 되었습니다."))));
      sqs.sendMessage(sendMessageRequest);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
