package kr.bb.product.domain.review.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import bloomingblooms.errors.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.flower.mapper.FlowerCommand.ProductFlowersRequestData;
import kr.bb.product.domain.product.adapter.out.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.application.port.in.ProductCommandInputPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.mapper.ProductCommand;
import kr.bb.product.domain.review.adapter.out.jpa.ReviewJpaRepository;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewCommand;
import kr.bb.product.domain.review.entity.ReviewCommand.ProductDetailReviewList;
import kr.bb.product.domain.review.entity.ReviewCommand.ReviewItem;
import kr.bb.product.domain.review.entity.ReviewCommand.ReviewList;
import kr.bb.product.domain.review.entity.ReviewCommand.SortOption;
import kr.bb.product.domain.review.entity.ReviewImages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewQueryInputPortTest {
  @MockBean SimpleMessageListenerContainer simpleMessageListenerContainer;
  @Autowired ReviewJpaRepository reviewJpaRepository;
  @Autowired private ReviewQueryInputPort reviewQueryInputPort;
  @Autowired private ProductCommandInputPort productCommandInputPort;
  @Autowired private ProductMongoRepository productMongoRepository;

  private ProductCommand.ProductRegister getProductRequestData() {
    List<Long> tagList = new ArrayList<>();
    tagList.add(1L);
    tagList.add(2L);

    List<ProductFlowersRequestData> list = new ArrayList<>();
    list.add(ProductFlowersRequestData.builder().flowerId(1L).flowerCount(2L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(3L).flowerCount(3L).build());
    list.add(ProductFlowersRequestData.builder().flowerId(2L).flowerCount(2L).build());

    return ProductCommand.ProductRegister.builder()
        .categoryId(1L)
        .productTag(tagList)
        .representativeFlower(
            ProductFlowersRequestData.builder().flowerCount(3L).flowerId(1L).build())
        .flowers(list)
        .storeId(1L)
        .productName("Example Product")
        .productSummary("Product Summary")
        .productDescriptionImage("image")
        .productThumbnail("thumbnail")
        .productPrice(100L)
        .productDescriptionImage("image_url")
        .build();
  }

  @Test
  @DisplayName("리뷰 조회 별점 낮은 순 - 한 페이지 개수 5개")
  void testFindReviewByStoreId() {
    productMongoRepository.deleteAll();
    PageRequest pageRequest = PageRequest.of(0, 5);
    productCommandInputPort.createProduct(getProductRequestData());
    createReviews();

    ReviewCommand.StoreReviewList reviewByStoreId =
        reviewQueryInputPort.findReviewByStoreId(1L, pageRequest, SortOption.LOW);
    assertThat(reviewByStoreId.getReviews().size()).isEqualTo(5);
    assertThat(reviewByStoreId.getReviews().get(0).getRating()).isEqualTo(4.5);
  }

  @Test
  @DisplayName("리뷰 조회 별점 높은 순 - 한 페이지 개수 5개")
  void testFindReviewByStoreIdHighRating() {
    productMongoRepository.deleteAll();
    PageRequest pageRequest = PageRequest.of(0, 5);
    productCommandInputPort.createProduct(getProductRequestData());
    createReviews();

    ReviewCommand.StoreReviewList reviewByStoreId =
        reviewQueryInputPort.findReviewByStoreId(1L, pageRequest, SortOption.HIGH);
    assertThat(reviewByStoreId.getReviews().size()).isEqualTo(5);
    assertThat(reviewByStoreId.getReviews().get(0).getRating()).isEqualTo(13.5);
  }

  private void createReviews() {
    List<Product> all = productMongoRepository.findAll();
    for (int i = 0; i < 10; i++) {
      Review reviewContent =
          Review.builder()
              .reviewContent("review content" + i)
              .reviewRating(4.5 + i)
              .productId(all.get(0).getProductId())
              .userId(1L)
              .build();
      for (int j = 0; j < 3; j++) {
        ReviewImages images =
            ReviewImages.builder()
                .reviewImageUrl(String.format("imageurl%d", j))
                .review(reviewContent)
                .build();
        reviewContent.getReviewImages().add(images);
      }
      Review save = reviewJpaRepository.save(reviewContent);
    }
  }

  @Test
  void findReviewByStoreId() {
    productMongoRepository.deleteAll();
    PageRequest pageRequest = PageRequest.of(0, 5);

    Throwable throwable =
        catchThrowable(
            () -> {
              reviewQueryInputPort.findReviewByStoreId(1L, pageRequest, SortOption.LOW);
            });
    assertThat(throwable).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("상품 상세 리뷰 조회")
  void findReviewsByProductId() {
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
    PageRequest pageRequest = PageRequest.of(0, 5);
    ProductDetailReviewList reviewsByProductId =
        reviewQueryInputPort.findReviewsByProductId("123", pageRequest, SortOption.DATE);
    assertThat(reviewsByProductId.getReviews().size()).isEqualTo(5);
    assertThat(reviewsByProductId.getReviews().get(0).getReviewImages().size()).isEqualTo(3);
  }

  @Test
  @DisplayName("마이페이지 리뷰 조회")
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
    PageRequest pageRequest = PageRequest.of(0, 5);
    ReviewList reviewsByUserId =
        reviewQueryInputPort.findReviewsByUserId(123L, pageRequest, SortOption.LOW);
    List<ReviewItem> reviews = reviewsByUserId.getReviews();
    long totalCnt = reviewsByUserId.getTotalCnt();
    assertThat(reviews.size()).isEqualTo(4);
    assertThat(totalCnt).isEqualTo(4);
  }
}
