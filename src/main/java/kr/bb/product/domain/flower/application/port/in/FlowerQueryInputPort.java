package kr.bb.product.domain.flower.application.port.in;

import bloomingblooms.domain.flower.FlowerDto;
import java.util.List;
import java.util.stream.Collectors;
import kr.bb.product.domain.flower.application.port.out.FlowerQueryOutPort;
import kr.bb.product.domain.flower.application.usecase.FlowerQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlowerQueryInputPort implements FlowerQueryUseCase {
  private final FlowerQueryOutPort flowerQueryOutPort;

  @Override
  public List<FlowerDto> getAllFlowers() {
    return flowerQueryOutPort.findAllFlowers().stream()
        .map(
            item ->
                FlowerDto.builder().flowerId(item.getId()).flowerName(item.getFlowerName()).build())
        .collect(Collectors.toList());
  }
}
