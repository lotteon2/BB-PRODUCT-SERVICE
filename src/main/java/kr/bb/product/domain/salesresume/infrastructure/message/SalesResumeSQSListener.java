package kr.bb.product.domain.salesresume.infrastructure.message;

import bloomingblooms.domain.resale.ResaleNotificationData;
import bloomingblooms.domain.resale.ResaleNotificationList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.salesresume.application.port.out.SalesResumeQueryOutPort;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import kr.bb.product.domain.salesresume.entity.mapper.SalesResumeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalesResumeSQSListener {
  private final ObjectMapper objectMapper;
  private final SalesResumeQueryOutPort salesResumeQueryOutPort;

  private final SalesResumeSQSPublisher salesResumeSQSPublisher;

  @SqsListener(
      value = "${cloud.aws.sqs.product-resale-notification-check-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeProductResaleNotificationCheckQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    ProductCommand.ResaleCheckRequest resaleCheckRequest =
        objectMapper.readValue(message, ProductCommand.ResaleCheckRequest.class);

    List<SalesResume> needToSendResaleNotification =
        salesResumeQueryOutPort.findNeedToSendResaleNotification(resaleCheckRequest.getProductId());
    if (!needToSendResaleNotification.isEmpty()) {
      List<ResaleNotificationData> resaleNotificationList =
          SalesResumeMapper.INSTANCE.toResaleNotificationList(needToSendResaleNotification);
      // send sqs resale notification
      salesResumeSQSPublisher.publishProductResaleNotificationQueueUrl(
          ResaleNotificationList.builder()
              .resaleNotificationData(resaleNotificationList)
              .productId(resaleCheckRequest.getProductId())
              .message(String.format("%s이 판매 시작되었습니다.", resaleCheckRequest.getProductName()))
              .build());
      ack.acknowledge();
    }
  }
}
