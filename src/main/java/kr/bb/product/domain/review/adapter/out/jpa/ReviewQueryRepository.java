package kr.bb.product.domain.review.adapter.out.jpa;

import java.util.List;
import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
import kr.bb.product.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository implements ReviewQueryOutPort {
  private final ReviewJpaRepository reviewJpaRepository;

  @Override
  public Page<Review> findReviewByProductId(List<String> productId, Pageable pageable) {
    return reviewJpaRepository.findReviewByProductIds(productId, pageable);
  }

  @Override
  public Page<Review> findReviewsByProductId(String productId, Pageable pageable) {
    return reviewJpaRepository.findReviewsByProductId(productId, pageable);
  }

  @Override
  public Page<Review> findReviewsByUserId(Long userId, Pageable pageable) {
    return reviewJpaRepository.findReviewsByUserId(userId, pageable);
  }

  @Override
  public Long findReviewCountByProductId(String productId) {
    return reviewJpaRepository.findReviewCountByProductId(productId);
  }
}
