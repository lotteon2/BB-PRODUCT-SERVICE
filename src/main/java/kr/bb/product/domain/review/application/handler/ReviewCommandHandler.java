package kr.bb.product.domain.review.application.handler;

import kr.bb.product.domain.review.application.usecase.ReviewCommandUseCase;
import kr.bb.product.domain.review.infrastructure.event.ReviewSNSPublisher;
import kr.bb.product.domain.review.mapper.ReviewCommand;
import kr.bb.product.domain.review.mapper.ReviewCommand.Register;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewCommandHandler {
  private final ReviewCommandUseCase reviewCommandUseCase;
  private final ReviewSNSPublisher reviewSNSPublisher;

  public void writeReview(Register review, Long userId, String productId) {
    // save review
    reviewCommandUseCase.writeReview(review, userId, productId);
    // sns publish
    reviewSNSPublisher.reviewRegisterEventPublish(
        ReviewCommand.getReviewRegisterEventData(productId, review));
  }
}
