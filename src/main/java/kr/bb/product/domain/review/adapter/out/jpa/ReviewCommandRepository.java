package kr.bb.product.domain.review.adapter.out.jpa;

import java.util.List;
import kr.bb.product.domain.review.application.port.out.ReviewOutPort;
import kr.bb.product.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewCommandRepository implements ReviewOutPort {
  private final ReviewJpaRepository reviewJpaRepository;

  @Override
  public void createReview(Review review) {
    reviewJpaRepository.save(review);
  }

  @Override
  public List<Review> findAll() {
    return reviewJpaRepository.findAll();
  }
}
