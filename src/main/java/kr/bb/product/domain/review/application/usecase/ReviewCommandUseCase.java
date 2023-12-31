package kr.bb.product.domain.review.application.usecase;

import kr.bb.product.domain.review.mapper.ReviewCommand;

public interface ReviewCommandUseCase {
  void writeReview(ReviewCommand.Register review, Long userId, String productId);
}
