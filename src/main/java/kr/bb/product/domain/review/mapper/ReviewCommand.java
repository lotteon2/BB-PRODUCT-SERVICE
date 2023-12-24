package kr.bb.product.domain.review.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import kr.bb.product.common.dto.ReviewRegisterEvent;
import kr.bb.product.common.dto.ReviewType;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewImages;
import kr.bb.product.domain.review.mapper.mapper.ReviewImageMapper;
import kr.bb.product.domain.review.mapper.mapper.ReviewMapper;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

public class ReviewCommand {

  public static ReviewRegisterEvent getReviewRegisterEventData(String productId, Register review) {
    return ReviewRegisterEvent.builder()
        .reviewRating(review.getRating())
        .productId(productId)
        .reviewType(ReviewType.PICKUP) // TODO: review type required
        .build();
  }

  @Getter
  public enum SortOption {
    DATE("createdAt"),
    HIGH("reviewRating"),
    LOW("reviewRating");
    private final String property;

    SortOption(String property) {
      this.property = property;
    }
  }

  @Getter
  @Builder
  public static class Register {
    private String reviewContent;
    private Double rating;
    private List<String> reviewImage;
    private String nickname;
    private String profileImage;

    public static Review toEntity(Register register, Long userId, String productId) {
      return ReviewMapper.INSTANCE.toEntity(register, userId, productId);
    }
  }

  public static class ReviewImage {
    public static List<ReviewImages> toEntityList(List<String> images) {
      return ReviewImageMapper.INSTANCE.toEntityList(images);
    }
  }

  @Getter
  @Builder
  public static class ProductDetailReviewList {
    @Nullable private List<ProductDetailReview> reviews;
    private long totalCnt;

    public static ProductDetailReviewList getData(Page<Review> reviewsByProductId) {
      List<Review> content = reviewsByProductId.getContent();
      return ProductDetailReviewList.builder()
          .reviews(ProductDetailReview.getData(content))
          .totalCnt(reviewsByProductId.getTotalElements())
          .build();
    }
  }

  @Getter
  @Builder
  public static class ProductDetailReview {
    private String profileImage;
    private Double rating;
    private String nickname;
    private String content;
    private List<String> reviewImages;

    public static List<ProductDetailReview> getData(List<Review> content) {
      return content.stream()
          .map(
              item ->
                  ProductDetailReview.builder()
                      .rating(item.getReviewRating())
                      .nickname(item.getNickname())
                      .content(item.getReviewContent())
                      .profileImage(item.getProfileImage())
                      .reviewImages(
                          item.getReviewImages().stream()
                              .map(ReviewImages::getReviewImageUrl)
                              .collect(Collectors.toList()))
                      .build())
          .collect(Collectors.toList());
    }
  }

  @Getter
  @Builder
  public static class StoreReviewList {
    private List<StoreReviewItem> reviews;
    private long totalCnt;

    public static StoreReviewList getData(
        Page<Review> reviewByProductId, Map<String, String> productName) {
      List<StoreReviewItem> reviewItems =
          reviewByProductId.getContent().stream()
              .map(
                  item ->
                      StoreReviewItem.builder()
                          .reviewId(item.getReviewId())
                          .content(item.getReviewContent())
                          .createdAt(item.getCreatedAt())
                          .nickname(item.getNickname())
                          .productName(productName.get(item.getProductId()))
                          .profileImage(item.getProfileImage())
                          .rating(item.getReviewRating())
                          .reviewImages(
                              item.getReviewImages().stream()
                                  .map(ReviewImages::getReviewImageUrl)
                                  .collect(Collectors.toList()))
                          .build())
              .collect(Collectors.toList());
      return StoreReviewList.builder()
          .reviews(reviewItems)
          .totalCnt(reviewByProductId.getTotalElements())
          .build();
    }
  }

  @Getter
  @Builder
  public static class StoreReviewItem {
    private Long reviewId;
    private LocalDateTime createdAt;
    private String profileImage;
    private Double rating;
    private String nickname;
    private String productName;
    private String content;
    private List<String> reviewImages;
  }

  @Getter
  @Builder
  public static class ReviewItem {
    private Long reviewId;
    private LocalDateTime createdAt;
    private Double reviewRating;
    private String reviewContent;
    private List<String> reviewImages;
    private String nickname;
    private String profileImage;
    private String productName;
  }

  @Getter
  @Builder
  public static class ReviewList {
    private List<ReviewItem> reviews;
    private long totalCnt;

    public static ReviewList getData(
        Page<Review> reviewsByUserId, Map<String, String> productNames) {
      return ReviewList.builder()
          .reviews(
              reviewsByUserId.getContent().stream()
                  .map(
                      item ->
                          ReviewItem.builder()
                              .reviewId(item.getReviewId())
                              .createdAt(item.getCreatedAt())
                              .nickname(item.getNickname())
                              .productName(productNames.get(item.getProductId()))
                              .profileImage(item.getProfileImage())
                              .reviewContent(item.getReviewContent())
                              .reviewImages(
                                  item.getReviewImages().stream()
                                      .map(ReviewImages::getReviewImageUrl)
                                      .collect(Collectors.toList()))
                              .reviewRating(item.getReviewRating())
                              .build())
                  .collect(Collectors.toList()))
          .totalCnt(reviewsByUserId.getTotalElements())
          .build();
    }
  }
}
