package kr.bb.product.domain.review.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import kr.bb.product.domain.review.entity.mapper.ReviewImageMapper;
import kr.bb.product.domain.review.entity.mapper.ReviewMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

public class ReviewCommand {
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
    @Nullable private List<ProductDetailReview> productReview;
    private int totalCnt;

    public static ProductDetailReviewList getData(Page<Review> reviewsByProductId) {
      List<Review> content = reviewsByProductId.getContent();
      return ProductDetailReviewList.builder()
          .productReview(ProductDetailReview.getData(content))
          .totalCnt(reviewsByProductId.getTotalPages())
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
  public static class StoreReview {
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
    @ToString
    public static class Review {
      private Long reviewId;
      private LocalDateTime createdAt;
      private Double reviewRating;
      private String reviewContent;
      private List<String> reviewImages;
      private String nickname;
      private String profileImage;

      public Review(
          Long reviewId,
          LocalDateTime createdAt,
          Double reviewRating,
          String reviewContent,
          List<String> reviewImages,
          String nickname,
          String profileImage) {
        this.reviewId = reviewId;
        this.createdAt = createdAt;
        this.reviewRating = reviewRating;
        this.reviewContent = reviewContent;
        this.reviewImages = reviewImages;
        this.nickname = nickname;
        this.profileImage = profileImage;
      }
    }
  }
}
