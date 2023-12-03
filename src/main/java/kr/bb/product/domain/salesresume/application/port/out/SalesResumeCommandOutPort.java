package kr.bb.product.domain.salesresume.application.port.out;

import kr.bb.product.domain.salesresume.entity.SalesResume;

public interface SalesResumeCommandOutPort {
  void save(SalesResume salesResume);
}
