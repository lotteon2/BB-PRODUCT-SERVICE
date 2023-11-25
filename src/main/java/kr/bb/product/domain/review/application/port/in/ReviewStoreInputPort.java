package kr.bb.product.domain.review.application.port.in;

import kr.bb.product.domain.review.adapter.in.ReviewCommand;
import kr.bb.product.domain.review.application.port.out.ReviewOutPort;
import kr.bb.product.domain.review.application.usecase.ReviewStoreUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewStoreInputPort implements ReviewStoreUseCase {
  private final ReviewOutPort reviewOutPort;

  @Override
  public void writeReview(ReviewCommand.Register review) {

  }
}
