package kr.bb.product.domain.review.adapter.out;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kr.bb.product.domain.review.entity.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ReviewRepositoryTest {
  @Autowired ReviewJpaRepository reviewJpaRepository;

  @Test
  @DisplayName("리뷰 작성 repository 테스트")
  void createReviewTest() {
    reviewJpaRepository.save(
        Review.builder()
            .content("review content")
            .rating(4.5)
            .productId("5678i")
            .userId(1L)
            .build());
    List<Review> all = reviewJpaRepository.findAll();
    assertThat(all.size()).isEqualTo(1);
  }
}
