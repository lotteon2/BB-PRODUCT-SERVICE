package kr.bb.product.domain.review.application.port.out;

import java.util.List;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.Review;

public interface ReviewQueryOutPort {
  List<Review> findReviewsWithReviewImages(List<String> productIds);

  List<Review> findReviewByProductId(String productId);
}
