package kr.bb.product.domain.salesresume.application.port.in;

import java.util.List;
import java.util.stream.Collectors;
import kr.bb.product.domain.salesresume.application.port.out.SalesResumeCommandOutPort;
import kr.bb.product.domain.salesresume.application.usecase.SalesResumeCommandUseCase;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import kr.bb.product.domain.salesresume.entity.SalesResumeCommand.SalesResumeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalesResumeCommandInputPort implements SalesResumeCommandUseCase {
  private final SalesResumeCommandOutPort salesResumeCommandOutPort;

  @Transactional
  @Override
  public void save(SalesResumeRequest request, Long userId) {
    salesResumeCommandOutPort.save(SalesResumeRequest.toEntity(request), userId);
  }

  @Transactional
  @Override
  public List<SalesResume> selectAndUpdate(String productId) {
    return salesResumeCommandOutPort.selectAndUpdate(productId).stream()
        .map(SalesResume::setNotified)
        .collect(Collectors.toList());
  }
}
