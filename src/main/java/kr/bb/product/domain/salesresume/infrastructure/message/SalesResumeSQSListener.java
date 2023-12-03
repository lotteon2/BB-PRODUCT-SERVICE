package kr.bb.product.domain.salesresume.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
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

  @SqsListener(
      value = "${aws.sqs.product-resale-notification-check-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeProductResaleNotificationCheckQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {}
}
