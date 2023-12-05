package kr.bb.product.domain.salesresume.adapter.out.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.bb.product.domain.salesresume.application.port.out.SalesResumeQueryOutPort;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SalesResumeQueryRepositoryTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired SalesResumeJpaRepository salesResumeJpaRepository;
  @Autowired SalesResumeQueryOutPort salesResumeQueryOutPort;

  @Test
  @DisplayName("상품 판매 재개 알림 요청 확인 ")
  void findNeedToSendResaleNotification() {
    for (int i = 0; i < 10; i++) {
      SalesResume build =
          SalesResume.builder().userId(1L).productId("123").isNotified(true).build();
      salesResumeJpaRepository.save(build);
    }
    List<SalesResume> needToSendResaleNotification =
        salesResumeQueryOutPort.findNeedToSendResaleNotification("123");
    assertThat(needToSendResaleNotification.size()).isEqualTo(10);
  }
}
