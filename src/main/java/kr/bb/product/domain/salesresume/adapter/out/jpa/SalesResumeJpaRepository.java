package kr.bb.product.domain.salesresume.adapter.out.jpa;

import java.util.List;
import kr.bb.product.domain.salesresume.entity.SalesResume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SalesResumeJpaRepository extends JpaRepository<SalesResume, Long> {
  @Query("select r from SalesResume r where r.productId=:productId and r.isNotified=false ")
  List<SalesResume> findSaleResumeByProductId(String productId);
}
