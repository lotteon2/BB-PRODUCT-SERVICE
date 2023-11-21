package kr.bb.product.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_images_id")
  private Long id;

  @Column(name = "review_image_url")
  private String reviewImageUrl;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinColumn(name = "id")
  private Review review;

}
