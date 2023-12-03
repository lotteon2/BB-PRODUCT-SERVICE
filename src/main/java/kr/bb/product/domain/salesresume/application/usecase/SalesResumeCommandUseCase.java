package kr.bb.product.domain.salesresume.application.usecase;

import kr.bb.product.domain.salesresume.entity.SalesResumeCommand;

public interface SalesResumeCommandUseCase {
  void save(SalesResumeCommand.SalesResumeRequest request);
}
