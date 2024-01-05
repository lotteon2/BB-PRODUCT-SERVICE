package kr.bb.product.domain.salesresume.application.port.out;

import java.util.List;
import kr.bb.product.domain.salesresume.entity.SalesResume;

public interface SalesResumeCommandOutPort {
  void save(SalesResume salesResume, Long userId);

  List<SalesResume> selectAndUpdate(String productId);
}
