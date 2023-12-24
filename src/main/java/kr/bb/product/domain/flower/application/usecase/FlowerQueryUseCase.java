package kr.bb.product.domain.flower.application.usecase;

import bloomingblooms.domain.flower.FlowerDto;
import java.util.List;

public interface FlowerQueryUseCase {
  List<FlowerDto> getAllFlowers();
}
