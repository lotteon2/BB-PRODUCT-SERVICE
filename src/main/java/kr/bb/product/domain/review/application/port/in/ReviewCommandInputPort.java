package kr.bb.product.domain.review.application.port.in;

import java.util.List;
import kr.bb.product.domain.review.application.port.out.ReviewOutPort;
import kr.bb.product.domain.review.application.usecase.ReviewCommandUseCase;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewImages;
import kr.bb.product.domain.review.mapper.ReviewCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommandInputPort implements ReviewCommandUseCase {
  private final ReviewOutPort reviewOutPort;

  /**
   * 리뷰 작성: 리뷰, 리뷰 이미지 저장
   *
   * @param review 리뷰
   * @param userId 작성자 id
   * @param productId 상품 id
   */
  @Transactional
  @Override
  public void writeReview(ReviewCommand.Register review, Long userId, String productId) {
    Review reviewEntity = ReviewCommand.Register.toEntity(review, userId, productId);
    List<ReviewImages> images = ReviewCommand.ReviewImage.toEntityList(review.getReviewImage());

    images.stream()
        .peek(img -> img.setReview(reviewEntity))
        .forEach(reviewEntity.getReviewImages()::add);

    reviewOutPort.createReview(reviewEntity);
  }
}
