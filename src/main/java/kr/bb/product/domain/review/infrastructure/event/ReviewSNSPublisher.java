package kr.bb.product.domain.review.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bb.product.common.dto.ReviewRegisterEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewSNSPublisher {
  private final SnsClient snsClient;
  private final ObjectMapper objectMapper;

  @Value("${cloud.aws.sns.review-register-event.arn}")
  private String arn;

  public void reviewRegisterEventPublish(ReviewRegisterEvent reviewRegisterEvent) {
    try {
      PublishResponse publish =
          snsClient.publish(
              PublishRequest.builder()
                  .message(objectMapper.writeValueAsString(reviewRegisterEvent))
                  .topicArn(arn)
                  .build());
      if (publish.sdkHttpResponse().isSuccessful()) {
        log.info("review register event success");
      } else log.error("review register event fail");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
