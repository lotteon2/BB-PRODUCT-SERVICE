package kr.bb.product.domain.flower.adapter.out.jpa;

import java.util.List;
import kr.bb.product.domain.flower.application.port.out.FlowerQueryOutPort;
import kr.bb.product.domain.flower.entity.Flower;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FlowerQueryRepository implements FlowerQueryOutPort {
  private final FlowerJpaRepository flowerJpaRepository;

  @Override
  public List<Flower> findProductDetailFlower(List<Long> flowers) {
    return flowerJpaRepository.findAllById(flowers);
  }

  @Override
  public List<Flower> findAllFlowers() {
    return flowerJpaRepository.findAll();
  }
}
