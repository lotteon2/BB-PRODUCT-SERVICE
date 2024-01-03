package kr.bb.product.domain.flower.adapter.in.client;

import bloomingblooms.domain.flower.FlowerDto;
import bloomingblooms.response.CommonResponse;
import java.util.List;
import kr.bb.product.domain.flower.application.usecase.FlowerQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FlowerFeignClientController {
  private final FlowerQueryUseCase flowerQueryUseCase;

  @GetMapping("/client/flowers")
  public CommonResponse<List<FlowerDto>> getAllFlowerInformation() {
    return CommonResponse.success(flowerQueryUseCase.getAllFlowers());
  }
}
