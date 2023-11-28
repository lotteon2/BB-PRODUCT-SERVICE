package kr.bb.product.domain.review.adapter.in;

import java.util.List;
import kr.bb.product.domain.review.entity.Review;
import kr.bb.product.domain.review.entity.ReviewImages;
import kr.bb.product.domain.review.entity.mapper.ReviewImageMapper;
import kr.bb.product.domain.review.entity.mapper.ReviewMapper;
import lombok.Builder;
import lombok.Getter;

public class ReviewCommand {
  @Getter
  @Builder
  public static class Register {
    private String reviewContent;
    private Double rating;
    private List<String> reviewImage;
    private String nickname;

    public static Review toEntity(Register register, Long userId, String productId) {
      return ReviewMapper.INSTANCE.toEntity(register, userId, productId);
    }
  }

  public static class ReviewImage {
    public static List<ReviewImages> toEntityList(List<String> images) {
      return ReviewImageMapper.INSTANCE.toEntityList(images);
    }
  }
}
