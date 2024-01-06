package kr.bb.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

@ActiveProfiles("test")
@Configuration
public class RedisTestConfig {
  static final String REDIS_IMAGE = "redis:6-alpine";
  static final GenericContainer REDIS_CONTAINER;

  static {
    REDIS_CONTAINER =
        new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(6379)
            .withCommand("--requirepass", "123456")
            .withReuse(true);
    REDIS_CONTAINER.start();
  }

  @DynamicPropertySource
  public static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
    registry.add("spring.redis.port", () -> "" + REDIS_CONTAINER.getMappedPort(6379));
    registry.add("spring.redis.password", () -> "123456");
  }
}
