package kr.bb.product.config;

import autovalue.shaded.org.jetbrains.annotations.NotNull;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AWSConfiguration {
  @Value("${aws.credentials.ACCESS_KEY_ID}")
  private String accessKeyId;

  @Value("${aws.credentials.SECRET_ACCESS_KEY}")
  private String secretAccessKey;

  @Value("${aws.region.static}")
  private String region;

  @NotNull
  private BasicAWSCredentials getBasicAWSCredentials() {
    return new BasicAWSCredentials(accessKeyId, secretAccessKey);
  }

  @Primary
  @Bean
  public AmazonSQSAsync amazonSQSAsync() {
    return AmazonSQSAsyncClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(getBasicAWSCredentials()))
        .build();
  }
}
