package kr.bb.product.domain.review.adapter.out.jpa;

import kr.bb.product.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

}
