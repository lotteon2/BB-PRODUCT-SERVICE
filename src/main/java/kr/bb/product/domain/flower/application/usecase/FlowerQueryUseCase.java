package kr.bb.product.domain.flower.application.usecase;

import bloomingblooms.domain.flower.FlowerInformation;
import java.util.List;

public interface FlowerQueryUseCase {
  List<FlowerInformation> getAllFlowers();
}
