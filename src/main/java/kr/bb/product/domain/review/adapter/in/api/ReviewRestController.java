package kr.bb.product.domain.review.adapter.in.api;

import bloomingblooms.response.CommonResponse;
import kr.bb.product.domain.review.application.handler.ReviewCommandHandler;
import kr.bb.product.domain.review.application.usecase.ReviewCommandUseCase;
import kr.bb.product.domain.review.application.usecase.ReviewQueryUseCase;
import kr.bb.product.domain.review.mapper.ReviewCommand;
import kr.bb.product.domain.review.mapper.ReviewCommand.SortOption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewRestController {
  private final ReviewCommandUseCase reviewCommandUseCase;
  private final ReviewQueryUseCase reviewQueryUseCase;
  private final ReviewCommandHandler reviewCommandHandler;

  @GetMapping("stores/{storeId}/reviews")
  public CommonResponse<ReviewCommand.StoreReviewList> getStoreReviews(
      @PathVariable Long storeId,
      @PageableDefault(
              page = 0,
              size = 10,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable,
      @RequestParam("sort-option") SortOption sortOption) {
    return CommonResponse.<ReviewCommand.StoreReviewList>builder()
        .message("가세 사장 리뷰 조회")
        .data(reviewQueryUseCase.findReviewByStoreId(storeId, pageable, sortOption))
        .build();
  }

  @PostMapping("{productId}/reviews")
  public void writeReview(
      @PathVariable String productId,
      @RequestHeader Long userId,
      @RequestBody ReviewCommand.Register review) {
    reviewCommandHandler.writeReview(review, userId, productId);
  }

  @GetMapping("{productId}/reviews")
  public CommonResponse<ReviewCommand.ProductDetailReviewList> getProductDetailReviews(
      @PathVariable String productId,
      @PageableDefault(
              page = 0,
              size = 10,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable,
      @RequestParam("sort-option") SortOption sortOption) {
    return CommonResponse.success(
        reviewQueryUseCase.findReviewsByProductId(productId, pageable, sortOption));
  }

  @GetMapping("reviews/mypage")
  public CommonResponse<ReviewCommand.ReviewList> getReviewByUserId(
      @RequestHeader Long userId,
      @PageableDefault(
              page = 0,
              size = 10,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable,
      @RequestParam("sort-option") SortOption sortOption) {
    return CommonResponse.success(
        reviewQueryUseCase.findReviewsByUserId(userId, pageable, sortOption));
  }
}
