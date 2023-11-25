package kr.bb.product.domain.review.adapter.out;

import java.util.List;
import kr.bb.product.domain.review.application.port.out.ReviewOutPort;
import kr.bb.product.domain.review.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository implements ReviewOutPort {
  private final ReviewJpaRepository repository;

  public ReviewRepository(ReviewJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public void createReview(Review review) {
    repository.save(review);
  }

  @Override
  public List<Review> findAll() {
    return repository.findAll();
  }
}
