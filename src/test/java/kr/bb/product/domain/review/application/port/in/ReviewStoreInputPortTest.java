package kr.bb.product.domain.review.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import kr.bb.product.domain.review.adapter.out.jpa.ReviewCommandRepository;
import kr.bb.product.domain.review.application.port.out.ReviewOutPort;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewImages;
import kr.bb.product.domain.review.mapper.ReviewCommand;
import kr.bb.product.domain.review.mapper.ReviewCommand.Register;
import kr.bb.product.domain.review.mapper.ReviewCommand.ReviewImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewStoreInputPortTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired ReviewOutPort reviewOutPort;
  @Autowired ReviewCommandRepository reviewRepository;
  @Autowired ReviewCommandInputPort reviewStoreInputPort;
  @Autowired EntityManager em;
  @MockBean RedissonAutoConfiguration redissonAutoConfiguration;

  @Test
  @DisplayName("리뷰 작성 service logic")
  void writeReviewServiceLogic() {
    List<String> images = new ArrayList<>();
    images.add("review image");
    ReviewCommand.Register registerReview =
        ReviewCommand.Register.builder()
            .reviewContent("content")
            .reviewImage(images)
            .rating(4.5)
            .build();

    Long userId = 1L;
    String productId = "1234j";

    Review entity = ReviewCommand.Register.toEntity(registerReview, userId, productId);
    reviewOutPort.createReview(entity);

    Review review = reviewRepository.findAll().get(0);
    assertThat(review.getReviewContent()).isEqualTo(registerReview.getReviewContent());
  }

  @Test
  @DisplayName("리뷰, 리뷰 이미지 저장: 영속성 전이")
  void saveReviewImageReview() {
    List<String> images = new ArrayList<>();
    images.add("review image");
    images.add("review image2");
    ReviewCommand.Register registerReview =
        ReviewCommand.Register.builder()
            .reviewContent("content")
            .reviewImage(images)
            .rating(4.5)
            .build();
    Review entity = Register.toEntity(registerReview, 1L, "87y");
    List<ReviewImages> entityList = ReviewImage.toEntityList(registerReview.getReviewImage());
    entity.setReviewImages(entityList);

    reviewOutPort.createReview(entity);
    Review review = reviewRepository.findAll().get(0);
    assertThat(review.getReviewImages().size()).isEqualTo(2);
  }

  @Test
  @DisplayName("review create test input port")
  void createReviewAndReviewImages() {
    List<String> images = new ArrayList<>();
    images.add("review image");
    images.add("review image2");
    Register review =
        Register.builder().reviewContent("content").reviewImage(images).rating(4.5).build();
    reviewStoreInputPort.writeReview(review, 1L, "osf98");
    Review reviews = reviewRepository.findAll().get(0);
    assertThat(reviews.getReviewRating()).isEqualTo(4.5);
  }
}
