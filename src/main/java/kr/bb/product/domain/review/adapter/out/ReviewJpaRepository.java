package kr.bb.product.domain.review.adapter.out;

import kr.bb.product.domain.review.application.out.ReviewOutPort;
import kr.bb.product.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

}
