package kr.bb.product.domain.review.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class ReviewCommand {
  @Getter
  @Builder
  public static class StoreReview {
    private Long reviewId;
    private LocalDateTime createdAt;
    private String profileImage;
    private Double rating;
    private String nickname;
    private String productName;
    private String content;
    private List<String> reviewImages;
  }
}
