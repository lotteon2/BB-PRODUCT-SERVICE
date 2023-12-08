package kr.bb.product.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockingTestConfiguration {
  @Bean(initMethod = "start", destroyMethod = "stop")
  public WireMockServer mockCacheApi() {
    return new WireMockServer(9561);
  }
}
