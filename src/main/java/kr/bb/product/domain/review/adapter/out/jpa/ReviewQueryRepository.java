package kr.bb.product.domain.review.adapter.out.jpa;

import java.util.List;
import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
import kr.bb.product.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository implements ReviewQueryOutPort {
  private final ReviewJpaRepository reviewJpaRepository;

  @Override
  public List<Review> findReviewByProductId(List<String> productId, Pageable pageable) {
    return reviewJpaRepository.getReviewByProductId(productId, pageable);
  }
}
