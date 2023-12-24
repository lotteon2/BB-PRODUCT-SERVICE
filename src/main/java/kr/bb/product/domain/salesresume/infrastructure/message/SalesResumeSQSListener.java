package kr.bb.product.domain.salesresume.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.salesresume.application.handler.ResaleFacadeHandler;
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
public class SalesResumeSQSListener {
  private final ObjectMapper objectMapper;
  private final ResaleFacadeHandler resaleFacadeHandler;

  @SqsListener(
      value = "${cloud.aws.sqs.product-resale-notification-check-queue.name}",
      deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  public void consumeProductResaleNotificationCheckQueue(
      @Payload String message, @Headers Map<String, String> headers, Acknowledgment ack)
      throws JsonProcessingException {
    ProductCommand.ResaleCheckRequest resaleCheckRequest =
        objectMapper.readValue(message, ProductCommand.ResaleCheckRequest.class);

    resaleFacadeHandler.productResaleNotificationHandler(resaleCheckRequest);

    ack.acknowledge();
  }
}
