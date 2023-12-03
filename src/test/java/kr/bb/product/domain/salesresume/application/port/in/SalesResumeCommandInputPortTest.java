package kr.bb.product.domain.salesresume.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.bb.product.domain.salesresume.adapter.out.jpa.SalesResumeJpaRepository;
import kr.bb.product.domain.salesresume.application.usecase.SalesResumeCommandUseCase;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import kr.bb.product.domain.salesresume.entity.SalesResumeCommand.SalesResumeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SalesResumeCommandInputPortTest {
  @Autowired SalesResumeCommandUseCase salesResumeCommandUseCase;
  @Autowired SalesResumeJpaRepository salesResumeJpaRepository;

  @Test
  @DisplayName("상품 판매 재개 알림 요청")
  void save() {
    SalesResumeRequest username =
        SalesResumeRequest.builder()
            .phoneNumber("01011111111")
            .productId("123")
            .userId(1L)
            .userName("username")
            .build();
    salesResumeCommandUseCase.save(username);
    List<SalesResume> all = salesResumeJpaRepository.findAll();
    assertThat(all.size()).isEqualTo(1);
  }
}
