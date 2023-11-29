package kr.bb.product.domain.review.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kr.bb.product.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@ToString
@Table(name = "review")
public class Review extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_id")
  private Long reviewId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "review_content")
  private String reviewContent;

  @Column(name = "review_rating")
  private Double reviewRating;

  @Column(name = "product_id")
  private String productId;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "profile_image")
  private String profileImage;

  @Builder.Default
  @OneToMany(
      mappedBy = "review",
      cascade = {CascadeType.PERSIST})
  private List<ReviewImages> reviewImages = new ArrayList<>();

  public void setReviewImages(List<ReviewImages> reviewImages) {
    this.reviewImages = reviewImages;
  }
}
