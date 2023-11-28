package kr.bb.product.domain.review.application.port.in;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.application.port.out.ProductQueryOutPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
import kr.bb.product.domain.review.application.usecase.ReviewQueryUseCase;
import kr.bb.product.domain.review.entity.ReviewCommand;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.Review;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.StoreReviewItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewQueryInputPort implements ReviewQueryUseCase {
  private final ReviewQueryOutPort reviewQueryOutPort;
  private final ProductQueryOutPort productQueryOutPort;

  @Override
  public List<StoreReviewItem> findReviewByStoreId(Long storeId, Pageable pageable) {
    Page<Product> productByStoreId = productQueryOutPort.findProductByStoreId(storeId, pageable);
    List<ReviewCommand.StoreReview.StoreReviewItem> reviewItems = new ArrayList<>();
    productByStoreId.forEach(
        item -> {
          List<Review> review = reviewQueryOutPort.findReviewByProductId(item.getProductId());
          review.forEach(
              reviews -> {
                reviewItems.add(
                    ReviewCommand.StoreReview.StoreReviewItem.builder()
                        .reviewId(reviews.getReviewId())
                        .content(reviews.getReviewContent())
                        .productName(item.getProductName())
                        .rating(reviews.getReviewRating())
                        .nickname(reviews.getNickname())
                        .profileImage(reviews.getProfileImage())
                        .build());
              });
        });
    return reviewItems;
  }
}
