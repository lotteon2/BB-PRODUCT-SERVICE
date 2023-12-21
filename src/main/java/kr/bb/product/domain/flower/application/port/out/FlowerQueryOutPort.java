package kr.bb.product.domain.flower.application.port.out;

import java.util.List;
import kr.bb.product.domain.flower.entity.Flower;

public interface FlowerQueryOutPort {
  List<Flower> findProductDetailFlower(List<Long> flowers);

  List<Flower> findAllFlowers();

  Flower findLanguageOfFlowerByFlowerId(Long flowerId);
}
