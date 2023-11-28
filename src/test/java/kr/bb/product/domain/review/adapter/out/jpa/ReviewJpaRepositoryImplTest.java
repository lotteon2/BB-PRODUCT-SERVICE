package kr.bb.product.domain.review.adapter.out.jpa;

import static org.junit.jupiter.api.Assertions.*;

import kr.bb.product.domain.review.application.port.out.ReviewQueryOutPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewJpaRepositoryImplTest {
  @Autowired ReviewJpaRepository reviewJpaRepository;
  @Autowired private ReviewQueryOutPort reviewQueryOutPort;

  @Test
  @DisplayName("가게 사장 리뷰 조회 ")
  void findReviewsWithReviewImages() {
//    Review reviewContent =
//        Review.builder()
//            .reviewContent("review content")
//            .reviewRating(4.5)
//            .productId("5678i")
//            .userId(1L)
//            .build();
//    for (int i = 0; i < 3; i++) {
//      ReviewImages images =
//          ReviewImages.builder()
//              .reviewImageUrl(String.format("imageurl%d", i))
//              .review(reviewContent)
//              .build();
//      reviewContent.getReviewImages().add(images);
//    }
//    Review reviewContent2 =
//        Review.builder()
//            .reviewContent("review content")
//            .reviewRating(4.5)
//            .productId("5678i")
//            .userId(1L)
//            .build();
//    for (int i = 0; i < 3; i++) {
//      ReviewImages images =
//          ReviewImages.builder()
//              .reviewImageUrl(String.format("imageurl%d", i))
//              .review(reviewContent2)
//              .build();
//      reviewContent2.getReviewImages().add(images);
//    }
//    Review save = reviewJpaRepository.save(reviewContent2);
//    Review save2 = reviewJpaRepository.save(reviewContent);
//    List<String> productId = new ArrayList<>();
//    productId.add(save.getProductId());
//
//    List<StoreReview.Review> reviewsWithReviewImages =
//        reviewQueryOutPort.findReviewsWithReviewImages(productId);
//    System.out.println(reviewsWithReviewImages.toString());
//    assertThat(reviewsWithReviewImages.size()).isEqualTo(2);
  }
}
