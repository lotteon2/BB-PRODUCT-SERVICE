package kr.bb.product.domain.salesresume.adapter.out.jpa;

import kr.bb.product.domain.salesresume.application.port.out.SalesResumeCommandOutPort;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SalesResumeCommandRepository implements SalesResumeCommandOutPort {
  private final SalesResumeJpaRepository salesResumeJpaRepository;

  @Override
  public void save(SalesResume salesResume) {
    salesResumeJpaRepository.save(salesResume);
  }
}