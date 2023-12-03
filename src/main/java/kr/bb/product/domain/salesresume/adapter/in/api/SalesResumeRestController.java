package kr.bb.product.domain.salesresume.adapter.in.api;

import bloomingblooms.response.CommonResponse;
import kr.bb.product.domain.salesresume.application.usecase.SalesResumeCommandUseCase;
import kr.bb.product.domain.salesresume.entity.SalesResumeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SalesResumeRestController {
  private final SalesResumeCommandUseCase salesResumeCommandUseCase;

  @PostMapping("{productId}/sale-resume")
  public CommonResponse<String> salesResumeNotificationRequest(
      @PathVariable String productId,
      @RequestBody SalesResumeCommand.SalesResumeRequest request,
      @RequestHeader Long userId) {
    request.setUserId(userId);
    request.setProductId(productId);
    salesResumeCommandUseCase.save(request);
    return CommonResponse.success("상품 판매 재개 알림이 요청되었습니다.");
  }
}
