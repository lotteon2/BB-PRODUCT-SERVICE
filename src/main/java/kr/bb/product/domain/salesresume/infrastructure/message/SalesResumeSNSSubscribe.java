package kr.bb.product.domain.salesresume.infrastructure.message;

import kr.bb.product.config.AWSConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@Service
@RequiredArgsConstructor
public class SalesResumeSNSSubscribe {
  private final AWSConfiguration awsConfiguration;

  @Value("${cloud.aws.sns.arn}")
  private String arn;

  public void subscribe(String phoneNumber) {
    SnsClient snsClient = awsConfiguration.snsClient();
    SubscribeRequest subscribeRequest =
        SubscribeRequest.builder()
            .endpoint(phoneNumber)
            .topicArn(arn)
            .returnSubscriptionArn(true)
            .protocol("sms")
            .build();
    snsClient.subscribe(subscribeRequest);
  }
}
