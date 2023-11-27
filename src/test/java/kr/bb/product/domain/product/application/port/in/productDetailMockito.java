package kr.bb.product.domain.product.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import bloomingblooms.response.CommonResponse;
import kr.bb.product.domain.product.entity.ProductCommand.StoreName;
import kr.bb.product.domain.product.infrastructure.client.StoreServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class productDetailMockito {
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
