package kr.bb.product.domain.review.application.port.out;

import java.util.List;
import kr.bb.product.domain.review.entity.Review;

public interface ReviewQueryOutPort {
  List<Review> findReviewByProductId(List<String> productId);
}
