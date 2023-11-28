package kr.bb.product.domain.review.entity;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import kr.bb.product.domain.review.entity.ReviewImagesCommand.ReviewImageList;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class ReviewCommand {
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
      private ReviewImageList reviewImages;
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

      @QueryProjection
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
