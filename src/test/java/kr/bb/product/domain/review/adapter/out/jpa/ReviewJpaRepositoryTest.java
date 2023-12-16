package kr.bb.product.domain.review.adapter.out.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewCommand.SortOption;
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
class ReviewJpaRepositoryTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired ReviewJpaRepository reviewJpaRepository;

  @Test
  void getReviewByProductId() {
    Review reviewContent =
        Review.builder()
            .reviewContent("review content")
            .reviewRating(4.5)
            .productId("5678i")
            .userId(1L)
            .build();
    for (int i = 0; i < 3; i++) {
      ReviewImages images =
          ReviewImages.builder()
              .reviewImageUrl(String.format("imageurl%d", i))
              .review(reviewContent)
              .build();
      reviewContent.getReviewImages().add(images);
    }
    Review save = reviewJpaRepository.save(reviewContent);

    List<String> id = new ArrayList<>();
    id.add("5678i");
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Direction.DESC, "createdAt"));
    Page<Review> reviewByProductId = reviewJpaRepository.findReviewByProductIds(id, pageRequest);
    System.out.println(reviewByProductId.toString());
  }

  @Test
  @DisplayName("리뷰 조회 : 마이페이지")
  void findReviewsByUserId() {
    for (int i = 0; i < 4; i++) {
      Review reviewContent =
          Review.builder()
              .reviewId(999L + i)
              .reviewRating(1.0 + i)
              .userId(123L)
              .reviewContent("reviewContent")
              .build();
      for (int j = 0; j < 3; j++) {
        ReviewImages reviewImages =
            ReviewImages.builder().reviewImageUrl("url" + j).review(reviewContent).build();
        reviewContent.getReviewImages().add(reviewImages);
      }
      reviewJpaRepository.save(reviewContent);
    }

    PageRequest pageRequest =
        PageRequest.of(0, 5, Sort.by(Direction.DESC, SortOption.LOW.getProperty()));

    Page<Review> reviewsByUserId = reviewJpaRepository.findReviewsByUserId(123L, pageRequest);
    List<Review> content = reviewsByUserId.getContent();
    int totalPages = reviewsByUserId.getTotalPages();
    assertThat(content.size()).isEqualTo(4);
    assertThat(totalPages).isEqualTo(1L);
  }
}
