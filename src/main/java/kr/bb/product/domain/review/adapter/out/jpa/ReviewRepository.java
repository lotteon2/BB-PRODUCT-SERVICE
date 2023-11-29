package kr.bb.product.domain.review.adapter.out.jpa;

import java.util.List;
import kr.bb.product.domain.review.application.port.out.ReviewOutPort;
import kr.bb.product.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepository implements ReviewOutPort {
  private final ReviewJpaRepository repository;

  @Override
  public void createReview(Review review) {
    repository.save(review);
  }

  @Override
  public List<Review> findAll() {
    return repository.findAll();
  }
}
