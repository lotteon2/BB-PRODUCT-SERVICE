package kr.bb.product.domain.review.adapter.out.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewImages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
class ReviewQueryRepositoryTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired ReviewJpaRepository reviewJpaRepository;

  @Test
  @DisplayName("상품 id에 따른 리뷰 조회 ")
  void findReviewByproductId() {
    List<ReviewImages> reviewImages = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      ReviewImages build = ReviewImages.builder().reviewImageUrl("url").build();
      reviewImages.add(build);
    }
    for (int i = 0; i < 10; i++) {
      Review build =
          Review.builder()
              .productId("123")
              .reviewImages(reviewImages)
              .reviewRating(1.0 + i)
              .reviewContent(i + " content")
              .build();
      reviewJpaRepository.save(build);
    }
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Direction.DESC, "reviewRating"));
    Page<Review> reviewsByProductId =
        reviewJpaRepository.findReviewsByProductId("123", pageRequest);
    List<Review> content = reviewsByProductId.getContent();
    assertThat(content.size()).isEqualTo(5);
    assertThat(content.get(0).getReviewRating() > content.get(1).getReviewRating()).isTrue();
    assertThat(content.get(0).getReviewImages().size()).isEqualTo(3);
  }
}
