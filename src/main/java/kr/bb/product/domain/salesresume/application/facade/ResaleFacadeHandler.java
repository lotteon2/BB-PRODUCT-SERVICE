package kr.bb.product.domain.salesresume.application.facade;

import bloomingblooms.domain.resale.ResaleNotificationData;
import bloomingblooms.domain.resale.ResaleNotificationList;
import java.util.List;
import kr.bb.product.domain.product.entity.ProductCommand.ResaleCheckRequest;
import kr.bb.product.domain.salesresume.application.usecase.SalesResumeCommandUseCase;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import kr.bb.product.domain.salesresume.entity.mapper.SalesResumeMapper;
import kr.bb.product.domain.salesresume.infrastructure.message.SalesResumeSQSPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResaleFacadeHandler {
  private final SalesResumeCommandUseCase salesResumeCommandUseCase;
  private final SalesResumeSQSPublisher salesResumeSQSPublisher;

  public void productResaleNotificationHandler(ResaleCheckRequest resaleCheckRequest) {
    List<SalesResume> salesResumes =
        salesResumeCommandUseCase.selectAndUpdate(resaleCheckRequest.getProductId());

    if (salesResumes != null) {
      List<ResaleNotificationData> resaleNotificationList =
          SalesResumeMapper.INSTANCE.toResaleNotificationList(salesResumes);
      salesResumeSQSPublisher.publishProductResaleNotificationQueueUrl(
          ResaleNotificationList.builder()
              .resaleNotificationData(resaleNotificationList)
              .productId(resaleCheckRequest.getProductId())
              .message(String.format("%s이 판매 시작되었습니다.", resaleCheckRequest.getProductName()))
              .build());
    }
  }
}
