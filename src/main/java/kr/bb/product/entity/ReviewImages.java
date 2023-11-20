package kr.bb.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "review_images")
public class ReviewImages extends BaseEntity {
  @Id
  @Column(name = "review_id")
  private Long reviewId;

  @Column(name = "review_image")
  private String reviewImage;
}
