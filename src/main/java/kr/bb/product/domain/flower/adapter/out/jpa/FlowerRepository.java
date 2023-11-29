package kr.bb.product.domain.flower.adapter.out.jpa;

import bloomingblooms.errors.EntityNotFoundException;
import kr.bb.product.domain.flower.application.port.out.FlowerQueryOutPort;
import kr.bb.product.domain.flower.entity.Flower;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlowerRepository implements FlowerQueryOutPort {
  private final FlowerJpaRepository flowerJpaRepository;

  @Override
  public Flower flowerNameById(Long flowerId) {
    return flowerJpaRepository.findById(flowerId).orElseThrow(EntityNotFoundException::new);
  }
}
