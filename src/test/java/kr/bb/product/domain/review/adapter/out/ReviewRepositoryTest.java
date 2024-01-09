package kr.bb.product.domain.review.adapter.out;

import java.util.List;
import kr.bb.product.domain.review.adapter.out.jpa.ReviewCommandRepository;
import kr.bb.product.domain.review.entity.Review;
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
class ReviewRepositoryTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired ReviewCommandRepository reviewRepositoryImpl;
  @MockBean RedissonAutoConfiguration redissonAutoConfiguration;

  @Test
  @DisplayName("review repository adapter test")
  void adapterReviewSave() {
    reviewRepositoryImpl.createReview(
        Review.builder().reviewContent("content").reviewRating(4.5).userId(1L).build());
    List<Review> all = reviewRepositoryImpl.findAll();
    System.out.println(all.size());
  }
}
