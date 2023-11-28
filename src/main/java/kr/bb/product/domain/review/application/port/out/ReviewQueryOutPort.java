package kr.bb.product.domain.review.application.port.out;


import java.util.List;
import kr.bb.product.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;

public interface ReviewQueryOutPort {
  List<Review> findReviewByProductId(List<String> productId, Pageable pageable);
}
