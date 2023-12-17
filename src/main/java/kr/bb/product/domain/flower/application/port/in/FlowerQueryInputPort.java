package kr.bb.product.domain.flower.application.port.in;

import java.util.List;
import java.util.stream.Collectors;
import kr.bb.product.common.dto.FlowerInformation;
import kr.bb.product.domain.flower.application.port.out.FlowerQueryOutPort;
import kr.bb.product.domain.flower.application.usecase.FlowerQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlowerQueryInputPort implements FlowerQueryUseCase {
  private final FlowerQueryOutPort flowerQueryOutPort;

  @Override
  public List<FlowerInformation> getAllFlowers() {
    return flowerQueryOutPort.findAllFlowers().stream()
        .map(
            item ->
                FlowerInformation.builder()
                    .flowerId(item.getId())
                    .flowerName(item.getFlowerName())
                    .build())
        .collect(Collectors.toList());
  }
}
