package kr.bb.product.domain.salesresume.application.port.in;

import kr.bb.product.domain.salesresume.application.port.out.SalesResumeCommandOutPort;
import kr.bb.product.domain.salesresume.application.usecase.SalesResumeCommandUseCase;
import kr.bb.product.domain.salesresume.entity.SalesResumeCommand.SalesResumeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalesResumeCommandInputPort implements SalesResumeCommandUseCase {
  private final SalesResumeCommandOutPort salesResumeCommandOutPort;

  @Override
  public void save(SalesResumeRequest request) {
    salesResumeCommandOutPort.save(SalesResumeRequest.toEntity(request));
  }
}
