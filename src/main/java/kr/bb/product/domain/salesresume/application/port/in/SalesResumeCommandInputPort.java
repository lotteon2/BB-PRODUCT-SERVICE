package kr.bb.product.domain.salesresume.application.port.in;

import com.amazonaws.services.sns.model.AmazonSNSException;
import java.util.List;
import java.util.stream.Collectors;
import kr.bb.product.domain.salesresume.application.port.out.SalesResumeCommandOutPort;
import kr.bb.product.domain.salesresume.application.usecase.SalesResumeCommandUseCase;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import kr.bb.product.domain.salesresume.entity.SalesResumeCommand.SalesResumeRequest;
import kr.bb.product.domain.salesresume.infrastructure.message.SalesResumeSNSSubscribe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalesResumeCommandInputPort implements SalesResumeCommandUseCase {
  private final SalesResumeCommandOutPort salesResumeCommandOutPort;
  private final SalesResumeSNSSubscribe salesResumeSNSSubscribe;

  @Transactional
  @Override
  public void save(SalesResumeRequest request) {
    request.setPhoneNumber(String.format("+82%s", request.getPhoneNumber()));
    try {
      salesResumeSNSSubscribe.subscribe(request.getPhoneNumber());
    } catch (AmazonSNSException e) {
      throw new AmazonSNSException("재입고 알림 등록 실패");
    }
    salesResumeCommandOutPort.save(SalesResumeRequest.toEntity(request));
  }

  @Transactional
  @Override
  public List<SalesResume> selectAndUpdate(String productId) {
    return salesResumeCommandOutPort.selectAndUpdate(productId).stream()
        .map(SalesResume::setNotified)
        .collect(Collectors.toList());
  }
}
