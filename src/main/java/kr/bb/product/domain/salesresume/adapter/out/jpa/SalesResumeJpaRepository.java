package kr.bb.product.domain.salesresume.adapter.out.jpa;

import kr.bb.product.domain.salesresume.entity.SalesResume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesResumeJpaRepository extends JpaRepository<SalesResume, Long> {}
