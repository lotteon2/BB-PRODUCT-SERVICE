package kr.bb.product.domain.review.application.usecase;

import kr.bb.product.domain.review.mapper.ReviewCommand;
import kr.bb.product.domain.review.mapper.ReviewCommand.SortOption;
import org.springframework.data.domain.Pageable;

public interface ReviewQueryUseCase {
  ReviewCommand.StoreReviewList findReviewByStoreId(Long storeId, Pageable pageable, SortOption sortOption);

  ReviewCommand.ProductDetailReviewList findReviewsByProductId(
      String productId, Pageable pageable, ReviewCommand.SortOption sortOption);

  ReviewCommand.ReviewList findReviewsByUserId(
      Long userId, Pageable pageable, SortOption sortOption);
}
