package kr.bb.product.domain.salesresume.adapter.out.jpa;

import java.util.List;
import kr.bb.product.domain.salesresume.application.port.out.SalesResumeQueryOutPort;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SalesResumeQueryRepository implements SalesResumeQueryOutPort {
  private final SalesResumeJpaRepository salesResumeJpaRepository;

  @Override
  public List<SalesResume> findNeedToSendResaleNotification(String productId) {
    return salesResumeJpaRepository.findSaleResumeByProductId(productId);
  }
}
