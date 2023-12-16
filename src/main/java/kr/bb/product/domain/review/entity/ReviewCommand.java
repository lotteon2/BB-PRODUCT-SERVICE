package kr.bb.product.domain.review.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import kr.bb.product.domain.review.entity.mapper.ReviewImageMapper;
import kr.bb.product.domain.review.entity.mapper.ReviewMapper;
import lombok.Builder;
import lombok.Getter;
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
    @Nullable private List<ProductDetailReview> reviews;
    private int totalCnt;

    public static ProductDetailReviewList getData(Page<Review> reviewsByProductId) {
      List<Review> content = reviewsByProductId.getContent();
      return ProductDetailReviewList.builder()
          .reviews(ProductDetailReview.getData(content))
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
  public static class StoreReviewList {
    private List<StoreReviewItem> reviews;
    private int totalCnt;

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
          .totalCnt(reviewByProductId.getTotalPages())
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
  }

  @Getter
  @Builder
  public static class ReviewList {
    private List<ReviewItem> reviews;
    private int totalCnt;

    public static ReviewList getData(Page<Review> reviewsByUserId) {
      return ReviewList.builder()
          .reviews(
              reviewsByUserId.getContent().stream()
                  .map(
                      item ->
                          ReviewItem.builder()
                              .reviewId(item.getReviewId())
                              .createdAt(item.getCreatedAt())
                              .nickname(item.getNickname())
                              .profileImage(item.getProfileImage())
                              .reviewContent(item.getReviewContent())
                              .reviewImages(
                                  item.getReviewImages().stream()
                                      .map(ReviewImages::getReviewImageUrl)
                                      .collect(Collectors.toList()))
                              .reviewRating(item.getReviewRating())
                              .build())
                  .collect(Collectors.toList()))
          .totalCnt(reviewsByUserId.getTotalPages())
          .build();
    }
  }
}
