package kr.bb.product.domain.review.application.usecase;

import java.util.List;
import kr.bb.product.domain.review.entity.ReviewCommand;
import kr.bb.product.domain.review.entity.ReviewCommand.SortOption;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.StoreReviewItem;
import org.springframework.data.domain.Pageable;

public interface ReviewQueryUseCase {
  List<StoreReviewItem> findReviewByStoreId(Long storeId, Pageable pageable, SortOption sortOption);

  ReviewCommand.ProductDetailReviewList findReviewsByProductId(
      String productId, Pageable pageable, ReviewCommand.SortOption sortOption);
}
