package kr.bb.product.domain.salesresume.application.port.out;

import java.util.List;
import kr.bb.product.domain.salesresume.entity.SalesResume;

public interface SalesResumeQueryOutPort {
  List<SalesResume> findNeedToSendResaleNotification(String productId);
}
