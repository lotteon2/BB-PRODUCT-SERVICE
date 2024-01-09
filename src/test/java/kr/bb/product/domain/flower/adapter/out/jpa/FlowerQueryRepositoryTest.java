package kr.bb.product.domain.flower.adapter.out.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.bb.product.domain.flower.entity.Flower;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataJpaTest
class FlowerQueryRepositoryTest {
  @MockBean RedissonAutoConfiguration redissonAutoConfiguration;
  @Autowired private FlowerJpaRepository flowerJpaRepository;

  @Test
  @DisplayName("전체 꽃 조회")
  void findAllFlowers() {
    for (int i = 0; i < 3; i++) {
      Flower flower = Flower.builder().flowerName("name").languageOfFlower("~~").id(i + 1L).build();
      flowerJpaRepository.save(flower);
    }
    List<Flower> flowers = flowerJpaRepository.findAll();
    assertThat(flowers.size()).isGreaterThan(1);
  }
}
