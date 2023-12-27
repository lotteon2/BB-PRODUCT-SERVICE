package kr.bb.product.config;

import autovalue.shaded.org.jetbrains.annotations.NotNull;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AWSConfiguration {
  @Value("${cloud.aws.region.static}")
  private String region;

  @Value("${cloud.aws.credentials.ACCESS_KEY_ID}")
  private String accessKeyId;

  @Value("${cloud.aws.credentials.SECRET_ACCESS_KEY}")
  private String secretAccessKey;


  @NotNull
  private BasicAWSCredentials getBasicAWSCredentials() {
    return new BasicAWSCredentials(accessKeyId, secretAccessKey);
  }

  // java2
  public AwsCredentialsProvider getAwsCredentials() {
    AwsBasicCredentials awsBasicCredentials =
        AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    return () -> awsBasicCredentials;
  }

  @Primary
  @Bean
  public AmazonSQSAsync amazonSQSAsync() {
    return AmazonSQSAsyncClientBuilder.standard()
        .withRegion(Regions.AP_NORTHEAST_1)
        .withCredentials(new AWSStaticCredentialsProvider(getBasicAWSCredentials()))
        .build();
  }

  @Primary
  @Bean
  public SnsClient snsClient() {
    return SnsClient.builder()
        .credentialsProvider(getAwsCredentials())
        .region(Region.AP_NORTHEAST_1)
        .build();
  }
}
