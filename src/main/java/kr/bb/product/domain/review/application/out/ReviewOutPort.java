package kr.bb.product.domain.review.application.out;

import java.util.List;
import kr.bb.product.domain.review.entity.Review;

public interface ReviewOutPort {
    void createReview(Review review);
    List<Review> findAll();
}
