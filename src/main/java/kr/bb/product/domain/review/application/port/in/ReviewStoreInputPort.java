package kr.bb.product.domain.review.application.port.in;

import java.util.List;
import kr.bb.product.domain.review.adapter.in.ReviewCommand;
import kr.bb.product.domain.review.adapter.in.ReviewCommand.Register;
import kr.bb.product.domain.review.application.port.out.ReviewOutPort;
import kr.bb.product.domain.review.application.usecase.ReviewStoreUseCase;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewImages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewStoreInputPort implements ReviewStoreUseCase {
  private final ReviewOutPort reviewOutPort;

  /**
   * 리뷰 작성: 리뷰, 리뷰 이미지 저장
   *
   * @param review 리뷰
   * @param userId 작성자 id
   * @param productId 상품 id
   */
  @Override
  public void writeReview(ReviewCommand.Register review, Long userId, String productId) {
    Review reviewEntity = ReviewCommand.Register.toEntity(review, userId, productId);
    List<ReviewImages> images = ReviewCommand.ReviewImage.toEntityList(review.getReviewImage());
    reviewEntity.setReviewImages(images);
    reviewOutPort.createReview(reviewEntity);
  }
}
