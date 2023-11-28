package kr.bb.product.domain.review.adapter.out.jpa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewImages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
class ReviewJpaRepositoryTest {
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
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Direction.DESC, "created_at"));
    List<Review> reviewByProductId = reviewJpaRepository.getReviewByProductId(id, pageRequest);
    System.out.println(reviewByProductId.toString());
  }
}
