package kr.bb.product.domain.salesresume.infrastructure.message;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalesResumeSQSPublisher {
      private final AmazonSQS sqs;
  private final ObjectMapper objectMapper;

  @Value("${aws.sqs.product-resale-notification-queue.url}")
  private String productResaleNotificationQueueUrl;
  public void publishProductResaleNotificationQueueUrl(){

  }
}
