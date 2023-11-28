package kr.bb.product.domain.review.application.port.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import kr.bb.product.domain.product.adapter.out.mongo.ProductMongoRepository;
import kr.bb.product.domain.product.application.port.in.ProductCommandInputPort;
import kr.bb.product.domain.product.entity.Product;
import kr.bb.product.domain.product.entity.ProductCommand;
import kr.bb.product.domain.product.vo.ProductFlowersRequestData;
import kr.bb.product.domain.review.adapter.out.jpa.ReviewJpaRepository;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewCommand.SortOption;
import kr.bb.product.domain.review.entity.ReviewCommand.StoreReview.StoreReviewItem;
import kr.bb.product.domain.review.entity.ReviewImages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewQueryInputPortTest {
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

    List<StoreReviewItem> reviewByStoreId =
        reviewQueryInputPort.findReviewByStoreId(1L, pageRequest, SortOption.LOW);
    assertThat(reviewByStoreId.size()).isEqualTo(5);
    assertThat(reviewByStoreId.get(0).getRating()).isEqualTo(4.5);
  }
  @Test
  @DisplayName("리뷰 조회 별점 높은 순 - 한 페이지 개수 5개")
  void testFindReviewByStoreIdHighRating() {
    productMongoRepository.deleteAll();
    PageRequest pageRequest = PageRequest.of(0, 5);
    productCommandInputPort.createProduct(getProductRequestData());
    createReviews();

    List<StoreReviewItem> reviewByStoreId =
        reviewQueryInputPort.findReviewByStoreId(1L, pageRequest, SortOption.HIGH);
    assertThat(reviewByStoreId.size()).isEqualTo(5);
    assertThat(reviewByStoreId.get(0).getRating()).isEqualTo(13.5);
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
}
