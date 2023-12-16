package kr.bb.product.domain.review.application.port.out;

import bloomingblooms.errors.EntityNotFoundException;
import java.util.List;
import kr.bb.product.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewQueryOutPort {

  /**
   * 가게 관리 페이지 리뷰 조회
   *
   * @param productId
   * @param pageable
   * @return
   * @throws EntityNotFoundException
   */
  List<Review> findReviewByProductId(List<String> productId, Pageable pageable)
      throws EntityNotFoundException;

  Page<Review> findReviewsByProductId(String productId, Pageable pageable);

  Page<Review> findReviewsByUserId(Long userId, Pageable pageable);
}
