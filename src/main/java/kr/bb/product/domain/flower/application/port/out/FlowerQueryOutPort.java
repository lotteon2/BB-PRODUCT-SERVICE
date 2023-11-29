package kr.bb.product.domain.flower.application.port.out;

import kr.bb.product.domain.flower.entity.Flower;

public interface FlowerQueryOutPort {
  Flower flowerNameById(Long flowerId);
}
