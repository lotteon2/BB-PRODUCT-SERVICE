package kr.bb.product.domain.salesresume.application.usecase;

import java.util.List;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import kr.bb.product.domain.salesresume.entity.SalesResumeCommand;

public interface SalesResumeCommandUseCase {
  void save(SalesResumeCommand.SalesResumeRequest request, Long userId);

  List<SalesResume> selectAndUpdate(String productId);
}
