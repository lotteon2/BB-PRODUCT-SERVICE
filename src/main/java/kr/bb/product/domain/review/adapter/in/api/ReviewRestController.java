package kr.bb.product.domain.review.adapter.in.api;

import kr.bb.product.domain.review.adapter.in.ReviewCommand;
import kr.bb.product.domain.review.application.usecase.ReviewStoreUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewRestController {
  private final ReviewStoreUseCase reviewStoreUseCase;

  @PostMapping("{productId}/reviews")
  public void writeReview(
      @PathVariable String productId,
      @RequestHeader Long userId,
      @RequestBody ReviewCommand.Register review) {
    System.out.println(userId);
    System.out.println(productId);
    System.out.println(review);
    reviewStoreUseCase.writeReview(review, userId, productId);
  }
}
