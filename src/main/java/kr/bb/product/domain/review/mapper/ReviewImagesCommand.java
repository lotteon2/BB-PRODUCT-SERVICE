package kr.bb.product.domain.review.mapper;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

public class ReviewImagesCommand {
  @Getter
  public static class ReviewImageList {
    private final String reviewImage;

    @QueryProjection
    public ReviewImageList(String reviewImage) {
      this.reviewImage = reviewImage;
    }
  }
}
