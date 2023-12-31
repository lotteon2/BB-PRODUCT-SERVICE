package kr.bb.product.domain.salesresume.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kr.bb.product.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "sales_resume")
public class SalesResume extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sales_resume_id")
  private Long saleResumeId;

  @Builder.Default
  @Column(name = "is_notified")
  private Boolean isNotified = false;

  @Column(name = "product_id")
  private String productId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "user_name")
  private String userName;

  public SalesResume setNotified() {
    this.isNotified = true;
    return this;
  }
}
