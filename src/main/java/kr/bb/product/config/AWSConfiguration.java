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
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AWSConfiguration {
  private final String region = "ap-northeast-2";

  @Value("${aws.credentials.ACCESS_KEY_ID}")
  private String accessKeyId;

  @Value("${aws.credentials.SECRET_ACCESS_KEY}")
  private String secretAccessKey;

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

  @Primary
  @Bean
  public SnsClient snsClient() {
    return SnsClient.builder()
        .region(Region.AP_NORTHEAST_1)
        .credentialsProvider(ProfileCredentialsProvider.create())
        .build();
  }
}
