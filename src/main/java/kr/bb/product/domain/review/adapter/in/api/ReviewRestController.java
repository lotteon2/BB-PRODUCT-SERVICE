package kr.bb.product.domain.review.adapter.in.api;

import bloomingblooms.response.CommonResponse;
import java.util.List;
import javax.ws.rs.QueryParam;
import kr.bb.product.domain.review.adapter.in.ReviewCommand;
import kr.bb.product.domain.review.application.usecase.ReviewCommandUseCase;
import kr.bb.product.domain.review.application.usecase.ReviewQueryUseCase;
import kr.bb.product.domain.review.entity.ReviewCommand.SortOption;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.StoreReviewItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewRestController {
  private final ReviewCommandUseCase reviewStoreUseCase;
  private final ReviewQueryUseCase reviewQueryUseCase;

  @GetMapping("stores/{storeId}/reviews")
  public CommonResponse<List<StoreReviewItem>> getStoreReviews(
      @PathVariable Long storeId,
      Pageable pageable,
      @QueryParam("sort-option") SortOption sortOption) {
    List<StoreReviewItem> reviewByStoreId =
        reviewQueryUseCase.findReviewByStoreId(storeId, pageable, sortOption);
    return CommonResponse.<List<StoreReviewItem>>builder()
        .message("가세 사장 리뷰 조회")
        .data(reviewByStoreId)
        .build();
  }

  @PostMapping("{productId}/reviews")
  public void writeReview(
      @PathVariable String productId,
      @RequestHeader Long userId,
      @RequestBody ReviewCommand.Register review) {
    reviewStoreUseCase.writeReview(review, userId, productId);
  }
}
