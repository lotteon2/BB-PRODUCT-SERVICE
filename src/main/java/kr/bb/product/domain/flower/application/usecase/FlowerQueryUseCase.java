package kr.bb.product.domain.flower.application.usecase;

import java.util.List;
import kr.bb.product.common.dto.FlowerInformation;

public interface FlowerQueryUseCase {
  List<FlowerInformation> getAllFlowers();
}
