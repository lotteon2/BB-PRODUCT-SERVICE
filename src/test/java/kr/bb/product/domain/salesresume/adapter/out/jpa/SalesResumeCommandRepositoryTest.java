package kr.bb.product.domain.salesresume.adapter.out.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.bb.product.domain.salesresume.application.port.out.SalesResumeCommandOutPort;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SalesResumeCommandRepositoryTest {
  @Autowired SalesResumeJpaRepository salesResumeJpaRepository;
  @Autowired private SalesResumeCommandOutPort salesResumeCommandOutPort;

  @Test
  @DisplayName("재입고 신청 알림 저장")
  void save() {
    SalesResume build = SalesResume.builder().userId(1L).productId("123").isNotified(true).build();
    salesResumeCommandOutPort.save(build);
    List<SalesResume> all = salesResumeJpaRepository.findAll();
    assertThat(all.size()).isEqualTo(1);
  }
}
