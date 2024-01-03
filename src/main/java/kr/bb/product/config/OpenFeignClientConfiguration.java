package kr.bb.product.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignClientConfiguration {
  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL; // log레벨 설정
  }

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      requestTemplate.header("Content-Type", "application/json");
      requestTemplate.header("Accept", "application/json");
    };
  }
}
