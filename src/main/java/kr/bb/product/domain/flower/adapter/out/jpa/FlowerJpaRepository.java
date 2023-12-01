package kr.bb.product.domain.flower.adapter.out.jpa;

import kr.bb.product.domain.flower.entity.Flower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowerJpaRepository extends JpaRepository<Flower, Long> { }
