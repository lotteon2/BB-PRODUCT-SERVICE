package kr.bb.product.domain.review.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.review.adapter.in.ReviewCommand.Register;
import kr.bb.product.domain.review.adapter.out.ReviewRepository;
import kr.bb.product.domain.review.application.port.out.ReviewOutPort;
import kr.bb.product.domain.review.entity.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewStoreInputPortTest {
  @Autowired ReviewOutPort reviewOutPort;
  @Autowired ReviewRepository reviewRepository;

  @Test
  @DisplayName("리뷰 작성 service logic")
  void writeReviewServiceLogic() {
    List<String> images = new ArrayList<>();
    images.add("review image");
    Register registerReview =
        Register.builder().reviewContent("content").reviewImage(images).rating(4.5).build();

    Long userId = 1L;
    String productId = "1234j";

    Review entity = Register.toEntity(registerReview, userId, productId);
    reviewOutPort.createReview(entity);

    Review review = reviewRepository.findAll().get(0);
    assertThat(review.getReviewContent()).isEqualTo(registerReview.getReviewContent());
  }
}
