package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import bloomingblooms.domain.store.StoreName;
import bloomingblooms.response.CommonResponse;
import kr.bb.product.domain.product.infrastructure.client.StoreServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class productDetailMockito {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @MockBean RedissonAutoConfiguration redissonAutoConfiguration;
  private WebTestClient webTestClient;
  @Mock private StoreServiceClient storeServiceClient;

  @BeforeEach
  void beforeEach() {
    this.webTestClient = WebTestClient.bindToController(this.storeServiceClient).build();
  }

  @Test
  @DisplayName("상품 상세 조회 service")
  void productDetail() {
    Long storeId = 1L;

    CommonResponse<StoreName> expectData =
        CommonResponse.<StoreName>builder()
            .data(StoreName.builder().storeName("storename1").build())
            .build();
    Mockito.when(storeServiceClient.getStoreNameOfProductDetail(storeId)).thenReturn(expectData);
    StoreName data = storeServiceClient.getStoreNameOfProductDetail(storeId).getData();
    assertThat(data.getStoreName()).isEqualTo(expectData.getData().getStoreName());
  }
}
