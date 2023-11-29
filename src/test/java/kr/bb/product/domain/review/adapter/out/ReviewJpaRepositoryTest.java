package kr.bb.product.domain.review.adapter.out;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import kr.bb.product.config.JpaConfiguration;
import kr.bb.product.config.QueryDslConfiguration;
import kr.bb.product.domain.review.adapter.out.jpa.ReviewJpaRepository;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewImages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaConfiguration.class, QueryDslConfiguration.class})
class ReviewJpaRepositoryTest {
  @Autowired ReviewJpaRepository reviewJpaRepository;

  @Test
  @DisplayName("리뷰 작성 repository 테스트")
  void createReviewTest() {
    reviewJpaRepository.save(
        Review.builder()
            .reviewContent("review content")
            .reviewRating(4.5)
            .productId("5678i")
            .userId(1L)
            .build());
    List<Review> all = reviewJpaRepository.findAll();
    assertThat(all.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("리뷰, 리뷰 이미지 영속성 전이 저장")
  void createReviewImageReview() {
    Review reviewContent =
        Review.builder()
            .reviewContent("review content")
            .reviewRating(4.5)
            .productId("5678i")
            .userId(1L)
            .build();
    for (int i = 0; i < 3; i++) {
      ReviewImages images =
          ReviewImages.builder().reviewImageUrl(String.format("imageurl%d", i)).build();
      reviewContent.getReviewImages().add(images);
    }
    Review save = reviewJpaRepository.save(reviewContent);
    assertThat(save.getReviewImages().size()).isEqualTo(3);
  }
}
