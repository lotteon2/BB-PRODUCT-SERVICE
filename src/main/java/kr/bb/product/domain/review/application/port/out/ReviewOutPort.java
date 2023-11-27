package kr.bb.product.domain.review.application.port.out;

import java.util.List;
import kr.bb.product.domain.review.entity.Review;
import org.jetbrains.annotations.TestOnly;

public interface ReviewOutPort {
  void createReview(Review review);

  @TestOnly
  List<Review> findAll();
}
